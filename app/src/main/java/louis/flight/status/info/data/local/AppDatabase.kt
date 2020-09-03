package louis.flight.status.info.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import louis.flight.status.info.data.local.dao.CountriesDao
import louis.flight.status.info.data.local.dao.GlobalSummaryDao
import louis.flight.status.info.data.local.dao.LocalSummaryDao
import louis.flight.status.info.data.local.model.Country
import louis.flight.status.info.data.local.model.GlobalSummary
import louis.flight.status.info.data.local.model.LocalSummary

@Database(entities = [GlobalSummary::class, LocalSummary::class, Country::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun globalSummaryDao(): GlobalSummaryDao
    abstract fun localSummaryDao(): LocalSummaryDao
    abstract fun countriesDao(): CountriesDao
}
