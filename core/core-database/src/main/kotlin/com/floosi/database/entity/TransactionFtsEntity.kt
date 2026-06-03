package com.floosi.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = TransactionEntity::class)
@Entity(tableName = "transactions_fts")
data class TransactionFtsEntity(
    @ColumnInfo(name = "note") val note: String,
)
