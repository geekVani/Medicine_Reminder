package com.example.medicinereminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicinereminder.R
import com.example.medicinereminder.model.roomDB.Item

class ItemRVAdapter(
    val deleteInterface: ItemDeleteInterface): RecyclerView.Adapter<ItemRVAdapter.ViewHolder>() {

    // creating a variable for all items list.
    private val allItems = ArrayList<Item>()

    // Add a function to update the data in the adapter.
    fun updateData(items: List<Item>) {
        allItems.clear()
        allItems.addAll(items)
        notifyDataSetChanged()
    }

    // Create a ViewHolder to hold your item views
    /** *******************************************************************
     * In the FormViewHolder class, we initialize the views in the item layout
     * using findViewById and assign them to respective properties of the ViewHolder.
     * This allows us to access and modify these views efficiently within the bind
     * method.
     * *********************************************************************/
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // create and initiale variables which are added in layout file
        val noteTV = itemView.findViewById<TextView>(R.id.tvName)!!
        val descTV = itemView.findViewById<TextView>(R.id.tvDesc)!!
        val startTV = itemView.findViewById<TextView>(R.id.tvStart)!!
        val endTV = itemView.findViewById<TextView>(R.id.tvEnd)!!
        val timeTV = itemView.findViewById<TextView>(R.id.tvTime)!!
        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating layout file for each item of recycler view.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.medicine_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to item of recycler view.
        holder.noteTV.text = allItems[position].medicineName
        holder.descTV.text = allItems[position].pillDescription
        holder.startTV.text = "From: ${allItems[position].fromDate }"
        holder.endTV.text = "Till: ${allItems[position].toDate}"
        holder.timeTV.text = "Time : ${allItems[position].time}"
        // adding click listener to delete image view icon.
        holder.deleteIV.setOnClickListener {
            // calling a item click interface and passing a position to it.
            deleteInterface.onDeleteIconClick(allItems[position])
        }
    }

    override fun getItemCount(): Int {
        // returning list size
        return allItems.size
    }
}

interface ItemDeleteInterface {
    // action on delete image view
    fun onDeleteIconClick(item: Item)
}

