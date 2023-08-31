package io.github.wangmuy.gptintentlauncher.allapps.model

class AppInfo(
    val pkgName: String,
    val title: String,
    val descriptionHtml: String,
    val summary: String,
    val genre: String,
    val familyGenre: String?,
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
        return "AppInfo(pkgName='$pkgName', title='$title', descriptionHtml='$descriptionHtml', summary='$summary', genre='$genre', familyGenre=$familyGenre)"
    }
}