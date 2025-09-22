/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faceapp.test.core.ui.R

@Composable
fun WelcomeScreen(viewModel: FaceAppViewModel = hiltViewModel()) {
    WelcomeScreen(
        onContinue = { viewModel.launchPermissionRequest() },
    )
}

@Composable
internal fun WelcomeScreen(
    onContinue: () -> Unit = {},
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(20.dp)
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
            onClick = onContinue,
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
        WelcomeScreen(onContinue = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        WelcomeScreen()
    }
}
