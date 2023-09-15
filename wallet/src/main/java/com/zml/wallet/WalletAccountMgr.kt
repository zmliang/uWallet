package com.zml.wallet

import android.util.Log
import com.self.base.appContext
import com.zml.wallet.entity.TokenEntity
import com.zml.wallet.entity.WalletEntity
import com.zml.wallet.response.EthTransactionResponse
import com.zml.wallet.web3.DecimalFormat
import com.zml.wallet.web3.Web3Impl
import com.zml.wallet.web3.Web3Impl.EthParamBuilder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Utf8String
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.core.DefaultBlockParameterName
import java.io.IOException
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode


class WalletAccountMgr {


    companion object {
        val COIN_ETH = 60
        @Volatile
        private var INSTANCE: WalletAccountMgr? = null

        @Synchronized
        @JvmStatic
        fun get(): WalletAccountMgr {
            if (INSTANCE == null) {
                INSTANCE = WalletAccountMgr()
            }
            return INSTANCE!!
        }
    }


    private fun getGlobalPwd():String{
        return ""
    }

    private val nativeLib = NativeLib()
    private val walletDao = WalletRoomDataBase.get(appContext).walletDao()
    private val tokenDao = WalletRoomDataBase.get(appContext).tokenDao()

    private val WEI = BigDecimal(BigInteger("1000000000000000000"))

    private val web3 = Web3Impl()


    fun init(){
        nativeLib.init(appContext)
    }

    suspend fun createWallet(name: String,chain: Chain): WalletInfo?{
        val pwd = getGlobalPwd()
        val walletKey = nativeLib.createWallet(pwd)
        walletKey?.let {
            val address = getPublicChainAddress(walletKey, pwd)
            val walletEntity = WalletEntity(address = address, passWord = pwd, id = null,
                chainID = chain.chainId, chainName = chain.name, walletKey = walletKey, walletName = name)
            val tmp = walletDao.query(address,chain.name!!,chain.chainId!!)
            var isNew = false
            if (tmp.isEmpty()){
                isNew = true
                walletDao.insert(walletEntity)
            }
            val publicKey = getPublicChainPublicKey(walletKey, pwd)
            return WalletInfo(walletEntity = walletEntity,addr=address,publicKey=publicKey, walletName = name).apply {
                this.isFresh = isNew
            }
        }

        return null
    }

    suspend fun importMnemonic(mnemonic: String?,chain: Chain,walletName: String): WalletInfo?{
        val pwd = getGlobalPwd()
        val walletKey = nativeLib.importMnemonic(mnemonic,pwd)
        walletKey?.let {
            val address = getPublicChainAddress(walletKey, pwd)
            val tmp = walletDao.query(address,chain.name!!,chain.chainId!!)
            var isNew = false
            var walletEntity:WalletEntity
            if (tmp.isEmpty()){
                walletEntity = WalletEntity(address = address, passWord = pwd, id = null,
                    chainID = chain.chainId, chainName = chain.name, walletKey = walletKey, walletName = walletName)
                isNew = true
                walletDao.insert(walletEntity)
            }else{
                walletEntity = tmp[0]
            }
            val publicKey = getPublicChainPublicKey(walletKey, pwd)

            return WalletInfo(walletEntity = walletEntity,addr=address,publicKey=publicKey).apply {
                this.walletName = walletName
                this.isFresh = isNew//是不是新钱包
            }
        }

        return null
    }

    suspend fun importPrivateKey(privateKey: String?, chain: Chain,walletName:String): WalletInfo?{
        val pwd = getGlobalPwd()
        val walletKey = nativeLib.importPrivateKey(privateKey,COIN_ETH)

        walletKey?.let {
            val address = getPublicChainAddress(walletKey, pwd)
            val tmp = walletDao.query(address,chain.name!!,chain.chainId!!)
            var walletEntity :WalletEntity
            var isNew = false
            if (tmp.isEmpty()){
                isNew = true
                walletEntity = WalletEntity(address = address, passWord = pwd, id = null,
                    chainID = chain.chainId, chainName = chain.name, walletKey = walletKey, walletName = walletName)

                walletDao.insert(walletEntity)
            }else{
                walletEntity = tmp[0]
            }
            val publicKey = getPublicChainPublicKey(walletKey, pwd)
            return WalletInfo(walletEntity = walletEntity,addr=address,publicKey=publicKey).apply {
                this.walletName = walletName
                this.isFresh = isNew
            }
        }

        return null
    }

    suspend fun getPublicChainPrivateKey(walletKey: String?, pwd: String?): String?{
        return nativeLib.getPublicChainPrivateKey(walletKey, pwd, COIN_ETH)
    }

    suspend fun getPublicChainPublicKey(walletKey: String?, pwd: String?): String?{
        return nativeLib.getPublicChainPublicKey(walletKey, pwd, COIN_ETH)["secp256k1"]
     }

    suspend fun getPublicChainAddress(walletKey: String?, pwd: String?): String{
        return nativeLib.getPublicChainAddress(walletKey, pwd, COIN_ETH)
    }

    suspend fun getMnemonic(walletKey: String?, pwd: String?):String?{
        return nativeLib.getMnemonic(walletKey, pwd)
    }

