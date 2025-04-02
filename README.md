# Mobile Computing - Networking

This repository contains the code snippets and samples for the Repository Pattern & Dependency Injection of the lecture.

Instructions during Seminar:

## Repository Pattern

### 1. Create Data Source as `/network/RemotePostsDataSource.kt`

```
interface RemotePostsDataSource {
    suspend fun getPosts(): List<ApiPost>
    suspend fun getComments(postId: Long): List<ApiComment>
}

class RemotePostsDataSourceImpl : RemotePostsDataSource {

    private val api: JsonPlaceholderApi = jsonPlaceholderApi

    override suspend fun getPosts(): List<ApiPost> {
        val response = api.getPosts()
        val responseBody = response.body()

        val posts = if (response.isSuccessful && responseBody != null) {
            responseBody
        } else {
            emptyList()
        }

        return posts
    }

    override suspend fun getComments(postId: Long): List<ApiComment> {
        val response = api.getComments(postId = postId)
        val responseBody = response.body()

        val comments = if (response.isSuccessful && responseBody != null) {
            responseBody
        } else {
            emptyList()
        }

        return comments
    }

}
```

### 2. Create Repository as `/network/PostsRepository`

```
interface PostsRepository {
    suspend fun getPosts(): List<ApiPost>
    suspend fun getComments(postId: Long): List<ApiComment>
}

class PostsRepositoryImpl : PostsRepository {

    private val remotePostsDataSource: RemotePostsDataSource = RemotePostsDataSourceImpl()

    override suspend fun getPosts(): List<ApiPost> {
        return remotePostsDataSource.getPosts()
    }

    override suspend fun getComments(postId: Long): List<ApiComment> {
        return remotePostsDataSource.getComments(postId = postId)
    }
}
```

### 3. Use `PostsRepository` in `PostsViewModel` and `CommentsViewModel`

PostsViewModel
```
class PostsViewModel : ViewModel() {

    private val postsRepository: PostsRepository = PostsRepositoryImpl()
    private val _posts: MutableStateFlow<List<ApiPost>> = MutableStateFlow(emptyList())
    val posts: StateFlow<List<ApiPost>> = _posts.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _posts.update {
                postsRepository.getPosts()
            }
        }
    }
}
```

CommentsViewModel
```
class CommentsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val postsRepository: PostsRepository = PostsRepositoryImpl()
    private val postId: Long = savedStateHandle.toRoute<CommentsRoute>().postId

    private val _comments: MutableStateFlow<List<ApiComment>> = MutableStateFlow(emptyList())
    val comments: StateFlow<List<ApiComment>> = _comments.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _comments.update {
                postsRepository.getComments(postId = postId)
            }
        }
    }
}
```

## Dependency Injection

### 1. Add Koin library to gradle/libs.versions.toml

```
[versions]
koin = "4.0.0"

[libraries]
# Koin
koin-android = { group = "io.insert-koin", name = "koin-android", version.ref = "koin" }
koin-compose = { group = "io.insert-koin", name = "koin-androidx-compose", version.ref = "koin" }
```

### 2. Add Koin dependencies to app/build.gradle.kts

```
dependencies {
    ...
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
}
```

### 3. Create Dependency Injection Module as `/di/AppModule.kt`

```
val appModule = module {
    viewModelOf(::CommentsViewModel)
    viewModelOf(::PostsViewModel)

    singleOf(::PostsRepositoryImpl) bind PostsRepository::class
    singleOf(::RemotePostsDataSourceImpl) bind RemotePostsDataSource::class
}
```

### 4. Add DI Module to App as `App.kt`

App.kt
```
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule)
        }
    }
}
```

AndroidManifest.xml
```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    …

    <application
        android:name="de.appsfactory.lecture.App"
	…

    </application>

</manifest>
```

### 5. Replace Repository & Data Source initialization with injection

```
class PostsRepositoryImpl(
    private val remotePostsDataSource: RemotePostsDataSource,
) : PostsRepository {

    override suspend fun getPosts(): List<ApiPost> {
        return remotePostsDataSource.getPosts()
    }

    override suspend fun getComments(postId: Long): List<ApiComment> {
        return remotePostsDataSource.getComments(postId = postId)
    }
}
```

```
class PostsViewModel(
    private val postsRepository: PostsRepository,
) : ViewModel() {
	…
}
```

```
class CommentsViewModel(
    private val postsRepository: PostsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
	…
}
```

### 6. Inject ViewModel instances in screen Composables

```
@Composable
fun PostsScreen(
    onPostClick: (ApiPost) -> Unit,
    viewModel: PostsViewModel = koinViewModel(),
) {
	…
}
```

```
@Composable
fun CommentsScreen(
    onUpClick: () -> Unit,
    viewModel: CommentsViewModel = koinViewModel(),
) {
	…
}
```