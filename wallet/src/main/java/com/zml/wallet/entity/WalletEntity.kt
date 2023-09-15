package com.zml.wallet.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "wallet_entity")
data class WalletEntity(
    @PrimaryKey(autoGenerate = true) val id:Long?,
    val chainName:String?,
    val chainID:String?,
    val address:String,
    val walletKey:String,
    val passWord:String,
    val mode:Int = 0,
    val walletName:String = "unknown",
)
