package de.appsfactory.lecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.appsfactory.lecture.ui.detail.DetailScreen
import de.appsfactory.lecture.ui.detail.DetailViewModel
import de.appsfactory.lecture.ui.search.SearchScreen
import de.appsfactory.lecture.ui.search.SearchViewModel
import de.appsfactory.lecture.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "search"
                ) {
                    composable("search") {
                        val searchViewModel: SearchViewModel = koinViewModel() // ✅ injecție cu Koin

                        SearchScreen(
                            searchViewModel = searchViewModel,
                            onItemClick = { objectId ->
                                navController.navigate("detail/$objectId")
                            }
                        )
                    }


                    composable("detail/{id}") { backStackEntry ->
                        val objectId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        if (objectId != null) {
                            DetailScreen(
                                objectId = objectId,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }

    }
}
