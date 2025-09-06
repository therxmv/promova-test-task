package com.therxmv.promovatest.di

import com.therxmv.base.navigation.Navigator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationModule = module {
    singleOf(::Navigator)
}