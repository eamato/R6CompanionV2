package eamato.funn.r6companion.core.extenstions

inline fun <T> Boolean.onTrueInvoke(predicate: () -> T): T? {
    if (this)
        return predicate.invoke()

    return null
}