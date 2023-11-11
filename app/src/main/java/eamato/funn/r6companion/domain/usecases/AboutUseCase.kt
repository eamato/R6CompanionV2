package eamato.funn.r6companion.domain.usecases

import eamato.funn.r6companion.core.utils.Result
import javax.inject.Inject
import eamato.funn.r6companion.data.repositories.about.IAboutRepository
import eamato.funn.r6companion.domain.entities.settings.SettingsAboutInfo
import eamato.funn.r6companion.domain.mappers.settings.AboutInfoUseCaseMapper

class AboutUseCase @Inject constructor(private val aboutRepository: IAboutRepository) {

    suspend operator fun invoke(): Result<SettingsAboutInfo> {
        return Result.Success(AboutInfoUseCaseMapper.map(aboutRepository.getAboutInfo()))
    }
}