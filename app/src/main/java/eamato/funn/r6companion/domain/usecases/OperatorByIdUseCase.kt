package eamato.funn.r6companion.domain.usecases

import eamato.funn.r6companion.data.entities.Operators
import eamato.funn.r6companion.data.repositories.operators.IOperatorsRepository
import eamato.funn.r6companion.core.utils.Result
import javax.inject.Inject

class OperatorByIdUseCase @Inject constructor(private val operatorsRepository: IOperatorsRepository) {

    suspend operator fun <T> invoke(
        operatorId: Int,
        mapper: IUseCaseMapper<Operators.Operator, T?>
    ): Result<T> {
        val operator = operatorsRepository.getOperatorById(operatorId)
            ?: return Result.Error(Throwable("There is no operator with id: $operatorId"))
        val mappedOperator = operator.let { mapper.map(operator) }
            ?: return Result.Error(Throwable("Something went wrong"))

        return Result.Success(mappedOperator)
    }
}