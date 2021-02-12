package com.example.branchtechnicalchallenge.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "todos_table")
class ToDo : Serializable {
    @PrimaryKey(autoGenerate = true) var uid = 0L
    @ColumnInfo(name = "title")
    var title: String
    @ColumnInfo(name = "description")
    var description: String
    @ColumnInfo(name = "createdAt")
    var createdAt: Long
    @ColumnInfo(name = "list")
    var list: Long
    @ColumnInfo(name = "completed")
    var completed: Boolean

    constructor(
        title: String,
        description: String,
        createdAt: Long,
        list: Long,
        completed: Boolean
    ) {
        this.title = title
        this.description = description
        this.createdAt = createdAt
        this.list = list
        this.completed = completed
    }

}