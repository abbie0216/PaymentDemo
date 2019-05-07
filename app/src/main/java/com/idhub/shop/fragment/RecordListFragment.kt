package com.idhub.shop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idhub.shop.R
import com.idhub.shop.adapter.RecordListAdapter
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.customview.SpaceItemDecoration
import com.idhub.shop.model.Order
import com.idhub.shop.model.Product
import com.idhub.shop.util.SPUtil.Companion.ADDRESS
import com.idhub.shop.util.SPUtil.Companion.MOBILE
import com.idhub.shop.util.SPUtil.Companion.NAME
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.fragment_record_list.*

class RecordListFragment : BaseFragment() {
    private var data = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_record, TopBarBtnType.BACK, TopBarBtnType.HINT)
    }

    override fun onResume() {
        super.onResume()

        setData()

        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecord.layoutManager = layoutManager
        rvRecord.addItemDecoration(SpaceItemDecoration(10))
        rvRecord.adapter = RecordListAdapter(context!!, data, object : ItemClickListener {
            override fun onItemClick(position: Int) {
                mainCallback.onRecordDetail(data[position])
            }
        })
    }

    private fun setData() {
        // FIXME Test data
        data.clear()
        data.add(
            Order(
                1,
                Product(1, "AXB49 Happy Days CD", 199),
                spUtil.getString(NAME),
                spUtil.getString(MOBILE),
                spUtil.getString(ADDRESS),
                Order.Status.PACKAGE_PROCESSING
            )
        )
        data.add(
            Order(
                2,
                Product(2, "AXB49 T-shirt (A)", 59),
                spUtil.getString(NAME),
                spUtil.getString(MOBILE),
                spUtil.getString(ADDRESS),
                Order.Status.PACKAGE_TRANSPORTING
            )
        )
        data.add(
            Order(
                3,
                Product(3, "AXB49 T-shirt (B)", 59),
                spUtil.getString(NAME),
                spUtil.getString(MOBILE),
                spUtil.getString(ADDRESS),
                Order.Status.PACKAGE_ARRIVED
            )
        )
        data.add(
            Order(
                4,
                Product(4, "AXB49 Mug (B)", 49),
                spUtil.getString(NAME),
                spUtil.getString(MOBILE),
                spUtil.getString(ADDRESS),
                Order.Status.PACKAGE_DONE
            )
        )
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}
