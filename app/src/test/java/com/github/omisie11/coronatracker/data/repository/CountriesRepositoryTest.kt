package louis.flight.status.info.data.repository

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import louis.flight.status.info.data.local.dao.CountriesDao
import louis.flight.status.info.data.remote.ApiService
import louis.flight.status.info.data.remote.model.CountriesRemote
import louis.flight.status.info.utils.testCountriesRemote
import louis.flight.status.info.utils.testCountry1
import louis.flight.status.info.utils.testCountry2
import louis.flight.status.info.utils.testCountry3
import louis.flight.status.info.utils.testCountry4
import louis.flight.status.info.utils.testCountry5
import louis.flight.status.info.utils.testCountry6
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CountriesRepositoryTest {

    private val testCountriesLocalList = listOf(
        testCountry1,
        testCountry2,
        testCountry3,
        testCountry4,
        testCountry5,
        testCountry6
    )

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var countriesDao: CountriesDao

    @Mock
    private lateinit var sharedPrefs: SharedPreferences

    // Class under test
    private lateinit var countriesRepository: CountriesRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)

        countriesRepository = CountriesRepository(apiService, countriesDao, sharedPrefs)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCountriesNamesFlowTest() = runBlocking {
        val countriesFlow = flowOf(testCountriesLocalList)

        Mockito.`when`(countriesDao.getCountriesNamesFlow()).thenAnswer {
            return@thenAnswer countriesFlow
        }

        val result: List<String> =
            countriesRepository.getCountriesNamesFlow().take(1).toList()[0]

        verify(countriesDao, times(1)).getCountriesNamesFlow()
        assertEquals(result, testCountriesLocalList)
    }

    @Test
    fun refreshData_force_validResponse_VerifyDataSaved() = runBlocking {
        val networkResponse = Response.success(testCountriesRemote)
        Mockito.`when`(apiService.getCountries()).thenAnswer {
            return@thenAnswer networkResponse
        }
        countriesRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getCountries()
        verify(countriesDao, times(1)).replace(testCountriesLocalList)
    }

    @Test
    fun refreshData_force_errorResponse_VerifyNotDataSaved() = runBlocking {
        val responseError: Response<CountriesRemote> = Response.error(
            403,
            ResponseBody.create(
                MediaType.parse("application/json"), "Bad Request"
            )
        )
        Mockito.`when`(apiService.getCountries()).thenAnswer {
            return@thenAnswer responseError
        }
        countriesRepository.refreshData(forceRefresh = true)

        verify(apiService, times(1)).getCountries()
        verify(countriesDao, times(0)).replace(testCountriesLocalList)
    }
}
