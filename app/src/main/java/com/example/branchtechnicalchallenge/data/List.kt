package com.example.branchtechnicalchallenge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "lists_table")
class Lists(
        @ColumnInfo(name = "title")
        var title: String,
        @ColumnInfo(name = "time_created")
        var timecreated: Long
    ) : Serializable {
    @PrimaryKey(autoGenerate = true) var uid = 0L
}