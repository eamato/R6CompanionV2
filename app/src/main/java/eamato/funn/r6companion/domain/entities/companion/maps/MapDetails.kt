package eamato.funn.r6companion.domain.entities.companion.maps

data class MapDetails(
    val id: String,
    val playlists: List<String>,
    val imageUrl: String?,
    val name: String,
    val location: String,
    val content: String,
    val blueprints: List<MapDetailsBlueprint>
) {

    data class MapDetailsBlueprint(
        val id: String,
        val name: String,
        val imageUrl: String?,
    )
}