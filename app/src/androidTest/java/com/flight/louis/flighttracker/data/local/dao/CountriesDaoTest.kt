package com.flight.louis.flighttracker.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import louis.flight.status.info.data.local.AppDatabase
import louis.flight.status.info.data.local.model.Country
import com.flight.louis.flighttracker.testCountry1
import com.flight.louis.flighttracker.testCountry2
import com.flight.louis.flighttracker.testCountry3
import com.flight.louis.flighttracker.testCountry4
import com.flight.louis.flighttracker.testCountry5
import com.flight.louis.flighttracker.testCountry6
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import louis.flight.status.info.data.local.dao.CountriesDao
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CountriesDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var countriesDao: CountriesDao

    private val testDispatcher = TestCoroutineDispatcher()

    private val testCountries = listOf(
        testCountry3,
        testCountry4,
        testCountry1,
        testCountry2,
        testCountry5,
        testCountry6
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        countriesDao = database.countriesDao()
    }

    @After
    fun cleanup() {
        database.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInsertAndGetCountries() = runBlocking {
        countriesDao.insert(testCountries)
        val result: List<Country> = countriesDao.getCountriesFlow().take(1).toList()[0]
        assertThat(result.size, equalTo(testCountries.size))
        for (i in result.indices) {
            assertThat(result[i].name, equalTo(testCountries[i].name))
        }
    }

    @Test
    fun testInsertAndGetCountriesNames() = runBlocking {
        countriesDao.insert(testCountries)
        val expected: MutableList<String> = mutableListOf()
        testCountries.forEach { country ->
            expected.add(country.name)
        }

        val result: List<String> = countriesDao.getCountriesNamesFlow().take(1).toList()[0]
        assertThat(result.size, equalTo(expected.size))
        for (i in result.indices) {
            assertThat(result[i], equalTo(expected[i]))
        }
    }

    @Test
    fun testReplaceCountriesData() = runBlocking {
        // Replace 2 times to check if there will be no duplicated data
        countriesDao.replace(testCountries)
        countriesDao.replace(testCountries)

        val result: List<Country> = countriesDao.getCountriesFlow().take(1).toList()[0]
        assertThat(result.size, equalTo(testCountries.size))
    }

    @Test
    fun testDeleteCountries() = runBlocking {
        countriesDao.replace(testCountries)
        countriesDao.delete()

        val result: List<Country> = countriesDao.getCountriesFlow().take(1).toList()[0]
        assertThat(result.size, equalTo(0))
    }
}
