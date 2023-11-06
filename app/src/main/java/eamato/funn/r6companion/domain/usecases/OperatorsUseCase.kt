package eamato.funn.r6companion.domain.usecases

import eamato.funn.r6companion.data.entities.Operators
import eamato.funn.r6companion.data.repositories.operators.IOperatorsRepository
import eamato.funn.r6companion.core.utils.Result
import javax.inject.Inject

class OperatorsUseCase @Inject constructor(private val operatorsRepository: IOperatorsRepository) {

    suspend operator fun <T> invoke(mapper: IUseCaseMapper<Operators.Operator, T?>): Result<List<T>> {
        val operators = operatorsRepository
            .getOperators()
            .mapNotNull { operator -> mapper.map(operator) }

        if (operators.isEmpty()) {
            return Result.Error(Throwable("Something went wrong"))
        }

        return Result.Success(operators)
    }
}