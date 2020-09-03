package louis.flight.status.info.data.mappers

import louis.flight.status.info.data.local.model.Country
import louis.flight.status.info.data.local.model.GlobalSummary
import louis.flight.status.info.data.local.model.LocalSummary
import louis.flight.status.info.data.remote.model.CountriesRemote
import louis.flight.status.info.data.remote.model.GlobalSummaryRemote
import louis.flight.status.info.data.remote.model.LocalSummaryRemote
import louis.flight.status.info.utils.testCountry1
import louis.flight.status.info.utils.testCountry2
import louis.flight.status.info.utils.testCountry3
import louis.flight.status.info.utils.testCountry4
import louis.flight.status.info.utils.testCountry5
import louis.flight.status.info.utils.testCountry6
import louis.flight.status.info.utils.testCountryRemote1
import louis.flight.status.info.utils.testCountryRemote2
import louis.flight.status.info.utils.testCountryRemote3
import louis.flight.status.info.utils.testCountryRemote4
import louis.flight.status.info.utils.testCountryRemote5
import louis.flight.status.info.utils.testCountryRemote6
import louis.flight.status.info.utils.testGlobalSummary
import louis.flight.status.info.utils.testGlobalSummaryRemote
import louis.flight.status.info.utils.testLocalSummary
import louis.flight.status.info.utils.testLocalSummaryRemote
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MappersExtensionsKtTest {

    private val testCountriesRemote = CountriesRemote(
        countries = listOf(
            testCountryRemote1,
            testCountryRemote2,
            testCountryRemote3,
            testCountryRemote4,
            testCountryRemote5,
            testCountryRemote6
        )
    )

    private val testCountriesList = listOf(
        testCountry1,
        testCountry2,
        testCountry3,
        testCountry4,
        testCountry5,
        testCountry6
    )

    @Test
    fun mapToLocalSummary_Global_validObject() {
        val expected = testGlobalSummary
        val result = testGlobalSummaryRemote.mapToLocalSummary()
        assertEquals(expected, result)
    }

    @Test
    fun mapToLocalSummary_Global_withNulls() {
        val testDataWithNulls = GlobalSummaryRemote(
            null,
            null,
            null,
            null,
            null
        )
        val expected = GlobalSummary(
            null,
            null,
            null,
            null,
            "",
            ""
        )
        val result = testDataWithNulls.mapToLocalSummary()
        assertEquals(expected, result)
    }

    @Test
    fun mapToLocalSummary_Local_validObject() {
        val expected = testLocalSummary
        val result = testLocalSummaryRemote.mapToLocalSummary()
        assertEquals(expected, result)
    }

    @Test
    fun mapToLocalSummary_Local_withNulls() {
        val testDataWithNulls = LocalSummaryRemote(
            null,
            null,
            null,
            null
        )
        val expected = LocalSummary(
            null,
            null,
            null,
            null,
            ""
        )
        val result = testDataWithNulls.mapToLocalSummary()
        assertEquals(expected, result)
    }

    @Test
    fun mapToLocalCountry_validData() {
        val expected = testCountriesList
        val result = testCountriesRemote.mapToLocalCountryList()
        assertEquals(expected, result)
    }

    @Test
    fun mapToLocalCountry_emptyList() {
        val testCountriesEmpty = CountriesRemote(emptyList())
        val expected = emptyList<Country>()
        val result = testCountriesEmpty.mapToLocalCountryList()
        assertEquals(expected, result)
    }
}
