package eamato.funn.r6companion.ui.entities.news.details

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner

interface IContentView {

    fun createView(parent: ViewGroup, lifecycleOwner: LifecycleOwner): View?
}