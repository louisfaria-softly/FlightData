package louis.flight.status.info.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import louis.flight.status.info.data.local.model.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<Country>)

    @Transaction
    fun replace(data: List<Country>) {
        delete()
        insert(data)
    }

    @Query("select * from countries_list_table")
    fun getCountriesFlow(): Flow<List<Country>>

    @Query("select name from countries_list_table")
    fun getCountriesNamesFlow(): Flow<List<String>>

    @Query("delete from countries_list_table")
    fun delete()
}
