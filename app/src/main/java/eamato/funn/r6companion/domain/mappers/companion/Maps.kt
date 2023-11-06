package eamato.funn.r6companion.domain.mappers.companion

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery.Item
import eamato.funn.r6companion.MapsDetailsMasterQuery.MapsDetailsMaster
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper
import eamato.funn.r6companion.domain.entities.companion.maps.Map
import eamato.funn.r6companion.domain.entities.companion.maps.MapDetails

object MapsUseCaseMapper : IUseCaseMapper<Item, Map?> {

    override fun map(input: Item): Map? {
        return input.toDomainMap()
    }

    private fun Item.toDomainMap(): Map? {
        val localizedItems = this.localizedItems ?: return null

        val id = this.sys.id
        val name = localizedItems.title ?: return null
        val imageUrl = this.mapThumbnail?.url

        return Map(id = id, name = name, imageUrl = imageUrl)
    }
}

object MapDetailsUseCaseMapper : IUseCaseMapper<MapsDetailsMaster, MapDetails?> {

    override fun map(input: MapsDetailsMaster): MapDetails? {
        return input.toDomainMapDetails()
    }

    private fun MapsDetailsMaster.toDomainMapDetails(): MapDetails? {
        val id = this.sys.id
        val playlists = this.playlists
            ?.filterNotNull()
            ?.map { playlist -> playlist.replaceFirstChar { char -> char.uppercase() } }
            ?: emptyList()
        val imageUrl = this.mapThumbnail?.url
        val name = this.localizedItems?.title ?: return null
        val location = this.localizedItems.location ?: ""
        val content = this.localizedItems.content ?: ""
        val blueprints = this.localizedItems.blueprintsGallery
            ?.localizedItems
            ?.assetWithDetailsCollection
            ?.items
            ?.filterNotNull()
            ?.mapNotNull blueprintMapper@ { blueprint ->
                MapDetails.MapDetailsBlueprint(
                    id = blueprint.sys.id,
                    name = blueprint.localizedItems?.name ?: return@blueprintMapper null,
                    imageUrl = blueprint.localizedItems.image?.url
                )
            }
            ?: emptyList()

        return MapDetails(
            id = id,
            playlists = playlists,
            imageUrl = imageUrl,
            name = name,
            location = location,
            content = content,
            blueprints = blueprints
        )
    }
}

