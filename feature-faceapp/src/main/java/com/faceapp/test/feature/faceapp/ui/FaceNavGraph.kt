package com.faceapp.test.feature.faceapp.ui

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.faceapp.test.sharedViewModel


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

            LaunchedEffect(key1 = Unit){
                Log.i("TGB", "FaceAppViewModel instance: ${System.identityHashCode(viewModel)} uri1 y uri2: ${viewModel.uri1} ${viewModel.uri2}")
            }
            WelcomeScreen(
                viewModel = viewModel,
                onStart = { navController.navigate(Routes.IMAGE_A.name) }
            )
        }

        composable(Routes.IMAGE_A.name){entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)

            LaunchedEffect(key1 = Unit){
                Log.i("TGB", "FaceAppViewModel instance: ${System.identityHashCode(viewModel)} uri1 y uri2: ${viewModel.uri1} ${viewModel.uri2}")
            }
            ImageAScreen(
                captureBitmap = viewModel::captureBitmap,
                onNext = { navController.navigate(Routes.IMAGE_B.name)}
            )
        }
        composable(Routes.IMAGE_B.name){entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)

            LaunchedEffect(key1 = Unit){
                Log.i("TGB", "FaceAppViewModel instance: ${System.identityHashCode(viewModel)} uri1 y uri2: ${viewModel.uri1} ${viewModel.uri2}")
            }
            ImageBScreen(
                captureBitmap = viewModel::captureBitmap,
                onNext = {navController.navigate(Routes.MATCH.name)}
            )
        }

        composable(Routes.MATCH.name){entry ->
            val viewModel = entry.sharedViewModel<FaceAppViewModel>(navController = navController, graphRoute)

            LaunchedEffect(key1 = Unit){
                Log.i("TGB", "FaceAppViewModel instance: ${System.identityHashCode(viewModel)} uri1 y uri2: ${viewModel.uri1} ${viewModel.uri2}")
            }
            MatchScreen(
                onStart = viewModel::matchFaces,
                onMatchAgain = {navController.popBackStack(Routes.IMAGE_A.name, inclusive = true)},
                uri1 = viewModel.uri1!!,
                uri2 = viewModel.uri2!!,
                uiState = viewModel.uiState.collectAsState()
            )
        }

    }
}



