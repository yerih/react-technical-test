package com.faceapp.test.feature.faceapp.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.faceapp.test.core.ui.MyApplicationTheme
import com.faceapp.test.feature.faceapp.R
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun FaceScreen(
    viewModel: FaceAppViewModel = hiltViewModel()
){
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var camBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
//    var camImgUri by remember { mutableStateOf<Uri?>(null) }
    val defaultImage = painterResource(id = R.drawable.default_image)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraFile = File(context.cacheDir, "temp_image.jpg")
    val cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", cameraFile)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            coroutineScope.launch {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context).data(uri).allowHardware(false).build()
                val result = (imageLoader.execute(request) as SuccessResult).drawable
                val bitmap = (result as BitmapDrawable).bitmap
                imageBitmap = bitmap
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            coroutineScope.launch {
                val imageLoader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(cameraUri)
                    .allowHardware(false)
                    .build()
                val result = (imageLoader.execute(request) as SuccessResult).drawable
                camBitmap = (result as BitmapDrawable).bitmap
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text("Matching faces", fontSize = 40.sp)
        Text("1. Select an face image (image A) from gallery to compare it. Click the square", fontSize = 20.sp)

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            val painter = if (imageUri != null) {
                rememberAsyncImagePainter(model = imageUri)
            } else {
                defaultImage
            }

            Image(
                painter = painter,
                contentDescription = "Select a photo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Text("2. Use the camera for compare to the face image A", fontSize = 20.sp)

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable { cameraLauncher.launch(cameraUri) },
            contentAlignment = Alignment.Center
        ) {
            val painter = if (cameraUri != null) {
                rememberAsyncImagePainter(model = cameraUri)
            } else {
                defaultImage
            }

            Image(
                painter = painter,
                contentDescription = "Take a photo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        FaceScreen()
    }
}
