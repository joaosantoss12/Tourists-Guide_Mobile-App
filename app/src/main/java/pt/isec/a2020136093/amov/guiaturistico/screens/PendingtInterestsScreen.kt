package pt.isec.a2020136093.amov.guiaturistico.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.viewModel.FirebaseViewModel

@Composable
fun PendingInterestsScreen(
    viewModel: FirebaseViewModel,
    navController: NavController
) {
    viewModel.getLocaisInteresse()
    val locaisInteresse by FirebaseViewModel.locaisInteresse.observeAsState()


   Column (
         modifier = Modifier
             .fillMaxWidth()
             .padding(16.dp, 0.dp, 16.dp, 0.dp)
   ){

       Text(
           text = stringResource(R.string.title_pending_interests),
           lineHeight = 30.sp,
           textAlign = TextAlign.Center,
           fontSize = 30.sp,
           fontWeight = FontWeight.Bold,
           fontFamily = FontFamily(Font(R.font.font)),
           color = Color(42, 54, 66, 255),
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp, 0.dp, 16.dp, 20.dp)
       )
       locaisInteresse?.forEach { (firstInfo, secondInfo, thirdInfo) ->


           val (nome, descricao, imagemURL) = firstInfo
           val (categoria, classificacao, coordenadas) = secondInfo
           val (email, estado, emailsVotados) = thirdInfo

           if(estado == "pendente") {


               Card(
                   modifier = Modifier
                       .fillMaxWidth(),
                   elevation = CardDefaults.cardElevation(10.dp),
                   colors = CardDefaults.cardColors(
                       containerColor = Color.White
                   )
               ) {
                   Column(modifier = Modifier.fillMaxSize()) {
                       AsyncImage(
                           model = imagemURL,
                           error = painterResource(id = R.drawable.error),
                           contentDescription = "local image",
                           contentScale = ContentScale.Crop,
                           modifier = Modifier
                               .fillMaxWidth()
                               .heightIn(0.dp, 200.dp)
                       )
                       Text(
                           text = nome,
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(0.dp, 10.dp, 0.dp, 0.dp),
                           textAlign = TextAlign.Center,
                           fontWeight = FontWeight.Bold,
                           fontFamily = FontFamily.Serif,
                           fontSize = 18.sp,
                           color = Color.Black
                       )

                       Text(
                           text = descricao,
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(15.dp),
                           maxLines = 3,
                           fontFamily = FontFamily.Serif,
                           fontSize = 13.sp,
                           color = Color.Gray
                       )
                   }
               }
               Spacer(modifier = Modifier.height(20.dp))
           }
       }
   }
}
