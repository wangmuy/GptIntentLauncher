package io.github.wangmuy.gptintentlauncher.allapps.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageStoreInfoDao {
    @Query("SELECT * FROM packageStoreInfo")
    fun observeAll(): Flow<List<LocalPackageStoreInfo>>

    @Query("SELECT * FROM packageStoreInfo")
    suspend fun getAll(): List<LocalPackageStoreInfo>

    @Query("SELECT * FROM packageStoreInfo WHERE packageName=:pkgName")
    suspend fun get(pkgName: String): LocalPackageStoreInfo?

    @Insert
    suspend fun insert(info: LocalPackageStoreInfo)
}