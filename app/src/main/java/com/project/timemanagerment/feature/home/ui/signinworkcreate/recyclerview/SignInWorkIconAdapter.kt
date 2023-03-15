package com.project.timemanagerment.feature.home.ui.signinworkcreate.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.timemanagerment.base.util.SignInIcon
import com.project.timemanagerment.base.util.SignInIconList
import com.project.timemanagerment.databinding.ListSignInWorkIconBinding

class SignInWorkIconAdapter : ListAdapter<SignInIcon, RecyclerView.ViewHolder>(
    SignInIconDiffCallback()
) {
    var oldSelectPosition = 0
    var currentSelectIconIndex = 1;
    lateinit var selectIconIndexCallback: (Int) -> Unit
    fun setCurrentSelectIconIndexAndUpdate(index: Int) {
        currentSelectIconIndex = index
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ListSignInWorkIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignInWorkIconViewHolder(binding)
    }

    /* override fun getItemId(position: Int): Long {
         return super.getItemId(position)
     }*/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //  setHasStableIds(true)
        val item = getItem(position)
        (holder as SignInWorkIconViewHolder).bind(
            item,
            currentSelectIconIndex,
            selectIconIndexCallback,
            this
        )
    }

    class SignInWorkIconViewHolder(private val binding: ListSignInWorkIconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(
            item: SignInIcon?,
            currentSelectIconIndex: Int,
            selectIconIndexCallback: (Int) -> Unit,
            signInWorkIconAdapter: SignInWorkIconAdapter
        ) {
            //binding.clSon.isSelected = item!!.iconIndex == currentSelectIconIndex
            if (item!!.iconIndex == currentSelectIconIndex) {
                binding.clSon.isSelected = true
                signInWorkIconAdapter.oldSelectPosition = bindingAdapterPosition
            } else {
                binding.clSon.isSelected = false
            }
            val iconDraw = ContextCompat.getDrawable(
                binding.root.context,
                SignInIconList.getSignInIconByIndex(item.iconIndex).iconId
            )
            binding.ivWorkIcon.setImageDrawable(iconDraw)
            binding.clParent.setOnClickListener {
                signInWorkIconAdapter.currentSelectIconIndex = item.iconIndex
                selectIconIndexCallback(item.iconIndex)
                signInWorkIconAdapter.notifyItemChanged(signInWorkIconAdapter.oldSelectPosition)
                signInWorkIconAdapter.notifyItemChanged(bindingAdapterPosition)
            }

        }

    }


    class SignInIconDiffCallback : DiffUtil.ItemCallback<SignInIcon>() {
        override fun areItemsTheSame(oldItem: SignInIcon, newItem: SignInIcon): Boolean {
            return oldItem.iconIndex == newItem.iconIndex
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: SignInIcon, newItem: SignInIcon): Boolean {
            return oldItem == newItem
        }

    }
}