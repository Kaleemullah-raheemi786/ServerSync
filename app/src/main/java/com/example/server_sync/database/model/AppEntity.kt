package com.example.server_sync.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val packageName: String,
    val store_id: Int,
    val store_name: String,
    val vername: String,
    val vercode: Int,
    val md5sum: String,
    val apk_tags: List<String>, // needs a TypeConverter for List<String>
    val size: Long,
    val downloads: Int,
    val pdownloads: Int,
    val added: String,
    val modified: String,
    val updated: String,
    val rating: Double,
    val icon: String,
    val graphic: String?,
    val uptype: String
)

