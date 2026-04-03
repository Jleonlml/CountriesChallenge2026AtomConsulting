package com.devart.countrieschallange2026.di

import android.content.Context
import com.devart.countrieschallange2026.DaggerApplication
import com.devart.countrieschallange2026.MainActivity
import com.devart.data.di.NetworkModule
import com.devart.data.di.RepositoryModule
import com.devart.domain.repository.CountryRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(application: DaggerApplication)
    fun countryRepository(): CountryRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}