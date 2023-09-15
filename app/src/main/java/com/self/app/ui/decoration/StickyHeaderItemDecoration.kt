package com.self.app.ui.decoration

import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.self.app.databinding.StickyHeaderBinding
import com.self.app.getPixels
import com.self.app.ui.adapter.HomeAdapter
import com.self.app.ui.other.StickyHeaderHandler

class StickyHeaderItemDecoration(private val stickyHeaderHandler: StickyHeaderHandler):ItemDecoration() {
    // Header View
    private var mCurrentHeaderView: View? = null


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChildView = parent.findChildViewUnder(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat() /*+ (currentHeader?.second?.itemView?.height ?: 0 )*/
        ) ?: return
        val topChildViewInAdapterPos = parent.getChildAdapterPosition(topChildView)
        if (topChildViewInAdapterPos == RecyclerView.NO_POSITION) {
            return
        }

//        //获取正在显示的列表中的顶层视图
//        val topChildView: View = parent.getChildAt(0)?:return
//
//        //顶层view在adapter里的索引
//        val topChildViewInAdapterPos = parent.getChildAdapterPosition(topChildView)
//
//        if (topChildViewInAdapterPos == RecyclerView.NO_POSITION) return

        //获取HeaderPosition的索引
        val prevHeaderInAdapterPos = stickyHeaderHandler.getHeaderPosition(topChildViewInAdapterPos)


        if(prevHeaderInAdapterPos == StickyHeaderHandler.HEADER_POSITION_NOT_FOUND) return

        mCurrentHeaderView = getHeaderView(topChildViewInAdapterPos, parent)

        fixLayoutSize(parent, mCurrentHeaderView)

        //获取当前Header的Bottom Position(与父View的相对距离)
        val contactPoint = (mCurrentHeaderView!!.bottom)

        //
        val nextCell = getNextCellToHeader(parent, contactPoint) ?: return

        val nextCellPosition =parent.getChildAdapterPosition(nextCell)

        if(nextCellPosition == StickyHeaderHandler.HEADER_POSITION_NOT_FOUND) {
            return
        }

        /**
         * header有没有被滑走
         */
        if (stickyHeaderHandler.isHeader(nextCellPosition)) {
            moveHeader(c, mCurrentHeaderView, parent.layoutManager?.findViewByPosition(prevHeaderInAdapterPos))
            return
        }

        //直接画在顶部
        drawHeader(c, mCurrentHeaderView)
    }

    private fun getHeaderView(itemPosition: Int, parent: RecyclerView): View? {
        val headerPosition = stickyHeaderHandler.getHeaderPosition(itemPosition)
        val layoutResId = stickyHeaderHandler.getHeaderLayout(headerPosition)
        val headerView = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        stickyHeaderHandler.bindHeaderData(headerView, headerPosition)
        return headerView
    }


    private fun fixLayoutSize(parent: ViewGroup, headerView: View?) {
        headerView?:return


        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)


        val headerWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            headerView.layoutParams.width
        )
        val headerHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            headerView.layoutParams.height
        )
        headerView.measure(headerWidthSpec, headerHeightSpec)
        headerView.layout(0, 0, headerView.measuredWidth, headerView.measuredHeight)
    }


    /**
     * contractPoint：当前header view的bottom
     * 离header view最近的下一个item view
     */
    private fun getNextCellToHeader(parent: RecyclerView, contactPoint: Int): View? {
        var nextView: View? = null
        for (index in 0 until parent.childCount) {
            val child = parent.getChildAt(index)
            if (child.bottom+contactPoint > contactPoint) {
                if (child.top <= contactPoint) {
                    nextView = child
                    break
                }
            }
        }
        return nextView
    }

    private fun moveHeader(c: Canvas, currentHeader: View?, curHeader: View?) {
        currentHeader?:return

        c.save()
        c.translate(0F, (curHeader?.bottom!!).toFloat())//跟随adapter里面的header的底部进行绘制
        currentHeader.draw(c)
        c.restore()
    }

    /**
     * 把header固定绘制在顶部
     */
    private fun drawHeader(c: Canvas, header: View?) {
        c.save()
        c.translate(0F, 0F)
        header!!.draw(c)
        c.restore()
    }

}

