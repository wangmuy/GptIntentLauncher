/*
 * Copyright 2021 Arthur Ivanets, arthur.ivanets.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arthurivanets.googleplayscraper

import com.arthurivanets.googleplayscraper.specs.*
import com.arthurivanets.googleplayscraper.util.path

internal object Specs {

    val CLUSTER_URL = path("ds:6", 1, 1, 0, 21, 1, 2, 4, 2)

    val APPS_INITIAL_RESPONSE = AppsResponseSpec(
        apps = path("ds:3", 0, 1, 0, 21, 0),
        token = path("ds:3", 0, 1, 0, 21, 1, 3, 1),
    )

    val APP_SEARCH_INITIAL_RESPONSE = AppSearchResponseSpec(
        featuredAppsSectionIndicator = path("ds:4", 0, 1, 0, 23, 16, 11, 0, 0),
        noResultsSectionIndicator = path("ds:4", 0, 1, 0, 25, 0, 0, 0, 1),
        featuredApp = path("ds:4", 0, 1, 0, 23),
        apps = path("ds:4", 0, 1, 0, 22, 0),
        moreApps = path("ds:4", 0, 1, 2, 22, 0),
        moreResultsToken = path("ds:4", 0, 1, 0, 22, 1, 3, 1),
    )

    val APPS_DEV_ID_NAN_INITIAL_RESPONSE = AppsResponseSpec(
        apps = path("ds:3", 0, 1, 0, 22, 0),
        token = path("ds:3", 0, 1, 0, 22, 1, 3, 1),
    )

    val APPS_RESPONSE = AppsResponseSpec(
        apps = path(0, 2, 0, 0, 0),
        token = path(0, 2, 0, 0, 7, 1)
    )

    val APPS_LISTING_RESPONSE = AppsResponseSpec(
        apps = path(0, 2, 0, 1, 0, 28, 0),
        token = path(0, 2, 0, 1, 0, 0),
    )

    val APP_INITIAL_REQUEST = AppSpec(
        appId = path(0, 0),
        title = path(3),
        summary = path(13, 1),
        score = path(4, 1),
        scoreText = path(4, 0),
        url = path(10, 4, 2),
        iconUrl = path(1, 3, 2),
        developer = path(14),
        priceText = path(8, 1, 0, 2),
        price = path(8, 1, 0, 0),
        currency = path(8, 1, 0, 1),
    )

    val APP_DEV_ID_NAN = AppSpec(
        appId = path(0, 0, 0),
        title = path(0, 3),
        summary = path(0, 13, 1),
        score = path(0, 4, 1),
        scoreText = path(0, 4, 0),
        url = path(0, 10, 4, 2),
        iconUrl = path(0, 1, 3, 2),
        developer = path(0, 14),
        priceText = path(0, 8, 1, 0, 2),
        price = path(0, 8, 1, 0, 0),
        currency = path(0, 8, 1, 0, 1),
    )

    val APP_SEARCH = AppSpec(
        appId = path(0, 0, 0),
        title = path(0, 3),
        summary = path(0, 13, 1),
        score = path(0, 4, 1),
        scoreText = path(0, 4, 0),
        url = path(0, 10, 4, 2),
        iconUrl = path(0, 1, 3, 2),
        developer = path(0, 14),
        priceText = path(0, 8, 1, 0, 2),
        price = path(0, 8, 1, 0, 0),
        currency = path(0, 8, 1, 0, 1),
    )

    val APP_SEARCH_FEATURED = AppSpec(
        appId = path(16, 11, 0, 0),
        title = path(16, 2, 0, 0),
        summary = path(16, 2, 73, 0, 1),
        score = path(16, 2, 51, 0, 1),
        scoreText = path(16, 2, 51, 0, 0),
        url = path(17, 0, 0, 4, 2),
        iconUrl = path(16, 2, 95, 0, 3, 2),
        developer = path(16, 2, 68, 0),
        priceText = path(16, 2, 57, 0, 0, 0, 0, 1, 0, 2),
        price = path(16, 2, 57, 0, 0, 0, 0, 1, 0, 0),
        currency = path(16, 2, 57, 0, 0, 0, 0, 1, 0, 1),
    )

    val APP = AppSpec(
        appId = path(12, 0),
        title = path(2),
        summary = path(4, 1, 1, 1, 1),
        score = path(6, 0, 2, 1, 1),
        scoreText = path(6, 0, 2, 1, 0),
        url = path(9, 4, 2),
        iconUrl = path(1, 1, 0, 3, 2),
        developer = path(4, 0, 0, 0),
        priceText = path(7, 0, 3, 2, 1, 0, 2),
        price = path(7, 0, 3, 2, 1, 0, 0),
        currency = path(7, 0, 3, 2, 1, 0, 1),
    )

    val APP_LISTING = AppSpec(
        appId = path(0, 0, 0),
        title = path(0, 3),
        summary = path(0, 13, 1),
        score = path(0, 4, 1),
        scoreText = path(0, 4, 0),
        url = path(0, 10, 4, 2),
        iconUrl = path(0, 1, 3, 2),
        developer = path(0, 14),
        priceText = path(0, 8, 1, 0, 2),
        price = path(0, 8, 1, 0, 0),
        currency = path(0, 8, 1, 0, 1),
    )

    val APP_DETAILS = AppDetailsSpec(
        title = path("ds:5", 1, 2, 0, 0),
        description = path("ds:5", 1, 2, 72, 0, 1),
        summary = path("ds:5", 1, 2, 73, 0, 1),
        installs = path("ds:5", 1, 2, 13, 0),
        minInstalls = path("ds:5", 1, 2, 13, 1),
        maxInstalls = path("ds:5", 1, 2, 13, 2),
        score = path("ds:5", 1, 2, 51, 0, 1),
        scoreText = path("ds:5", 1, 2, 51, 0, 0),
        ratingCount = path("ds:5", 1, 2, 51, 2, 1),
        reviewCount = path("ds:5", 1, 2, 51, 3, 1),
        ratingsHistogram = path("ds:5", 1, 2, 51, 1),
        priceText = path("ds:5", 1, 2, 57, 0, 0, 0, 0, 1, 0, 2),
        price = path("ds:5", 1, 2, 57, 0, 0, 0, 0, 1, 0, 0),
        currency = path("ds:5", 1, 2, 57, 0, 0, 0, 0, 1, 0, 1),
        isAvailable = path("ds:5", 1, 2, 18, 0),
        offersInAppPurchases = path("ds:5", 1, 2, 19, 0),
        inAppPurchasesPriceRange = path("ds:5", 1, 2, 19, 0),
        appSize = path("ds:8", 0),
        androidVersion = path("ds:5", 1, 2, 140, 1, 1, 0, 0, 1),
        androidMinSdkVersion = path("ds:5", 1, 2, 140, 1, 1, 0, 0, 0),
        androidTargetSdkVersion = path("ds:5", 1, 2, 140, 1, 0, 0, 0),
        developer = path("ds:5", 1, 2, 68, 0),
        developerId = path("ds:5", 1, 2, 68, 1, 4, 2),
        developerInternalId = path("ds:5", 1, 2, 68, 1, 4, 2),
        developerEmail = path("ds:5", 1, 2, 69, 1, 0),
        developerWebsite = path("ds:5", 1, 2, 69, 0, 5, 2),
        developerAddress = path("ds:5", 1, 2, 69, 2, 0),
        privacyPolicyUrl = path("ds:5", 1, 2, 99, 0, 5, 2),
        genre = path("ds:5", 1, 2, 79, 0, 0, 0),
        genreId = path("ds:5", 1, 2, 79, 0, 0, 2),
        familyGenre = path("ds:5", 0, 12, 13, 1, 0),
        familyGenreId = path("ds:5", 0, 12, 13, 1, 2),
        iconUrl = path("ds:5", 1, 2, 95, 0, 3, 2),
        headerImageUrl = path("ds:5", 1, 2, 96, 0, 3, 2),
        screenshotsUrlsContainer = path("ds:5", 1, 2, 78, 0),
        _screenshotUrl = path(3, 2),
        videoUrl = path("ds:5", 1, 2, 100, 0, 0, 3, 2),
        videoThumbnailUrl = path("ds:5", 1, 2, 100, 1, 0, 3, 2),
        contentRating = path("ds:5", 1, 2, 9, 0),
        contentRatingDescription = path("ds:5", 1, 2, 9, 2, 1),
        containsAds = path("ds:5", 1, 2, 48),
        releaseDate = path("ds:5", 1, 2, 10, 0),
        lastUpdateTimestamp = path("ds:5", 1, 2, 145, 0, 1, 0),
        appVersion = path("ds:5", 1, 2, 140, 0, 0, 0),
        lastUpdateChangelog = path("ds:5", 1, 2, 144, 1, 1),
        isEditorsChoice = path("ds:5", 0, 12, 15, 0),
    )

    val APP_REVIEWS_RESPONSE = AppReviewsResponseSpec(
        reviews = path(0, 2, 0),
        token = path(0, 2, 1, 1)
    )

    val APP_REVIEW = AppReviewSpec(
        id = path(0),
        authorUsername = path(1, 0),
        authorImageUrl = path(1, 1, 3, 2),
        timestamp = path(5),
        score = path(2),
        title = path(0),
        text = path(4),
        replyTimestamp = path(7, 2),
        replyText = path(7, 1),
        appVersion = path(10),
        thumbsUpCount = path(6),
        criteria = path(12, 0),
    )

    val PERMISSIONS_RESPONSE = PermissionsResponseSpec(
        categorized = PermissionsSectionSpec(
            section = path(0, 2, 0),
            category = PermissionsCategorySpec(
                categoryType = path(0),
                permissions = path(2),
                permissionDescription = path(1)
            )
        ),
        other = PermissionsSectionSpec(
            section = path(0, 2, 1),
            category = PermissionsCategorySpec(
                categoryType = path(0),
                permissions = path(2),
                permissionDescription = path(1)
            )
        ),
        leftover = PermissionsCategorySpec(
            permissions = path(0, 2, 2),
            permissionDescription = path(1)
        )
    )

}