package de.appsfactory.lecture.di

import de.appsfactory.lecture.network.MetApiService
import de.appsfactory.lecture.ui.detail.DetailViewModel
import de.appsfactory.lecture.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://collectionapi.metmuseum.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MetApiService::class.java)
    }

    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
