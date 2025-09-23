package com.faceapp.test.feature.faceapp.ui.screens

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.faceapp.test.feature.faceapp.R
import com.faceapp.test.core.data.domain.saveTempBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun ImageAScreen(
    onNext: ()->Unit = {},
    captureBitmap: (Uri, Boolean) -> Unit = {_,_->},
){

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val defaultImage = painterResource(id = R.drawable.default_image)
    var isBtnEnabled by remember{ mutableStateOf(false) }


    LaunchedEffect(key1 = imageUri){ isBtnEnabled = (imageUri != null) }

    val imagePickerLauncher = ImagePickerLauncher(context = context, scope = coroutineScope){ uri ->
        imageUri = uri
        captureBitmap(uri, true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Matching faces", fontSize = 40.sp)
        Text(
            "1. Select an face image (image A) from gallery to compare it. Click in the circle",
            fontSize = 20.sp
        )

        Box(
            modifier = Modifier.size(200.dp).clip(CircleShape).border(1.dp, Color.Gray, CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = if (imageUri != null) rememberAsyncImagePainter(model = imageUri) else defaultImage,
                contentDescription = "Select a photo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }


        Spacer(modifier = Modifier.height(20.dp))
        Button(
            enabled = isBtnEnabled,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onNext()
            },
        ) {
            Text("Next")
        }
    }
}


@Composable
fun ImagePickerLauncher(
    context: Context,
    scope: CoroutineScope,
    onGetUri: (Uri)-> Unit
): ManagedActivityResultLauncher<String, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context).data(uri).allowHardware(false).build()
                val result = (imageLoader.execute(request) as SuccessResult).drawable
                val bitmap = (result as BitmapDrawable).bitmap
                saveTempBitmap(context, bitmap, "img1")
                onGetUri(uri)
            }
        }
    }
}


