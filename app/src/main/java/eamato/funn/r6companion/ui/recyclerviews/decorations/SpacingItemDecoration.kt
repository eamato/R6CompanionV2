package eamato.funn.r6companion.ui.recyclerviews.decorations

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.extenstions.getDimension
import kotlin.math.ceil

sealed class SpacingItemDecoration(
    protected val spacings: Spacings
) : RecyclerView.ItemDecoration() {

    protected data class Spacings(
        val topSpacing: Int = 0,
        val leftSpacing: Int = 0,
        val bottomSpacing: Int = 0,
        val rightSpacing: Int = 0,
        val topSpacingMultiplier: Int = 1,
        val bottomSpacingMultiplier: Int = 1,
    )

    private class GridSpacingItemDecoration(
        spacings: Spacings,
        private val spanCount: Int,
        private val includeEdge: Boolean = false
    ) : SpacingItemDecoration(spacings = spacings) {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            val numRows = ceil((state.itemCount.toFloat() / spanCount).toDouble()).toInt()
            val row = position / spanCount

            if (includeEdge) {
                outRect.left = spacings.leftSpacing - column * spacings.leftSpacing / spanCount
                outRect.right = (column + 1) * spacings.rightSpacing / spanCount

                if (position < spanCount) {
                    outRect.top = spacings.topSpacing * spacings.topSpacingMultiplier
                }
                if (row == numRows - 1) {
                    outRect.bottom = spacings.bottomSpacing * spacings.bottomSpacingMultiplier
                } else {
                    outRect.bottom = spacings.bottomSpacing
                }
            } else {
                outRect.left = column * spacings.leftSpacing / spanCount
                outRect.right = spacings.rightSpacing - (column + 1) * spacings.rightSpacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacings.topSpacing
                }
            }
        }
    }

    private class LinearSpacingItemDecoration(
        spacings: Spacings
    ) : SpacingItemDecoration(spacings = spacings) {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)

            if (position == 0) {
                outRect.top = spacings.topSpacing * spacings.topSpacingMultiplier
            } else {
                outRect.top = spacings.topSpacing
            }

            outRect.left = spacings.leftSpacing

            if (position == state.itemCount - 1) {
                outRect.bottom = spacings.bottomSpacing * spacings.bottomSpacingMultiplier
            } else {
                outRect.bottom = spacings.bottomSpacing
            }

            outRect.right = spacings.rightSpacing
        }
    }

    companion object {
        fun linear(): ILinear = Linear()
        fun grid(): IGrid = Grid()

        interface IBase {
            fun setSpacingRes(
                @DimenRes spacingTopResId: Int,
                @DimenRes spacingLeftResId: Int,
                @DimenRes spacingBottomResId: Int,
                @DimenRes spacingRightResId: Int
            ): IBase

            fun setSpacingPx(
                spacingTopInPx: Int,
                spacingLeftInPx: Int,
                spacingBottomInPx: Int,
                spacingRightInPx: Int
            ): IBase

            fun create(context: Context): SpacingItemDecoration

            fun setTopSpacingMultiplier(multiplier: Int): IBase

            fun setBottomSpacingMultiplier(multiplier: Int): IBase
        }

        interface ILinear : IBase

        private abstract class Base : IBase {
            @DimenRes
            protected var spacingTopResId: Int = -1
            @DimenRes
            protected var spacingLeftResId: Int = -1
            @DimenRes
            protected var spacingBottomResId: Int = -1
            @DimenRes
            protected var spacingRightResId: Int = -1

            protected var spacingTopInPx: Int = -1
            protected var spacingLeftInPx: Int = -1
            protected var spacingBottomInPx: Int = -1
            protected var spacingRightInPx: Int = -1

            protected var topSpacingMultiplier: Int = 1
            protected var bottomSpacingMultiplier: Int = 1

            override fun setSpacingRes(
                spacingTopResId: Int,
                spacingLeftResId: Int,
                spacingBottomResId: Int,
                spacingRightResId: Int
            ): IBase {
                this.spacingTopResId = spacingTopResId
                this.spacingLeftResId = spacingLeftResId
                this.spacingBottomResId = spacingBottomResId
                this.spacingRightResId = spacingRightResId

                return this
            }

            override fun setSpacingPx(
                spacingTopInPx: Int,
                spacingLeftInPx: Int,
                spacingBottomInPx: Int,
                spacingRightInPx: Int
            ): IBase {
                this.spacingTopInPx = spacingTopInPx
                this.spacingLeftInPx = spacingLeftInPx
                this.spacingBottomInPx = spacingBottomInPx
                this.spacingRightInPx = spacingRightInPx

                return this
            }

            protected fun prepareSpacings(context: Context): Spacings {
                val areSpacingResIdsAdded = spacingTopResId != -1 &&
                        spacingLeftResId != -1 &&
                        spacingBottomResId != -1 &&
                        spacingRightResId != -1

                if (areSpacingResIdsAdded) {
                    return Spacings(
                        topSpacing = spacingTopResId.getDimension(context),
                        leftSpacing = spacingLeftResId.getDimension(context),
                        bottomSpacing = spacingBottomResId.getDimension(context),
                        rightSpacing = spacingRightResId.getDimension(context),
                        topSpacingMultiplier = topSpacingMultiplier,
                        bottomSpacingMultiplier = bottomSpacingMultiplier
                    )
                }

                val areSpacingsInPxAdded = spacingTopInPx != -1 &&
                        spacingLeftInPx != -1 &&
                        spacingBottomInPx != -1 &&
                        spacingRightInPx != -1

                if (areSpacingsInPxAdded) {
                    return Spacings(
                        topSpacing = spacingTopInPx,
                        leftSpacing = spacingLeftInPx,
                        bottomSpacing = spacingBottomInPx,
                        rightSpacing = spacingRightInPx,
                        topSpacingMultiplier = topSpacingMultiplier,
                        bottomSpacingMultiplier = bottomSpacingMultiplier
                    )
                }

                return Spacings(
                    topSpacingMultiplier = topSpacingMultiplier,
                    bottomSpacingMultiplier = bottomSpacingMultiplier
                )
            }

            override fun setTopSpacingMultiplier(multiplier: Int): IBase {
                topSpacingMultiplier = multiplier
                return this
            }

            override fun setBottomSpacingMultiplier(multiplier: Int): IBase {
                bottomSpacingMultiplier = multiplier
                return this
            }
        }

        private class Linear : Base(), ILinear {

            override fun create(context: Context): SpacingItemDecoration {
                return LinearSpacingItemDecoration(prepareSpacings(context))
            }
        }

        interface IGrid : IBase {
            fun setSpanCount(spanCount: Int): IGrid
            fun setIncludeEdge(includeEdge: Boolean): IGrid
        }

        private class Grid : Base(), IGrid {

            private var spanCount: Int = 1
            private var includeEdge: Boolean = false

            override fun create(context: Context): SpacingItemDecoration {
                return GridSpacingItemDecoration(
                    prepareSpacings(context),
                    spanCount = spanCount,
                    includeEdge = includeEdge,
                )
            }

            override fun setSpanCount(spanCount: Int): IGrid {
                this.spanCount = spanCount
                return this
            }

            override fun setIncludeEdge(includeEdge: Boolean): IGrid {
                this.includeEdge = includeEdge
                return this
            }
        }
    }
}