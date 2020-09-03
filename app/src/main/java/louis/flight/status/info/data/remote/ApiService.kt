package louis.flight.status.info.data.remote

import louis.flight.status.info.data.remote.model.CountriesRemote
import louis.flight.status.info.data.remote.model.GlobalSummaryRemote
import louis.flight.status.info.data.remote.model.LocalSummaryRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    /**
     * Get global summary info
     */
    @GET(GLOBAL_URL)
    suspend fun getGlobalSummary(): Response<GlobalSummaryRemote>

    /**
     * Pass country name in English to get summary for single country
     * .../api/countries/[country]
     */
    @GET
    suspend fun getLocalSummary(@Url country: String): Response<LocalSummaryRemote>

    @GET(BASE_COUNTRY_URL)
    suspend fun getCountries(): Response<CountriesRemote>
}
