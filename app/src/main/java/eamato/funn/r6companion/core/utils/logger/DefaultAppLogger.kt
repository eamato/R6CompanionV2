package eamato.funn.r6companion.core.utils.logger

class DefaultAppLogger private constructor(): ILogger {

    companion object {
        private var instance: DefaultAppLogger? = null

        fun getInstance(): DefaultAppLogger {
            if (instance == null) {
                instance = DefaultAppLogger()
            }

            return instance!!
        }
    }

    init {
        i(Message.message {
            clazz = this@DefaultAppLogger::class.java
            message = "DefaultAppLogger initialized"
        })
    }
}