package eamato.funn.r6companion.domain.mappers.companion

import eamato.funn.r6companion.MapsDetailsMasterCollectionQuery.Item
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper
import eamato.funn.r6companion.domain.entities.companion.maps.Map

object MapsUseCaseMapper : IUseCaseMapper<Item, Map?> {

    override fun map(input: Item): Map? {
        return input.toDomainMap()
    }
}

private fun Item.toDomainMap(): Map? {
    val localizedItems = this.localizedItems ?: return null

    val id = this.sys.id
    val name = localizedItems.title ?: return null
    val imageUrl = this.mapThumbnail?.url

    return Map(id = id, name = name, imageUrl = imageUrl)
}