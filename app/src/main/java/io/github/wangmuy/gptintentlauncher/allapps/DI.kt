package io.github.wangmuy.gptintentlauncher.allapps

import io.github.wangmuy.gptintentlauncher.DEFAULT
import io.github.wangmuy.gptintentlauncher.allapps.service.AppStoreService
import io.github.wangmuy.gptintentlauncher.allapps.service.GooglePlayStoreService
import io.github.wangmuy.gptintentlauncher.allapps.source.AllAppsRepository
import io.github.wangmuy.gptintentlauncher.allapps.source.AppsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val allAppsModule = module {
    single<AppsRepository> { AllAppsRepository(get(), get(named(DEFAULT)), get(named(DEFAULT))) }
    single<AppStoreService> { GooglePlayStoreService(get(named(DEFAULT))) }
    factory { AllAppsScreenViewModel(get(), get()) }
}