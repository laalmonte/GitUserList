package com.directpl.gituserlist.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.directpl.gituserlist.R
import com.directpl.gituserlist.extension.loadUrls
import com.directpl.gituserlist.model.UserObject
import kotlinx.android.synthetic.main.view_item.view.*
import kotlin.properties.Delegates

class ItemAdapter(private val onItemSelectCallback: OnItemSelectCallback) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private var oldTrackList = mutableListOf<UserObject>()
    private lateinit var mContext: Context

    private var trackList: List<UserObject> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item, parent, false))
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(trackList[position], position)

    }

    fun updateContext(contextParam: Context){
        mContext = contextParam
    }

    fun updateUserList(newDataSet: MutableList<UserObject>) {
        oldTrackList.clear()
        oldTrackList.addAll(newDataSet)
        trackList = emptyList()
        trackList = oldTrackList
        notifyDataSetChanged()
    }

    fun updateEmptyUserList() {
        oldTrackList.clear()
        trackList = emptyList()
        trackList = oldTrackList
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(obj: UserObject, position: Int) {
            var name    = ""
            var type    = ""
            obj.login?.let { name = "UserName: $it" }
            obj.type.let { type = "Type: $it" }
            obj.note.let {
                if (it == ""){
                    itemView.fileLayout.visibility = View.INVISIBLE
                } else {
                    itemView.fileLayout.visibility = View.VISIBLE
                }
            }

            itemView.tvName.text    = name
            itemView.tvType.text    = type
            itemView.ivArtwork.loadUrls(mContext, obj.avatarUrl)

            itemView.userLayout.setOnClickListener { onItemSelectCallback.onSelectItem(obj)}
        }
    }

    interface OnItemSelectCallback {
        fun onSelectItem(userObject: UserObject)
    }

}