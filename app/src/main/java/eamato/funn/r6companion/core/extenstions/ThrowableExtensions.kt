package eamato.funn.r6companion.core.extenstions

import eamato.funn.r6companion.core.utils.logger.ILogger
import eamato.funn.r6companion.core.utils.logger.Message

fun Throwable.log(logger: ILogger) {
    logger.e(Message.message { message = "ERROR" }, this)
}