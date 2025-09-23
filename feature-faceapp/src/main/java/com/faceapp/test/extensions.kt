package com.faceapp.test

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController


fun Double.toPercentage() = "${String.format("%.2f", this * 100)}%"

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    graphRoute: String
): T {
    val parentEntry = remember(this){
        navController.getBackStackEntry(graphRoute)
    }
    return hiltViewModel(parentEntry)
}

fun Any.log(msg: String, tag: String = "TGB", ) = Log.i(tag, msg)


