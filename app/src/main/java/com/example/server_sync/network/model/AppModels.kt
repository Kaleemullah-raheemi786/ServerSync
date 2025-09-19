package com.example.server_sync.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppListResponse(
    @SerialName("status")
    val status: String,
    @SerialName("responses")
    val responses: Responses
)

@Serializable
data class Responses(
    @SerialName("listApps")
    val listApps: ListApps
)

@Serializable
data class ListApps(
    @SerialName("info")
    val info: Info,
    @SerialName("datasets")
    val datasets: Datasets
)

@Serializable
data class Info(
    val status: String,
    val time: Time
)

@Serializable
data class Time(
    val seconds: Double,
    val human: String
)

@Serializable
data class Datasets(
    @SerialName("all")
    val all: All
)

@Serializable
data class All(
    @SerialName("info")
    val info: Info,
    @SerialName("data")
    val data: Data
)

@Serializable
data class Data(
    @SerialName("total")
    val total: Int,
    @SerialName("offset")
    val offset: Int,
    @SerialName("limit")
    val limit: Int,
    @SerialName("next")
    val next: Int,
    @SerialName("hidden")
    val hidden: Int,
    @SerialName("list")
    val list: List<AppItem>
)

@Serializable
data class AppItem(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("package")
    val packageName: String,
    @SerialName("store_id")
    val store_id: Int,
    @SerialName("store_name")
    val store_name: String,
    @SerialName("vername")
    val vername: String,
    @SerialName("vercode")
    val vercode: Int,
    @SerialName("md5sum")
    val md5sum: String,
    @SerialName("apk_tags")
    val apk_tags: List<String>,
    @SerialName("size")
    val size: Long,
    @SerialName("downloads")
    val downloads: Int,
    @SerialName("pdownloads")
    val pdownloads: Int,
    @SerialName("added")
    val added: String,
    @SerialName("modified")
    val modified: String,
    @SerialName("updated")
    val updated: String,
    @SerialName("rating")
    val rating: Double,
    @SerialName("icon")
    val icon: String,
    @SerialName("graphic")
    val graphic: String?,
    @SerialName("uptype")
    val uptype: String
)
