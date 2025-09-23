package com.faceapp.test.feature.faceapp.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.faceapp.test.feature.faceapp.ui.presentation.FaceAppViewModel
import com.faceapp.test.feature.faceapp.ui.screens.ImageAScreen
import com.faceapp.test.feature.faceapp.ui.screens.ImageBScreen
import com.faceapp.test.feature.faceapp.ui.screens.MatchScreen
import com.faceapp.test.feature.faceapp.ui.sharedViewModel
import com.faceapp.test.feature.faceapp.ui.screens.WelcomeScreen


enum class Routes{
    WELCOME,
    FACE_GRAPH,
    FACE,
    IMAGE_A,
    IMAGE_B,
    MATCH
}

fun NavGraphBuilder.faceNavGraph(
    navController: NavHostController,
){
    val graphRoute = Routes.FACE_GRAPH.name
    navigation(
        startDestination = Routes.WELCOME.name,
        route = graphRoute
    ){
        composable(Routes.WELCOME.name) { entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)
            WelcomeScreen(
                viewModel = viewModel,
                onStart = { navController.navigate(Routes.IMAGE_A.name) }
            )
        }

        composable(Routes.IMAGE_A.name){ entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)
            ImageAScreen(
                captureBitmap = viewModel::captureBitmap,
                onNext = { navController.navigate(Routes.IMAGE_B.name)}
            )
        }
        composable(Routes.IMAGE_B.name){ entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)
            ImageBScreen(
                captureBitmap = viewModel::captureBitmap,
                onNext = {navController.navigate(Routes.MATCH.name)}
            )
        }

        composable(Routes.MATCH.name){ entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)
            MatchScreen(
                onStart = viewModel::matchFaces,
                onExit = viewModel::onExit,
                onMatchAgain = {
                    viewModel.resetState()
                    navController.popBackStack(Routes.IMAGE_A.name, inclusive = true)
                },
                uri1 = viewModel.uri1!!,
                uri2 = viewModel.uri2!!,
                uiState = viewModel.uiState.collectAsState()
            )
        }

    }
}



