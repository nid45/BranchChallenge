package com.example.branchtechnicalchallenge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "lists_table")
class Lists : Serializable {
    @PrimaryKey(autoGenerate = true) var uid = 0L
    @ColumnInfo(name = "title")
    var title: String

    constructor(title: String) {
        this.title = title
    }

}