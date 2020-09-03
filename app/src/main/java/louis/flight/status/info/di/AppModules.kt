package louis.flight.status.info.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import louis.flight.status.info.data.local.AppDatabase
import louis.flight.status.info.data.remote.ApiService
import louis.flight.status.info.data.remote.BASE_URL
import louis.flight.status.info.data.repository.CountriesRepository
import louis.flight.status.info.data.repository.GlobalSummaryRepository
import louis.flight.status.info.data.repository.LocalSummaryRepository
import louis.flight.status.info.ui.global.GlobalViewModel
import louis.flight.status.info.ui.local.LocalViewModel
import louis.flight.status.info.ui.settings.CountriesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val mainModule = module {

    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        Room.databaseBuilder(
                androidApplication(),
                AppDatabase::class.java,
                "corona_data.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { PreferenceManager.getDefaultSharedPreferences(get()) }
}

val networkModule = module {

    // Create Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    // Create retrofit Service
    single { get<Retrofit>().create(ApiService::class.java) }
}

val globalModule = module {

    single { get<AppDatabase>().globalSummaryDao() }

    single {
        GlobalSummaryRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel { GlobalViewModel(get()) }
}

val localModule = module {

    single { get<AppDatabase>().localSummaryDao() }

    single {
        LocalSummaryRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel { LocalViewModel(get()) }
}

val countriesModule = module {

    single { get<AppDatabase>().countriesDao() }

    single {
        CountriesRepository(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        CountriesViewModel(
            get()
        )
    }
}
