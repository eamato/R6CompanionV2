package eamato.funn.r6companion.core.utils

object ScrollToTopAdditionalEvent : IAdditionalEvent {

    private const val EVENT = "scroll_to_top"

    override fun getEvent(): String {
        return EVENT
    }
}