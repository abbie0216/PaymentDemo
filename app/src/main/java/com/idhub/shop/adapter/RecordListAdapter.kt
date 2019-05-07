package com.idhub.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idhub.shop.R
import com.idhub.shop.fragment.RecordListFragment
import com.idhub.shop.model.Order
import kotlinx.android.synthetic.main.item_record.view.*

class RecordListAdapter(
    private val context: Context,
    private val dataList: ArrayList<Order>,
    private val itemClickListener: RecordListFragment.ItemClickListener
) :
    RecyclerView.Adapter<RecordListAdapter.RecordListVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecordListVH(layoutInflater.inflate(R.layout.item_record, parent, false))
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(viewHolder: RecordListVH, position: Int) {
        val data = dataList[position]
        viewHolder.llBottom.visibility = GONE
        viewHolder.tvOrderId.text = "Order#1932011000${data.id}"
        viewHolder.itemView.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
        when (data.status) {
            Order.Status.PACKAGE_PROCESSING -> {
                viewHolder.tvOrderId.setTextColor(context.getColor(R.color.blue_dark_216188))
                viewHolder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_1)
                viewHolder.ivOrderStatus.setBackgroundResource(R.color.blue_light_72afd3)
                viewHolder.llTop.setBackgroundResource(R.color.white)
                viewHolder.itemView.setBackgroundResource(R.color.white)
            }
            Order.Status.PACKAGE_TRANSPORTING -> {
                viewHolder.tvOrderId.setTextColor(context.getColor(R.color.blue_dark_216188))
                viewHolder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_2)
                viewHolder.ivOrderStatus.setBackgroundResource(R.color.blue_light_72afd3)
                viewHolder.llTop.setBackgroundResource(R.color.white)
                viewHolder.itemView.setBackgroundResource(R.color.white)
            }
            Order.Status.PACKAGE_ARRIVED -> {
                viewHolder.tvOrderId.setTextColor(context.getColor(R.color.pink_light_d372bd))
                viewHolder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_3)
                viewHolder.ivOrderStatus.setBackgroundResource(R.color.pink_light_d372bd)
                viewHolder.llTop.setBackgroundResource(R.color.white)
                viewHolder.itemView.setBackgroundResource(R.color.white)
            }
            Order.Status.PACKAGE_DONE -> {
                viewHolder.tvOrderId.setTextColor(context.getColor(R.color.gray_light_9e9e9e))
                viewHolder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_4)
                viewHolder.ivOrderStatus.setBackgroundResource(R.color.gray_light_9e9e9e)
                viewHolder.llTop.setBackgroundResource(R.color.gray_light_e0e0e0)
                viewHolder.itemView.setBackgroundResource(R.color.gray_light_e0e0e0)
            }
        }
    }

    inner class RecordListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llTop: View = itemView.llTop
        val llBottom: View = itemView.llBottom
        val ivOrderStatus: ImageView = itemView.ivOrderStatus
        val tvOrderId: TextView = itemView.tvOrderId
    }
}