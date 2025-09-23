@file:OptIn(ExperimentalPermissionsApi::class)

package com.faceapp.test.feature.faceapp.ui

import com.faceapp.test.core.ui.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faceapp.test.core.ui.R
import com.faceapp.test.core.ui.permissions.PermissionRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun WelcomeScreen(
    viewModel: FaceAppViewModel = hiltViewModel(),
    onStart: () -> Unit,
) {
    WelcomeScreen(
        onStart = onStart,
        uiState = viewModel.uiState.collectAsState()
    )
}

@Composable
internal fun WelcomeScreen(
    onStart: () -> Unit = {},
    uiState: State<UiState> = mutableStateOf(UiState()),
//    uiState: StateFlow<UiState> = MutableStateFlow(UiState()),
    permissionRequester: PermissionRequester = PermissionRequester(),
) {
    val context = LocalContext.current
    val permissionState = permissionRequester.checkPermissions()



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "FaceApp Test",
            style = TextStyle(fontSize = 40.sp)
        )
        Text(
            text = "by Yerih Iturriago",
            style = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if(uiState.value.isLoading) return@Button
                if(!permissionState.allPermissionsGranted)permissionState.launchMultiplePermissionRequest()
                else onStart()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if(uiState.value.isLoading)
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
            else
                Text(text = context.getString(R.string.start))
        }

        Spacer(modifier = Modifier.height(80.dp))
        if(uiState.value.error != null)
            Text(text = uiState.value.error!!.message.toString(), style = TextStyle(color = Color.Red))

    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        WelcomeScreen(onStart = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        WelcomeScreen()
    }
}
