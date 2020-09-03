package louis.flight.status.info.data.repository

import android.content.SharedPreferences
import louis.flight.status.info.data.local.dao.CountriesDao
import louis.flight.status.info.data.local.model.Country
import louis.flight.status.info.data.mappers.mapToLocalCountryList
import louis.flight.status.info.data.remote.ApiService
import louis.flight.status.info.data.remote.model.CountriesRemote
import louis.flight.status.info.util.PREFS_LAST_REFRESH_COUNTRIES
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class CountriesRepository(
    private val apiService: ApiService,
    private val countriesDao: CountriesDao,
    sharedPrefs: SharedPreferences
) : BaseRepository<CountriesRemote, List<Country>>(sharedPrefs) {

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_COUNTRIES

    fun getCountriesNamesFlow(): Flow<List<String>> = countriesDao.getCountriesNamesFlow()

    override suspend fun makeApiCall(): Response<CountriesRemote> = apiService.getCountries()

    override suspend fun mapRemoteModelToLocal(data: CountriesRemote): List<Country> =
        data.mapToLocalCountryList()

    override suspend fun saveToDb(data: List<Country>) {
        countriesDao.replace(data)
    }
}
