package com.zml.wallet.dao

import androidx.room.*
import com.zml.wallet.entity.TokenEntity


@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insert(tokenEntity: TokenEntity )

    @Delete
    suspend fun  delete(tokenEntity: TokenEntity )

    @Query("delete from token_entity")
    fun deleteAll()

    @Query("delete from token_entity where contract=:contract and ownerChainId=:chainId")
    fun deleteBy(contract: String,chainId: String):Int

    @Update
    suspend fun  update(tokenEntity: TokenEntity )

    @Query("select * from token_entity where ownerChainId=:chainId")
    suspend fun queryByChain(chainId:String): List<TokenEntity>

    @Query("select * from token_entity where ownerChainId=:chainId and contract=:contract")
    suspend fun queryByContractAndChain(chainId:String?,contract:String): List<TokenEntity>

}
