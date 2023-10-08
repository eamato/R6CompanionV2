package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.utils.recyclerview.ListChangeListener

abstract class ABaseAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val listChangeListener: ListChangeListener
) : ListAdapter<T, ABaseAdapter.ABaseViewHolder<T>>(diffCallback) {

    override fun onBindViewHolder(holder: ABaseViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        super.onCurrentListChanged(previousList, currentList)

        if (previousList.isEmpty() || currentList.isEmpty()) {
            return
        }

        listChangeListener()
    }

    fun getItemAtPosition(position: Int): T = getItem(position)

    abstract class ABaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(item: T)
    }
}