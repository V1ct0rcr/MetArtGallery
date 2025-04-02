package de.appsfactory.lecture.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SearchScreen(
    onItemClick: (Int) -> Unit,
    searchViewModel: SearchViewModel
) {
    var query by remember { mutableStateOf("") }
    val results by searchViewModel.results.collectAsState()
    val lastQuery by searchViewModel.lastQuery.collectAsState()
    val rawCount by searchViewModel.rawCount.collectAsState()
    val apiTotal by searchViewModel.apiTotal.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter a keyword") },
            placeholder = { Text("e.g. armor, cat, painting") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (query.isNotBlank() && query != lastQuery) {
                    searchViewModel.search(query)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }


        if (lastQuery.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Last searched: \"$lastQuery\"",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(12.dp))


        if (!isLoading && lastQuery.isNotBlank() && results.isEmpty()) {
            Text(
                text = "No results found",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }


        if (!isLoading && results.isNotEmpty()) {
            Text(
                text = "Filtered ${results.size} out of $apiTotal total",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(results) { obj ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(obj.objectID) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                if (obj.primaryImage.isNotBlank()) {
                                    AsyncImage(
                                        model = obj.primaryImage,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(80.dp)
                                            .padding(end = 16.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = obj.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "ID: ${obj.objectID}",
                                        style = MaterialTheme.typography.bodySmall
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
