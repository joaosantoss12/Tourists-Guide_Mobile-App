package pt.isec.a2020136093.amov.guiaturistico.ui.composables

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.utils.FileUtils
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel
import java.io.File



@Composable
fun SelectImage(
    viewModel : FirebaseViewModel,
) {

    val context = LocalContext.current

    // GALERIA VARS
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri == null) {
            viewModel.imagePath.value = null
            return@rememberLauncherForActivityResult
        }
        viewModel.imagePath.value = FileUtils.createFileFromUri(context, uri)
    }

    // CAMERA VARS
    var tempFile by remember { mutableStateOf("") }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (!success) {
            viewModel.imagePath.value = null
            return@rememberLauncherForActivityResult
        }
        viewModel.imagePath.value = tempFile
    }

    Column(
        modifier = Modifier
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ) {
            Button(
                onClick = {
                    galleryLauncher.launch(PickVisualMediaRequest())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(10, 10, 150), // Cor de fundo do botão
                    contentColor = Color.White // Cor do texto do botão
                ),
                shape = RoundedCornerShape(15.dp), // Borda arredondada do botão
            ) {
                Text(text = "Galeria")

            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    tempFile = FileUtils.getTempFilename(context)
                    val fileUri = FileProvider.getUriForFile(
                        context,
                        "pt.isec.a2020136093.amov.guiaturistico.android.fileprovider",
                        File(tempFile)
                    )
                    cameraLauncher.launch(fileUri)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(10, 10, 150), // Cor de fundo do botão
                    contentColor = Color.White // Cor do texto do botão
                ),
                shape = RoundedCornerShape(15.dp), // Borda arredondada do botão
            ) {
                Text(text = "Foto")
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (viewModel.imagePath.value != null) {
                AsyncImage(
                    model = viewModel.imagePath.value,
                    contentDescription = "Image",
                    modifier = Modifier.matchParentSize()
                )
            }
            else {
                Image(
                    painter = painterResource(id = R.drawable.error),
                    contentDescription = "Default image",
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }

    Log.i("SelectGalleryImage", "imagePath: ${viewModel.imagePath.value}")

}