    suspend fun queryWalletByChain(chain: Chain):ArrayList<WalletInfo>{
        chain.chainId?.let {
            walletDao.queryByChain(it).let { it1 ->
                val ret = ArrayList<WalletInfo>()
                for (walletEntity in it1) {
                    val address = walletEntity.address
                    val pwd = walletEntity.passWord
                    val publicKey = getPublicChainPublicKey(walletEntity.walletKey, pwd)
                    ret.add(WalletInfo(walletEntity = walletEntity,addr=address,publicKey=publicKey))
                }
                return ret
            }
        }

        return arrayListOf()
    }

    suspend fun queryChainTokens(chain: Chain,wallet: WalletInfo):ArrayList<TokenInfo>{
        //Log.i("zml","查找token参数=$chain,  wallet=$wallet")
        tokenDao.queryByChain(chain.chainId).let {
            val ret = ArrayList<TokenInfo>()
            for (item in it) {
                val _strBalance =  balanceOf(item.contract,wallet.address,chain.rpcs[0])
                var _bal = "0"
                if (_strBalance.isNotEmpty()){
                    _bal = DecimalFormat.div(BigInteger(_strBalance),item.decimal?.toInt()!!,8)
                }

                ret.add(
                    TokenInfo(symbol = item.symbol, decimal = item.decimal, name = item.name,
                    contract = item.contract, chainId = wallet.chainID, balance = _bal))
            }
            return ret
        }
        return arrayListOf()
    }

    suspend fun deleteWallet(wallet:WalletInfo):Boolean{
       val ret= walletDao.deleteBy(wallet.address,wallet.chainName,wallet.chainID)
        Log.i("zml","删除结果=$ret")
        return ret!=0
    }

    suspend fun updateWallet(wallet:WalletInfo):Int{
        wallet.walletName?.let {
            return walletDao.updateBy(wallet.address, it,wallet.chainID)
        }
        return 0
    }
    suspend fun balanceOfMainToken(walletAddress: String):BigDecimal{
        var balance: BigInteger? = null
        try {
            balance = web3.web3j.ethGetBalance(
                walletAddress, DefaultBlockParameterName.LATEST
            ).send().balance
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.i("zml", "主币余额=$balance")
        return BigDecimal(balance).divide(WEI, 8, RoundingMode.HALF_DOWN)
    }

    fun deleteAllToken(){
        tokenDao.deleteAll()
    }


    fun deleteToken(tokenInfo: TokenInfo,chain: Chain):Int{
        Log.i("zml","删除了：$tokenInfo,  $chain")
        return tokenDao.deleteBy(tokenInfo.contract,chain.chainId)
    }

    suspend fun addCustomCoin(contract:String,walletAddress: String,chain: Chain):TokenInfo{
        Log.i("zml","自定义逼 参数：$contract,\n $walletAddress \n ${chain.rpcs[0]}")
        var localRet = tokenDao.queryByContractAndChain(chain.chainId,contract)
        var isFresh = true
        if (localRet.isNotEmpty()){
            isFresh = false
        }

        val ret = TokenInfo(contract=contract, decimal = "", name = "", symbol = "", isFresh = isFresh)

        web3.setRpc(chain.rpcs[0])
        web3.setWalletAddress(walletAddress)
        ret.name = web3.ethCall("name", contract, EthParamBuilder()
            .addOutParams(object : TypeReference<Utf8String>() {})
        )

        ret.symbol = web3.ethCall("symbol", contract, EthParamBuilder()
            .addOutParams(object : TypeReference<Utf8String>() {})
        )

        val decimal = web3.ethCall("decimals", contract, EthParamBuilder()
            .addOutParams(object : TypeReference<Uint256>() {})
        )
        ret.decimal = decimal

        val _strBalance = balanceOf(contract,walletAddress,chain.rpcs[0])
        if (_strBalance.isNotEmpty()){
            ret.balance = DecimalFormat.div(BigInteger(_strBalance),ret.decimal!!.toInt(),8)
        }else{
            ret.balance = "0"
        }

        ret.chainId = chain.chainId

        if (ret.decimal == "" && ret.symbol == "" || ret.name == ""){
            ret.isSuccess=false
            return ret
        }
        tokenDao.insert(TokenEntity(ownerChainId = chain.chainId,
        decimal = ret.decimal, name = ret.name, symbol = ret.symbol, contract = contract))

        return ret
    }

    suspend fun balanceOf(contract: String,walletAddress: String,rpc:String):String{
        Log.i("zml","余额参数：$contract,\n $walletAddress \n $rpc")
        web3.setRpc(rpc)
        web3.setWalletAddress(walletAddress)
        val _strBalance = web3.ethCall("balanceOf",contract, EthParamBuilder()
            .addInParams(Address(walletAddress))
            .addOutParams(object :TypeReference<Uint256>() {}))
        Log.i("zml", "余额=$_strBalance")
        return _strBalance
    }

    suspend fun transferToken(from:String,to:String, privateKey: String?,contractAddress:String,amount:BigInteger,chainId:String):EthTransactionResponse {
        var exception = ""
        try {
            val ret = web3.transferERC20Token(from,to,amount,privateKey, contractAddress,java.lang.Long.decode(chainId))

            return EthTransactionResponse().apply {
                this.data = ret.result
                this.code = if (ret.error == null){0}else ret.error.code
                this.message = if (ret.error == null){"success"}else ret.error.message
            }

        }catch (e:java.lang.Exception){
            e.printStackTrace()
            exception = e.message.toString()
        }



        return EthTransactionResponse().apply {
            this.data = null
            this.code = -1
            this.message = exception
        }
    }


}