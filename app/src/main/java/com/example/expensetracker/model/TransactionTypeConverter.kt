package com.example.expensetracker.model

import androidx.room.TypeConverter
import java.util.Date

class TransactionTypeConverter {

    @TypeConverter
    fun fromTransactionType(type: TransactionType?): Int? {
        return type?.ordinal
    }

    @TypeConverter
    fun toTransactionType(type: Int?): TransactionType? {
        return type?.let { TransactionType.values()[it] }
    }
}

class DateConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }
}
