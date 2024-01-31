package eamato.funn.r6companion.core.utils.recyclerview

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message

class RecyclerViewItemClickListener(
    private val recyclerView: RecyclerView,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.OnItemTouchListener {

    private val logger = DefaultAppLogger.getInstance()

    private val gestureDetector =
        GestureDetector(recyclerView.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent) = true
            override fun onLongPress(e: MotionEvent) {
                recyclerView.findChildViewUnder(e.x, e.y)
                    ?.run {
                        val position = recyclerView.getChildAdapterPosition(this)
                        if (position != RecyclerView.NO_POSITION) {
                            logger.i(Message.message {
                                clazz = this@RecyclerViewItemClickListener::class.java
                                message = "Item long pressed on position: $position"
                            })
                            onItemClickListener.onItemLongClicked(this, position)
                        }
                    }
            }
        })

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        rv.findChildViewUnder(e.x, e.y)?.run {
            if (!gestureDetector.onTouchEvent(e))
                return false
            val position = rv.getChildAdapterPosition(this)
            if (position != RecyclerView.NO_POSITION) {
                logger.i(Message.message {
                    clazz = this@RecyclerViewItemClickListener::class.java
                    message = "Item clicked on position: $position"
                })
                onItemClickListener.onItemClicked(this, position)
                return true
            }
            return false
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)
        fun onItemLongClicked(view: View, position: Int)
    }

    interface OnItemTapListener : OnItemClickListener {
        override fun onItemLongClicked(view: View, position: Int) {}
    }
}