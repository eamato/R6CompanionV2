package eamato.funn.r6companion.core.extenstions

fun <T> MutableList<T>.insertItemAtEveryStep(item: T, step: Int): MutableList<T> {
    var iteration = step
    val count = size / step
    for (i in 0 until count) {
        add(iteration, item)
        iteration += step
    }
    return this
}