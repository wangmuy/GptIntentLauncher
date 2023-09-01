package io.github.wangmuy.gptintentlauncher.allapps.source

import io.github.wangmuy.gptintentlauncher.allapps.model.PackageStoreInfo

fun PackageStoreInfo.toLocal() = LocalPackageStoreInfo(
    packageName = pkgName,
    title = title,
    summary = summary,
    description = descriptionHtml,
    genre = genre,
    familyGenre = familyGenre ?: ""
)

fun List<PackageStoreInfo>.toLocal() = map(PackageStoreInfo::toLocal)

fun LocalPackageStoreInfo.toExternal() = PackageStoreInfo(
    pkgName = packageName,
    title = title,
    summary = summary,
    descriptionHtml = description,
    genre = genre,
    familyGenre = familyGenre
)

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("localToExternal")
fun List<LocalPackageStoreInfo>.toExternal() = map(LocalPackageStoreInfo::toExternal)