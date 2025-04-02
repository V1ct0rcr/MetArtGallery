package de.appsfactory.lecture.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    objectId: Int,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = koinViewModel()
) {
    LaunchedEffect(objectId) {
        viewModel.loadObjectDetails(objectId)
    }

    val details by viewModel.objectDetails.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        details?.let { obj ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = obj.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                if (obj.primaryImage.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(obj.primaryImage),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Department: ${obj.department}", style = MaterialTheme.typography.bodyMedium)
                    Text("Object Name: ${obj.objectName}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "Object ID: ${obj.objectID}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (obj.additionalImages.isNotEmpty()) {
                    Text("More Images", style = MaterialTheme.typography.titleMedium)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(obj.additionalImages) { url ->
                            Image(
                                painter = rememberAsyncImagePainter(url),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        } ?: run {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading or no details available")
            }
        }
    }
}
