package com.self.app.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.self.app.R
import com.self.app.databinding.ItemTokenInfoBinding
import com.self.app.databinding.TokenGroupItemBinding
import com.self.app.databinding.TokenListBinding
import com.zml.wallet.TokenInfo


class TokenGroupView : ConstraintLayout {

    public lateinit var binding: TokenListBinding
    private lateinit var bookItemBinding: ItemTokenInfoBinding
    private lateinit var adapter: BookAdapter

    var tokenInfos: List<TokenInfo> = emptyList()
        set(value) {
            field = value
            onItemsUpdated()
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        binding = TokenListBinding.inflate(LayoutInflater.from(context), this, true)
        adapter = BookAdapter(context)
        binding.bookDetailsList.adapter = adapter
    }

    private fun onItemsUpdated() {
        adapter.notifyDataSetChanged()
        binding.bookDetailsList.requestLayoutForChangedDataset()
    }

    inner class BookAdapter(private val context: Context) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val tokenInfo: TokenInfo = tokenInfos[position]
            var view: View? = convertView

            if (view == null) {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.item_token_info, parent, false)
                bookItemBinding = ItemTokenInfoBinding.bind(view)
                view.tag = bookItemBinding
            } else {
                bookItemBinding = view.tag as ItemTokenInfoBinding
            }

            bookItemBinding.apply {
                coinName.text = tokenInfo.name
                coinPrice.text = tokenInfo.curPrice
                coinRange.text = "$tokenInfo.range%"

            }

            return bookItemBinding.root
        }

        override fun getItem(position: Int): Any {
            return tokenInfos[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return tokenInfos.size
        }

        override fun isEnabled(position: Int): Boolean {
            return false
        }
    }
}