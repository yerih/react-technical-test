package com.faceapp.test.feature.faceapp.ui.screens

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.faceapp.test.core.ui.composables.AutoCircularProgress
import com.faceapp.test.feature.faceapp.ui.presentation.UiState


@Composable
fun MatchScreen(
    onStart: ()-> Unit = {},
    onMatchAgain: ()->Unit = {},
    uri1: Uri,
    uri2: Uri,
    uiState: State<UiState> = mutableStateOf(UiState()),
){
    val context = LocalContext.current
    var showBtns by remember{ mutableStateOf(false) }
    LaunchedEffect(key1 = Unit){ onStart() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Match result", fontSize = 40.sp)
        Text("Images to match:", fontSize = 20.sp)

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ){

            Image(
                painter = rememberAsyncImagePainter(model = uri1),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = rememberAsyncImagePainter(model = uri2),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        uiState.value.error?.let {
            Text("Error: ${it.message}", style = TextStyle(fontSize = 16.sp, color = Color.Red))
            showBtns = true
        }?: uiState.value.resultModel?.let{result ->

            Text("Results:", style = TextStyle(fontSize = 25.sp, color = Color.Green))
            Text("Similarity: ${result.similarity}", style = TextStyle(fontSize = 20.sp, color = Color.Green))
            Text("Detections: ${result.detections}", style = TextStyle(fontSize = 20.sp, color = Color.Green))
            showBtns = true
        }?:let {
            AutoCircularProgress()
            Text("Verifying...")
        }

        if(showBtns){
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onMatchAgain,
            ) {
                Text("Match again")
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { (context as ComponentActivity).finish() },
            ) {
                Text("Exit")
            }
        }

    }
}



