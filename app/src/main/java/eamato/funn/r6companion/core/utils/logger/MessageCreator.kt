package eamato.funn.r6companion.core.utils.logger

class Message(
    private val clazz: Class<*>,
    private val message: String
) {

    private constructor(builder: Builder) : this(builder.clazz, builder.message)

    companion object {
        inline fun message(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    fun string() = "${this.clazz.name} -> ${this.message}"

    class Builder {
        var clazz: Class<*> = this::class.java
        var message: String = ""

        fun build() = Message(this)
    }
}