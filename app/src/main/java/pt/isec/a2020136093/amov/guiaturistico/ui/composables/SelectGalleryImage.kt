package pt.isec.a2020136093.amov.guiaturistico.ui.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.utils.FileUtils
import java.io.File


@Composable
fun SelectGalleryImage(
    imagePath: MutableState<String?>,
    modifier: Modifier = Modifier
) {
    var imagePath_aux1 = imagePath.value
    var imagePath_aux2 = imagePath.value

    val context = LocalContext.current

    // GALERIA VARS
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) {
            imagePath.value = null
            return@rememberLauncherForActivityResult
        }
        imagePath_aux1 = FileUtils.createFileFromUri(context, uri)
    }

    // CAMERA VARS
    var tempFile by remember { mutableStateOf("") }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) {
            imagePath.value = null
            return@rememberLauncherForActivityResult
        }
        imagePath_aux2 = tempFile
    }

    Row() {
        Button(onClick = {
            imagePath.value = imagePath_aux1

            galleryLauncher.launch(PickVisualMediaRequest())
        }) {
            Text(text = "Galeria")

        }
        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = {
            imagePath.value = tempFile

            tempFile = FileUtils.getTempFilename(context)
            val fileUri = FileProvider.getUriForFile(
                context,
                "pt.isec.ans.p02compose.android.fileprovider",
                File(tempFile)
            )
            cameraLauncher.launch(fileUri)
        }) {
            Text(text = "Foto")
        }
    }


    Spacer(modifier = Modifier.height(8.dp))

    Box(modifier = Modifier.fillMaxSize()) {
        if (imagePath.value != null) {
            AsyncImage(
                model = imagePath.value,
                contentDescription = "Image",
                modifier = Modifier.matchParentSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = "Default image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}