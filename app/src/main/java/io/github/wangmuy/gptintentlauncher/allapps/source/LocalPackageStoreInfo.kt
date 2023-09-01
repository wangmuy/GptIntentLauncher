package io.github.wangmuy.gptintentlauncher.allapps.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "packageStoreInfo",
    indices = [Index(value = ["packageName"], unique = true)]
)
data class LocalPackageStoreInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0,

    @ColumnInfo(name = "packageName")
    var packageName: String = "",

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "summary")
    var summary: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "genre")
    var genre: String = "",

    @ColumnInfo(name = "familyGenre")
    var familyGenre: String = ""
)