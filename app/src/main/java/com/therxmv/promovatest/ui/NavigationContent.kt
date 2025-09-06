package com.therxmv.promovatest.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.therxmv.base.navigation.Destination
import com.therxmv.base.navigation.Navigator
import com.therxmv.featuremovies.ui.content.MoviesScreen
import org.koin.compose.koinInject

@Composable
fun NavigationContent(
    navigator: Navigator = koinInject(),
) {
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Destination.Movies> {
                MoviesScreen()
            }
        },
    )
}