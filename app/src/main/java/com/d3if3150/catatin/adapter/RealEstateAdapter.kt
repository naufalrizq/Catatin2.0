package com.d3if3150.catatin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.d3if3150.catatin.R
import com.d3if3150.catatin.model.RealEstate

class RealEstateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val diffCallBack = object : DiffUtil.ItemCallback<RealEstate>() {
        override fun areItemsTheSame(oldItem: RealEstate, newItem: RealEstate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RealEstate, newItem: RealEstate): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.api_fetch_list_item, parent, false),
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecyclerViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class RecyclerViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RealEstate) = with(itemView) {
            val realEstateId = itemView.findViewById<TextView>(R.id.tv_id)
            val realEstatePrice = itemView.findViewById<TextView>(R.id.tv_price)
            val realEstateType = itemView.findViewById<TextView>(R.id.tv_type)
            val realEstateIv = itemView.findViewById<ImageView>(R.id.real_estate_iv)
            val realEstateCard = itemView.findViewById<CardView>(R.id.real_estate_cardView)

            realEstateId.text = item.id
            realEstatePrice.text = item.price.toString()
            realEstateType.text = item.type
            Glide.with(context).load(item.img_src).into(realEstateIv)

        }

    }
}