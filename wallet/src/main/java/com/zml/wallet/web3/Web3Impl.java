package com.zml.wallet.web3;

import android.util.Log;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Web3Impl{
    private String rpc = "https://polygon-mainnet.infura.io/v3/cf04cee8e88e436c9419811c5edbe86c";

    public Admin web3j = Admin.build(new HttpService(rpc));
    private String walletAddress = "0x5D482E16A2c92d92841eaA2Cd4abcA76948d779F";//钱包地址

    public void setWalletAddress(String address){
        if (this.walletAddress.equals(address)){
            return;
        }
        this.walletAddress = address;
    }

    public void setRpc(String rpc){
        if (this.rpc.equals(rpc)){
            return;
        }
        this.rpc = rpc;
        web3j = Admin.build(new HttpService(rpc));
    }

    public Web3Impl(){

    }

    public BigInteger balanceOf() {
        BigInteger balance = BigInteger.ZERO;
        try {
            balance = web3j.ethGetBalance(
                    walletAddress, DefaultBlockParameterName.LATEST).send().getBalance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void transferToken(String privateKey,String destAddress,String contractAddress,String amount) throws Exception {
        Log.i("zml","转账参数：私钥；"+privateKey+"\n"
        +"目的地："+destAddress+"\n"
        +"合约："+contractAddress+"\n");

        BigInteger bigInteger = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bigInteger);
        Credentials credentials = Credentials.create(ecKeyPair);
        String fromAddress = credentials.getAddress();
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        Address address = new Address(destAddress);
        Uint256 value = new Uint256(new BigInteger(amount));//数量 单位wei
        List<Type> parametersList = new ArrayList<>();
        parametersList.add(address);
        parametersList.add(value);
        List<TypeReference<?>> outList = new ArrayList<>();
        Function function = new Function("transfer", parametersList, outList);
        String encodedFunction = FunctionEncoder.encode(function);
        Log.i("zml","gasPrice:"+ DefaultGasProvider.GAS_PRICE);
        Log.i("zml","gasLimit:"+ DefaultGasProvider.GAS_LIMIT);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,  DefaultGasProvider.GAS_PRICE,
                new BigInteger("210000"), contractAddress, encodedFunction);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        Object transactionHash = ethSendTransaction.getTransactionHash();

        Log.i("zml","交易hash:"+transactionHash.toString());
    }


    /**
     * ERC-20Token交易
     *
     * @param from
     * @param to
     * @param value
     * @param privateKey
     * @return
     * @throws Exception
     */
    public EthSendTransaction transferERC20Token(String from,
                                                 String to,
                                                 BigInteger value,
                                                 String privateKey,
                                                 String contractAddress,
                                                 long chainId
                                                 ) throws Exception {
        //加载转账所需的凭证，用私钥
        Credentials credentials = Credentials.create(privateKey);
        //获取nonce，交易笔数
        BigInteger nonce = getNonce(from);
        //get gasPrice
        BigInteger gasPrice =DefaultGasProvider.GAS_PRICE;
        BigInteger gasLimit = DefaultGasProvider.GAS_LIMIT;

        //创建RawTransaction交易对象
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(to), new Uint256(value)),
                Arrays.asList(new TypeReference<Type>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);

        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce,
                gasPrice,
                gasLimit,
                contractAddress, encodedFunction);


        //签名Transaction，这里要对交易做签名
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction,chainId, credentials);
        String hexValue = Numeric.toHexString(signMessage);
        //发送交易
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        return ethSendTransaction;
    }

    /**
     * 获取nonce，交易笔数
     *
     * @param from
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private BigInteger getNonce(String from) throws ExecutionException, InterruptedException {
        EthGetTransactionCount transactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = transactionCount.getTransactionCount();
        Log.i("zml", "transfer nonce : " + nonce);
        return nonce;
    }


    public String ethCall(String methodName,String contract,EthParamBuilder paramBuilder){

        //input
        List<Type> inputParameters = paramBuilder.inputParameters;

        //output
        List<TypeReference<?>> outputParameters =paramBuilder.outputParameters;

        //function
        Function function = new Function(methodName, inputParameters, outputParameters);
        String data = FunctionEncoder.encode(function);

        //transaction
        Transaction transaction = Transaction.createEthCallTransaction(walletAddress, contract, data);

        EthCall ethCall;
        String value = "";
        try {
            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            Log.i("zml","value="+ethCall.getValue());
            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
            if (results.size() == 1){
                value = results.get(0).getValue().toString();
            }
        }  catch (IOException e) {
            Log.i("zml","e="+e);
        }
        return value;
    }

    public static class EthParamBuilder {

        private List<Type> inputParameters =  new ArrayList<>();

        private List<TypeReference<?>> outputParameters = new ArrayList<>();

        public EthParamBuilder addInParams(Type<?> inParams){
            inputParameters.add(inParams);
            return this;
        }

         public EthParamBuilder addOutParams(TypeReference<?> outParams){
            outputParameters.add(outParams);
            return this;
        }


    }

}
