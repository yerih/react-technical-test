package com.faceapp.test.feature.faceapp.ui.screens


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.faceapp.test.feature.faceapp.R
import com.faceapp.test.core.data.domain.saveTempBitmap
import com.faceapp.test.feature.faceapp.ui.log
import com.regula.facesdk.FaceSDK
import com.regula.facesdk.configuration.FaceCaptureConfiguration


@Composable
fun ImageBScreen(
    onNext: ()->Unit = {},
    captureBitmap: (Uri, Boolean) -> Unit = {_,_->},
){
    val context = LocalContext.current
    val defaultImage = painterResource(id = R.drawable.default_image)
    var cameraUri by remember{ mutableStateOf<Uri?>(null) }
    var isBtnEnabled by remember{ mutableStateOf(false) }
    var isCapturing by remember{ mutableStateOf(true) }
    var error by remember{ mutableStateOf<String?>(null)}


    LaunchedEffect(key1 = cameraUri){ isBtnEnabled = (cameraUri != null) }


    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Matching faces", fontSize = 40.sp)
        Text("2. Use the camera for compare to the face image A. Click the circle to launch camera.", fontSize = 20.sp)

        Box(
            modifier = Modifier.size(200.dp).clip(CircleShape).border(1.dp, Color.Gray, CircleShape).clickable {
                launchCamera(context, onError = { error = it }){ bitmap ->
                    val uri = saveTempBitmap(context, bitmap, "img2").uri.also{ cameraUri = it}
                    captureBitmap(uri, false)
                    isCapturing = false
                } },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = cameraUri?.run{ rememberAsyncImagePainter(model = cameraUri) } ?: defaultImage,
                contentDescription = "Take a photo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        error?.let { Text("Error: $it. Try again", style = TextStyle(color = Color.Red)) }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            enabled = isBtnEnabled,
            modifier = Modifier.fillMaxWidth(),
            onClick = { cameraUri?.let { onNext() } },
        ) {
            Text("Match")
        }
    }
}


fun launchCamera(
    context: Context,
    onError: (String)->Unit = {},
    onGetImage: (Bitmap)->Unit = {_->},
){

    val configuration = FaceCaptureConfiguration.Builder()
        .setCameraId(0)
        .setCameraSwitchEnabled(true)
        .build()

    FaceSDK.Instance().presentFaceCaptureActivity(context as ComponentActivity, configuration) { response ->
        response.log("response.image = ${response?.image}, excepcion = ${response.exception?.message}")
        response.image?.let {
            onGetImage(it.bitmap)
        }?:response.exception?.let{
            onError(it.message.toString())
        }
    }
}





