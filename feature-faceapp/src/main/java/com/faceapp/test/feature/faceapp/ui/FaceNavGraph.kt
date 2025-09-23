package com.faceapp.test.feature.faceapp.ui

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


enum class Routes{
    WELCOME,
    FACE_GRAPH,
    FACE,
    IMAGE_A,
    IMAGE_B,
    MATCH
}

fun NavGraphBuilder.faceNavGraph(navController: NavHostController){
    navigation(
        startDestination = Routes.WELCOME.name,
        route = Routes.FACE_GRAPH.name
    ){
        composable(Routes.WELCOME.name) {
            val viewModel = hiltViewModel<FaceAppViewModel>()
            WelcomeScreen(onStart = { navController.navigate(Routes.IMAGE_A.name) })
        }

        composable(Routes.IMAGE_A.name){
            val viewModel = hiltViewModel<FaceAppViewModel>()
            ImageAScreen(
                captureBitmap = viewModel::captureBitmap,
                onNext = { navController.navigate(Routes.IMAGE_B.name)}
            )
        }
        composable(Routes.IMAGE_B.name){
            val viewModel = hiltViewModel<FaceAppViewModel>()
//            ImageBScreen(onNext = {navController.navigate(Routes.MATCH.name)})
        }

    }
}



