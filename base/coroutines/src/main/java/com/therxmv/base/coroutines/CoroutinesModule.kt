package com.therxmv.base.coroutines

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutinesModule = module {
    single(named(KoinDispatchers.IO)) { Dispatchers.IO }
    single(named(KoinDispatchers.Main)) { Dispatchers.Main }
    single(named(KoinDispatchers.Default)) { Dispatchers.Default }
}

enum class KoinDispatchers {
    Main,
    Default,
    IO,
}