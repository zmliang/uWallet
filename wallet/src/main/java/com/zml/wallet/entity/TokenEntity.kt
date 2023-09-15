package com.zml.wallet.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.web3j.protocol.core.methods.response.EthChainId


@Entity(tableName = "token_entity")
data class TokenEntity(
    @PrimaryKey
    var contract:String,
    var ownerChainId:String?,
    var symbol:String?,
    var decimal:String?,
    var name:String?,
)
