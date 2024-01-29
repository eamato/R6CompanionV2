package eamato.funn.r6companion.core.extenstions

import android.graphics.Rect
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import com.google.android.material.transition.platform.MaterialSharedAxis

fun View.setViewsEnabled(isEnabled: Boolean) {
    if (this is ViewGroup) {
        children.forEach { it.setViewsEnabled(isEnabled) }
    }

    this.isEnabled = isEnabled
}

fun View?.applySystemInsetsIfNeeded(onInsetsFound: (Insets) -> Unit) {
    if (this == null) {
        return
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        if (insets.top > 0 || insets.left > 0 || insets.right > 0 || insets.bottom > 0) {
            onInsetsFound.invoke(insets)
        }

        WindowInsetsCompat.CONSUMED
    }
}

fun View.setViewVisibleOrGone(visibility: Boolean) {
    this.visibility = if (visibility) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun ViewGroup.slideUpAndShow(onTransitionEnded: (() -> Unit)? = null) {
    val modalIn = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    onTransitionEnded?.run nonNullListener@ {
        modalIn.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}

            override fun onTransitionEnd(transition: Transition) {
                this@nonNullListener.invoke()
            }

            override fun onTransitionCancel(transition: Transition) {
                this@nonNullListener.invoke()
            }

            override fun onTransitionPause(transition: Transition) {}

            override fun onTransitionResume(transition: Transition) {}
        })
    }
    TransitionManager.beginDelayedTransition(this, modalIn)
    setViewVisibleOrGone(true)
}

fun ViewGroup.slideDownAndHide(onTransitionEnded: (() -> Unit)? = null) {
    val modalOut = MaterialSharedAxis(MaterialSharedAxis.Y, false)
    onTransitionEnded?.run nonNullListener@ {
        modalOut.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}

            override fun onTransitionEnd(transition: Transition) {
                this@nonNullListener.invoke()
            }

            override fun onTransitionCancel(transition: Transition) {
                this@nonNullListener.invoke()
            }

            override fun onTransitionPause(transition: Transition) {}

            override fun onTransitionResume(transition: Transition) {}
        })
    }
    TransitionManager.beginDelayedTransition(this, modalOut)
    setViewVisibleOrGone(false)
}

fun View?.clearFocusAndHideKeyboard() {
    if (this == null) {
        return
    }

    clearFocus()
    ContextCompat.getSystemService(context, InputMethodManager::class.java)?.hideKeyboard(this)
}

fun View.findViewByLocation(x: Int, y: Int): View? {
    return findViewTraversal(x, y)
}

fun View.findViewTraversal(x: Int, y: Int): View? {
    if (this is ViewGroup) {
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            if (child.visibility == View.VISIBLE) {
                val rect = Rect()
                child.getHitRect(rect)
                if (rect.contains(x, y)) {
                    return child.findViewTraversal(x - rect.left, y - rect.top)
                }
            }
        }
    }
    return this.takeIf { isVisible }
}