package eamato.funn.r6companion.domain.usecases

interface IUseCaseMapper<I, O> {
    fun map(input: I): O
}