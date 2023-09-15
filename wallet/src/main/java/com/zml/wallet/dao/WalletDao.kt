package com.zml.wallet.dao

import androidx.room.*
import com.zml.wallet.entity.WalletEntity


@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insert(walletEntity: WalletEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insert(walletEntities: List<WalletEntity>)

    @Delete
    suspend fun  delete(walletEntity: WalletEntity)

    @Query("delete from wallet_entity where address=:address and chainName=:chainName and chainID=:chainId")
    suspend fun  deleteBy(address:String,chainName:String,chainId:String):Int

    @Update
    suspend fun  update(walletEntity: WalletEntity)

    @Query("update wallet_entity set walletName=:walletName where address=:address and chainID=:chainId")
    suspend fun  updateBy(address:String,walletName:String,chainId:String):Int

    @Query("select * from wallet_entity where address=:address and chainName=:chainName and chainID=:chainId")
    suspend fun query(address:String,chainName:String,chainId:String): List<WalletEntity>

    @Query("select * from wallet_entity")
    suspend fun  queryAll(): List<WalletEntity>

    @Query("select * from wallet_entity where chainID=:chainID")
    suspend fun  queryByChain(chainID:String): List<WalletEntity>

    @Query("select count(*) from wallet_entity")
    suspend fun  walletCount(): Int

}
