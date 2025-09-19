package com.example.server_sync.network.utils

import androidx.room.TypeConverter

/**
 * Type converters to allow Room to handle List<String> types.
 */
class Converters {

    /**
     * Converts a comma-separated [String] into a [List] of [String].
     *
     * @param value The comma-separated string.
     * @return A list of strings, or an empty list if the input is empty.
     */
    @TypeConverter
    fun fromString(value: String): List<String> {
        return if (value.isEmpty()) emptyList() else value.split(",")
    }

    /**
     * Converts a [List] of [String] into a single comma-separated [String].
     *
     * @param list The list of strings to convert.
     * @return A single comma-separated string.
     */
    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}

