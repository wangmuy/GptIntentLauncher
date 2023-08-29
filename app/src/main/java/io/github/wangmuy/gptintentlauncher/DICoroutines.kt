package io.github.wangmuy.gptintentlauncher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO = "IO"
const val DEFAULT = "Default"

val coroutinesModule = module {
    single(named(IO)) { Dispatchers.IO }
    single(named(DEFAULT)) { Dispatchers.Default }
    single(named(DEFAULT)) {
        CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(DEFAULT)))
    }
}