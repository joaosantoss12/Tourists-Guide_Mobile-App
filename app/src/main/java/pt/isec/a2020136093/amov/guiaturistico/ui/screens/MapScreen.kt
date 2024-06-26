package pt.isec.a2020136093.amov.guiaturistico.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import pt.isec.a2020136093.amov.guiaturistico.R
import pt.isec.a2020136093.amov.guiaturistico.viewModel.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: LocationViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight

        //var autoEnabled by remember { mutableStateOf(false) }
        val location = LocationViewModel.currentLocation.observeAsState()

        var geoPoint by remember {
            mutableStateOf(
                GeoPoint(
                    location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
                )
            )
        }

        /*if (autoEnabled)
            LaunchedEffect(key1 = location.value) {
                geoPoint = GeoPoint(
                    location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
                )
            }*/


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(if (!isLandscape) 8.dp else 0.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Lat: ${location.value?.latitude ?: "--"}")

                /*Switch(checked = autoEnabled, onCheckedChange = {
                    autoEnabled = it
                })*/

                Text(text = "Lon: ${location.value?.longitude ?: "--"}")
            }

            Spacer(Modifier.height(if (!isLandscape) 16.dp else 6.dp))

            if(isLandscape){
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight()
                        .clipToBounds()
                        .background(Color(255, 240, 128)),
                ) {

                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                setTileSource(TileSourceFactory.MAPNIK);//==TileSourceFactory.DEFAULT_TILE_SOURCE
                                setMultiTouchControls(true)
                                controller.setCenter(geoPoint)
                                controller.setZoom(18.0)
                                if (LocationViewModel.showLocations.value == true) {
                                    for (poi in viewModel.POIs_localizacoes)
                                        overlays.add(
                                            Marker(this).apply {
                                                position = GeoPoint(poi.latitude, poi.longitude)
                                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                title = poi.team
                                            }
                                        )
                                } else {
                                    for (poi in viewModel.POIs_locaisInteresse)
                                        overlays.add(
                                            Marker(this).apply {
                                                position = GeoPoint(poi.latitude, poi.longitude)
                                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                title = poi.team
                                            }
                                        )
                                }

                            }
                        },
                        update = { view ->
                            view.controller.setCenter(geoPoint)
                        }
                    )
                }

                Spacer(Modifier.width(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (LocationViewModel.showLocations.value == true) {
                        items(viewModel.POIs_localizacoes) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(128, 192, 255)
                                ),
                                onClick = {
                                    geoPoint = GeoPoint(it.latitude, it.longitude)
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = it.team, fontSize = 20.sp)
                                    Text(text = "Latitude: ${it.latitude}", fontSize = 14.sp)
                                    Text(text = "Longitude: ${it.longitude}", fontSize = 14.sp)
                                    Text(
                                        text = "(Obtenção coordenadas: ${it.metodo})",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else { //locais de interesse
                        items(viewModel.POIs_locaisInteresse) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(128, 192, 255)
                                ),
                                onClick = {
                                    geoPoint = GeoPoint(it.latitude, it.longitude)
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = it.team, fontSize = 20.sp)
                                    Text(text = "Latitude: ${it.latitude}", fontSize = 14.sp)
                                    Text(text = "Longitude: ${it.longitude}", fontSize = 14.sp)
                                    Text(
                                        text = "(Obtenção coordenadas: ${it.metodo})",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                    }
                }
            }
            else{
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f)
                        .clipToBounds()
                        .background(Color(255, 240, 128)),
                ) {

                    AndroidView(
                        factory = { context ->
                            MapView(context).apply {
                                setTileSource(TileSourceFactory.MAPNIK);//==TileSourceFactory.DEFAULT_TILE_SOURCE
                                setMultiTouchControls(true)
                                controller.setCenter(geoPoint)
                                controller.setZoom(18.0)
                                if (LocationViewModel.showLocations.value == true) {
                                    for (poi in viewModel.POIs_localizacoes)
                                        overlays.add(
                                            Marker(this).apply {
                                                position = GeoPoint(poi.latitude, poi.longitude)
                                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                title = poi.team
                                            }
                                        )
                                } else {
                                    for (poi in viewModel.POIs_locaisInteresse)
                                        overlays.add(
                                            Marker(this).apply {
                                                position = GeoPoint(poi.latitude, poi.longitude)
                                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                title = poi.team
                                            }
                                        )
                                }

                            }
                        },
                        update = { view ->
                            view.controller.setCenter(geoPoint)
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (LocationViewModel.showLocations.value == true) {
                        items(viewModel.POIs_localizacoes) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(128, 192, 255)
                                ),
                                onClick = {
                                    geoPoint = GeoPoint(it.latitude, it.longitude)
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = it.team, fontSize = 20.sp)
                                    Text(text = "Latitude: ${it.latitude}", fontSize = 14.sp)
                                    Text(text = "Longitude: ${it.longitude}", fontSize = 14.sp)

                                    Text(
                                        text = "(Obtenção coordenadas: ${it.metodo})",
                                        fontSize = 14.sp
                                    )
                                    if(it.metodo == "automático"){
                                        Text(
                                            text = "["+stringResource(R.string.high_accuracy)+"]",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    } else { //locais de interesse
                        items(viewModel.POIs_locaisInteresse) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(128, 192, 255)
                                ),
                                onClick = {
                                    geoPoint = GeoPoint(it.latitude, it.longitude)
                                }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = it.team, fontSize = 20.sp)
                                    Text(text = "Latitude: ${it.latitude}", fontSize = 14.sp)
                                    Text(text = "Longitude: ${it.longitude}", fontSize = 14.sp)

                                    Text(
                                        text = "(Obtenção coordenadas: ${it.metodo})",
                                        fontSize = 14.sp
                                    )
                                    if(it.metodo == "automático"){
                                        Text(
                                            text = "["+stringResource(R.string.high_accuracy)+"]",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}