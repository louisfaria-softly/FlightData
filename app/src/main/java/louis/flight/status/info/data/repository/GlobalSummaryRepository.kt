package louis.flight.status.info.data.repository

import android.content.SharedPreferences
import com.github.mikephil.charting.data.PieEntry
import louis.flight.status.info.data.local.dao.GlobalSummaryDao
import louis.flight.status.info.data.local.model.GlobalSummary
import louis.flight.status.info.data.mappers.mapToLocalSummary
import louis.flight.status.info.data.remote.ApiService
import louis.flight.status.info.data.remote.model.GlobalSummaryRemote
import louis.flight.status.info.util.PREFS_LAST_REFRESH_GLOBAL_SUMMARY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

class GlobalSummaryRepository(
    private val apiService: ApiService,
    private val globalSummaryDao: GlobalSummaryDao,
    sharedPrefs: SharedPreferences
) : BaseRepository<GlobalSummaryRemote, GlobalSummary>(sharedPrefs) {

    override val lastRefreshKey: String = PREFS_LAST_REFRESH_GLOBAL_SUMMARY

    fun getGlobalSummaryFlow(): Flow<GlobalSummary> = globalSummaryDao.getGlobalSummaryFlow()

    fun getGlobalSummaryPieChartDataFlow(): Flow<List<PieEntry>> =
        globalSummaryDao.getGlobalSummaryFlow()
            .map { summary -> mapGlobalSummaryToPieChartEntry(summary) }

    override suspend fun makeApiCall(): Response<GlobalSummaryRemote> =
        apiService.getGlobalSummary()

    override suspend fun saveToDb(data: GlobalSummary) {
        globalSummaryDao.replace(data)
    }

    override suspend fun mapRemoteModelToLocal(data: GlobalSummaryRemote): GlobalSummary =
        data.mapToLocalSummary()

    private fun mapGlobalSummaryToPieChartEntry(data: GlobalSummary?): List<PieEntry> {
        return if (data != null) {
            listOf(
                PieEntry(data.confirmed?.toFloat() ?: 0F, "confirmed"),
                PieEntry(data.recovered?.toFloat() ?: 0F, "recovered"),
                PieEntry(data.deaths?.toFloat() ?: 0F, "deaths")
            )
        } else emptyList()
    }
}
