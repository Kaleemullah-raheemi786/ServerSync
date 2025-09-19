package com.example.demo.dummy

import com.example.demo.database.model.AppEntity

val dummyCachedApps = listOf(
    AppEntity(
        id = 1L,
        name = "App 1",
        packageName = "com.example.app1",
        store_id = 1,
        store_name = "MainStore",
        vername = "1.0",
        vercode = 1,
        md5sum = "abc123",
        apk_tags = emptyList(),
        size = 1024L,
        downloads = 100,
        pdownloads = 500,
        added = "1620000000L",
        modified = "1620000001L",
        updated = "1620000002L",
        rating = 4.5,
        icon = "https://example.com/icon1.png",
        graphic = null,
        uptype = "normal"
    ),
    AppEntity(
        id = 2L,
        name = "App 2",
        packageName = "com.example.app2",
        store_id = 2,
        store_name = "AltStore",
        vername = "2.0",
        vercode = 2,
        md5sum = "def456",
        apk_tags = emptyList(),
        size = 2048L,
        downloads = 100,
        pdownloads = 500,
        added = "1620000000L",
        modified = "1620000001L",
        updated = "1620000002L",
        rating = 4.7,
        icon = "https://example.com/icon2.png",
        graphic = null,
        uptype = "beta"
    )
)
