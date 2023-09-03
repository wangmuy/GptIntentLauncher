package io.github.wangmuy.gptintentlauncher.allapps.model

/** app info from store */
class PackageStoreInfo(
    val pkgName: String,
    val title: String,
    val descriptionHtml: String,
    val summary: String,
    val genre: String,
    val familyGenre: String? = null,
//    val androidVersion: String?,
//    val androidVersionText: String?,
//    val genreId: String,
//    val familyGenreId: String?,
//    val contentRating: String,
//    val contentRatingDescription: String,
//    val appVersion: String,
//    val lastUpdateChangelog: String,
) {
    override fun toString(): String {
        return "PackageStoreInfo(pkgName='$pkgName', title='$title', descriptionHtml='$descriptionHtml', summary='$summary', genre='$genre', familyGenre=$familyGenre)"
    }
}