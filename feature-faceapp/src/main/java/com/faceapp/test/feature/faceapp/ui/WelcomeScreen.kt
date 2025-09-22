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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faceapp.test.core.ui.R
import com.faceapp.test.core.ui.permissions.PermissionRequester
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun WelcomeScreen(
    viewModel: FaceAppViewModel = hiltViewModel(),
) {
    WelcomeScreen(
        onStart = viewModel::onStart
    )
}

@Composable
internal fun WelcomeScreen(
    onStart: () -> Unit = {},
    permissionRequester: PermissionRequester = PermissionRequester()
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
                if(!permissionState.allPermissionsGranted)permissionState.launchMultiplePermissionRequest()
                else onStart()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = context.getString(R.string.start))
        }

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
