package louis.flight.status.info.utils

import com.github.mikephil.charting.data.PieEntry
import louis.flight.status.info.data.local.model.LocalSummary
import louis.flight.status.info.data.remote.model.LocalSummaryRemote
import louis.flight.status.info.data.remote.model.SummaryRemoteItem

val testLocalSummary = LocalSummary(
    confirmed = 1085,
    recovered = 7,
    deaths = 15,
    lastUpdate = "2020-03-26T09:09:25.000Z"
)

val testLocalSummaryRemote = LocalSummaryRemote(
    SummaryRemoteItem(value = 1085, detail = ""),
    SummaryRemoteItem(value = 7, detail = ""),
    SummaryRemoteItem(value = 15, detail = ""),
    lastUpdate = "2020-03-26T09:09:25.000Z"
)

val testLocalSummaryPieChartData = listOf(
    PieEntry(1085F, "confirmed"),
    PieEntry(7F, "recovered"),
    PieEntry(15F, "deaths")
)
