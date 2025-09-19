package com.example.demo.network.utils

import com.example.demo.database.model.AppEntity
import com.example.demo.network.model.AppItem

/**
 * Extension function to convert an AppItem to an AppEntity.
 * This is typically used when saving API response models into the local database.
 */
fun AppItem.toEntity() = AppEntity(
    id = id,
    name = name,
    packageName = packageName,
    store_id = store_id,
    store_name = store_name,
    vername = vername,
    vercode = vercode,
    md5sum = md5sum,
    apk_tags = apk_tags,
    size = size,
    downloads = downloads,
    pdownloads = pdownloads,
    added = added,
    modified = modified,
    updated = updated,
    rating = rating,
    icon = icon,
    graphic = graphic,
    uptype = uptype
)

/**
 * Extension function to convert an AppEntity to an AppItem.
 * This is typically used when retrieving local data from the database
 * and transforming it back to the domain model used in the app UI or logic.
 */
fun AppEntity.toAppItem() = AppItem(
    id = id,
    name = name,
    packageName = packageName,
    store_id = store_id,
    store_name = store_name,
    vername = vername,
    vercode = vercode,
    md5sum = md5sum,
    apk_tags = apk_tags,
    size = size,
    downloads = downloads,
    pdownloads = pdownloads,
    added = added,
    modified = modified,
    updated = updated,
    rating = rating,
    icon = icon,
    graphic = graphic,
    uptype = uptype
)

