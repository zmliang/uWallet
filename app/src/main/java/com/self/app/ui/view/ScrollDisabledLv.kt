package com.self.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ListView
import com.google.android.material.tabs.TabLayout


class ScrollDisabledLv : ListView {

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredHeight = MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, measuredHeight)
        val params: ViewGroup.LayoutParams = layoutParams
        params.height = measuredHeight
    }

    fun requestLayoutForChangedDataset() {

        val listAdapter = this.adapter
        listAdapter?.let { adapter ->
            val itemCount = adapter.count

            var totalHeight = 0
            for (position in 0 until itemCount) {
                val item = adapter.getView(position, null, this)
                item.measure(0, 0)

                totalHeight += item.measuredHeight

                val layoutParams = this.layoutParams
                layoutParams.height = totalHeight
                this.requestLayout()
            }
        }
    }
}