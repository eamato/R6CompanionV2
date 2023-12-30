package eamato.funn.r6companion.domain.usecases

import eamato.funn.r6companion.core.utils.Result
import eamato.funn.r6companion.data.repositories.weapons.IWeaponsRepository
import javax.inject.Inject

class WeaponsUseCase @Inject constructor(private val weaponsRepository: IWeaponsRepository) {

    suspend operator fun invoke(): Result<String> {
        return Result.Success(weaponsRepository.getWeaponsPlaceHolder())
    }
}