package com.faceapp.test.feature.faceapp.ui

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation


enum class Routes{
    WELCOME,
    FACE_GRAPH,
    FACE,
}

fun NavGraphBuilder.faceNavGraph(navController: NavHostController){
    navigation(
        startDestination = Routes.WELCOME.name,
        route = Routes.FACE_GRAPH.name
    ){
        composable(Routes.WELCOME.name) {
            WelcomeScreen(onStart = { navController.navigate(Routes.FACE.name) })
        }
        composable(Routes.FACE.name){
            FaceScreen()
        }
    }
}



