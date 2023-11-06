package eamato.funn.r6companion.data.entities

import eamato.funn.r6companion.core.MAPS_COUNT_DEFAULT_VALUE

data class MapsRequestParams(
    val skip: Int = 0,
    val limit: Int = MAPS_COUNT_DEFAULT_VALUE
)