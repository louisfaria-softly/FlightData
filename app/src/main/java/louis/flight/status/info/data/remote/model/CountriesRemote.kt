package louis.flight.status.info.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountriesRemote(
    @Json(name = "countries") val countries: List<CountryRemote>
)
