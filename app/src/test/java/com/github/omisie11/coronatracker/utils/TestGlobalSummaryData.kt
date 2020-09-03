package louis.flight.status.info.utils

import com.github.mikephil.charting.data.PieEntry
import louis.flight.status.info.data.local.model.GlobalSummary
import louis.flight.status.info.data.remote.model.GlobalSummaryRemote
import louis.flight.status.info.data.remote.model.SummaryRemoteItem

val testGlobalSummary = GlobalSummary(
    confirmed = 474204,
    recovered = 115003,
    deaths = 21353,
    imageUrl = "https://covid19.mathdro.id/api/og",
    lastUpdate = "2020-03-26T09:14:15.000Z"
)

val testGlobalSummaryRemote = GlobalSummaryRemote(
    SummaryRemoteItem(value = 474204, detail = ""),
    SummaryRemoteItem(value = 115003, detail = ""),
    SummaryRemoteItem(value = 21353, detail = ""),
    imageUrl = "https://covid19.mathdro.id/api/og",
    lastUpdate = "2020-03-26T09:14:15.000Z"
)

val testGlobalSummaryPieChartData = listOf(
    PieEntry(474204F, "confirmed"),
    PieEntry(115003F, "recovered"),
    PieEntry(21353F, "deaths")
)
