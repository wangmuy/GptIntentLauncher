package io.github.wangmuy.gptintentlauncher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val DISPATCHER_IO = "IO"
const val DISPATCHER_DEFAULT = "Default"

val coroutinesModule = module {
    single(named(DISPATCHER_IO)) { Dispatchers.IO }
    single(named(DISPATCHER_DEFAULT)) { Dispatchers.Default }
    single { (dispatcher: CoroutineDispatcher) ->
        CoroutineScope(SupervisorJob() + dispatcher) }
}