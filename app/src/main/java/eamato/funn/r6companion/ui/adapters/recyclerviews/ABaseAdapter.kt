package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class ABaseAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, ABaseAdapter.ABaseViewHolder<T>>(diffCallback) {

    override fun onBindViewHolder(holder: ABaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAtPosition(position: Int): T = getItem(position)

    abstract class ABaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: T)
    }
}