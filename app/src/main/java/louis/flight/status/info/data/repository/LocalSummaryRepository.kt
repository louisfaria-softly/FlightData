package louis.flight.status.info.data.repository

import android.content.SharedPreferences
import com.github.mikephil.charting.data.PieEntry
import louis.flight.status.info.data.local.dao.LocalSummaryDao
import louis.flight.status.info.data.local.model.LocalSummary
import louis.flight.status.info.data.mappers.mapToLocalSummary
import louis.flight.status.info.data.remote.ApiService
import louis.flight.status.info.data.remote.BASE_COUNTRY_URL
import louis.flight.status.info.data.remote.model.LocalSummaryRemote
import louis.flight.status.info.util.PREFS_KEY_CHOSEN_LOCATION
import louis.flight.status.info.util.PREFS_LAST_REFRESH_LOCAL_SUMMARY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class LocalSummaryRepository(
    private val apiService: ApiService,
    private val localSummaryDao: LocalSummaryDao,
    private val sharedPrefs: SharedPreferences
) : BaseRepository<LocalSummaryRemote, LocalSummary>(sharedPrefs) {

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_LOCAL_SUMMARY

    fun getLocalSummaryFlow(): Flow<LocalSummary> = localSummaryDao.getLocalSummaryFlow()

    fun getLocalSummaryPieChartDataFlow(): Flow<List<PieEntry>> =
        localSummaryDao.getLocalSummaryFlow()
            .map { summary -> mapLocalSummaryToPieChartEntry(summary) }

    override suspend fun makeApiCall(): Response<LocalSummaryRemote> =
        apiService.getLocalSummary(BASE_COUNTRY_URL + getChosenLocation())

    override suspend fun saveToDb(data: LocalSummary) {
        localSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: LocalSummaryRemote): LocalSummary =
        data.mapToLocalSummary()

    private fun getChosenLocation(): String =
        sharedPrefs.getString(PREFS_KEY_CHOSEN_LOCATION, "Poland") ?: "Poland"

    private fun mapLocalSummaryToPieChartEntry(data: LocalSummary?): List<PieEntry> {
        return if (data != null) {
            listOf(
                PieEntry(data.confirmed?.toFloat() ?: 0F, "confirmed"),
                PieEntry(data.recovered?.toFloat() ?: 0F, "recovered"),
                PieEntry(data.deaths?.toFloat() ?: 0F, "deaths")
            )
        } else emptyList()
    }
}
