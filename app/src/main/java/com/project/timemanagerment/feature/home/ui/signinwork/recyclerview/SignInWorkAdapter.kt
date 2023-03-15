package com.project.timemanagerment.feature.home.ui.signinwork.recyclerview


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.textfield.TextInputEditText
import com.project.timemanagerment.R
import com.project.timemanagerment.app.presentation.MainFragmentDirections
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.base.util.DateWeek
import com.project.timemanagerment.base.util.SignInIconList
import com.project.timemanagerment.databinding.ListSignInGridBinding
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWorkWithDates
import com.project.timemanagerment.feature.home.ui.signinwork.recyclerview.callback.SignInWorkAdapterCallback
import java.util.*

class SignInWorkAdapter :
    ListAdapter<SignInWorkWithDates, RecyclerView.ViewHolder>(SignInWorkDiffCallback()) {
    lateinit var callback: SignInWorkAdapterCallback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ListSignInGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SignInWorkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as SignInWorkViewHolder).bind(item, callback)
    }

    class SignInWorkViewHolder(private val binding: ListSignInGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SignInWorkWithDates?, callback: SignInWorkAdapterCallback) {
            binding.clParent.setOnClickListener { view ->
                val signInWork = item!!.signInWork
                val directions = signInWork.let { it1 ->
                    MainFragmentDirections.navToSignInDateFragment(
                        it1.signInWorkId
                    )
                }
                if (directions != null) {
                    binding.root.findNavController().navigate(directions)
                }
                /*val signInWork = item!!.signInWork
                if (toDayIsSignIn(item)) {
                    if (signInWork.isShowRemind) {
                        showRemindDialog(item, callback, true)
                    } else {
                        callback.deleteSignInDate(
                            signInWork.finalSignInDateId!!,
                            signInWork.signInWorkId
                        )
                    }
                } else {
                    val calendar = Calendar.getInstance()
                    calendar.time = Date()
                    calendar[Calendar.HOUR_OF_DAY] = 0
                    calendar[Calendar.MINUTE] = 0
                    calendar[Calendar.SECOND] = 0
                    calendar[Calendar.MILLISECOND] = 0
                    callback.insertSignInDate(
                        SignInDate(
                            signInWork.signInWorkId,
                            calendar.time,
                            getDateStringByYMD(
                                calendar.year,
                                calendar.month,
                                calendar.dayOfMonth
                            ),
                            "",
                            0L,
                            Date()
                        )
                    )
                    if (signInWork.isShowRemind) {
                        showRemindDialog(item, callback)
                    }
                }*/
            }
            //view
            binding.clParent.setOnLongClickListener {
                //showDialog
                //showRemindDialog(item, callback)
                return@setOnLongClickListener true
            }
            //initView
            binding.tvName.text = item!!.signInWork.name
            //image
            val iconDraw = ContextCompat.getDrawable(
                binding.root.context!!,
                SignInIconList.getSignInIconByIndex(item.signInWork.iconIndex).iconId
            )
            binding.ivWorkIcon.setImageDrawable(iconDraw)
            if (toDayIsSignIn(item)) {
                binding.ivWorkIcon.setColorFilter(Color.parseColor("#21f52e"))
            } else {
                binding.ivWorkIcon.setColorFilter(Color.parseColor("#A0A0A0"))
            }
            //count
            binding.tvDateCount.text = item.signDates.size.toString()
        }

        private fun toDayIsSignIn(item: SignInWorkWithDates?): Boolean {
            val signInWork = item!!.signInWork
            val nowDateString = DateFormatUtil.getFormatYMD(Date().time)
            val signInDateString =
                if (signInWork.finalSignInDateTime != null) DateFormatUtil.getFormatYMD(
                    signInWork.finalSignInDateTime!!.time
                ) else ""
            return nowDateString == signInDateString
        }

        private fun getDateStringByYMD(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
            val gregorianCalendar = GregorianCalendar(year, monthOfYear, dayOfMonth - 1)
            val dayOfWeek: Int = gregorianCalendar.get(java.util.Calendar.DAY_OF_WEEK)
            val dayOfWeekString = DateWeek.getWeekStringByWeekIndex(dayOfWeek)
            val realMoth = monthOfYear + 1
            val dateString = "$year-$realMoth-$dayOfMonth $dayOfWeekString"
            // getDateTimeByDateString(dateString)
            return dateString
        }

        private fun showRemindDialog(
            item: SignInWorkWithDates?,
            callback: SignInWorkAdapterCallback,
            hasNegative: Boolean = false
        ) {
            val dialog = MaterialDialog(binding.root.context).show {
                customView(R.layout.dialog_sign_in_show, noVerticalPadding = true)
            }
            val signInWork = item!!.signInWork
            //binding
            val customView = dialog.getCustomView()
            val ivWorkIcon = customView.findViewById<ImageView>(R.id.iv_work_icon)
            val tvWorkName = customView.findViewById<TextView>(R.id.tv_work_name)
            val clSignInHistory = customView.findViewById<ConstraintLayout>(R.id.cl_sign_in_history)
            val etDateIntroduction =
                customView.findViewById<TextInputEditText>(R.id.et_date_introduction)
            val clRb = customView.findViewById<ConstraintLayout>(R.id.cl_rb)
            val rbShowRemind = customView.findViewById<RadioButton>(R.id.rb_show_remind)
            val tvNegative = customView.findViewById<TextView>(R.id.tv_negative)
            val tvPositive = customView.findViewById<TextView>(R.id.tv_positive)
            var currentShowDate: SignInDate? = null
            //hasNegative
            if (hasNegative) {
                tvNegative.visibility = View.VISIBLE
                //setEtIntroduction
                val dateList = item.signDates
                val calendar = Calendar.getInstance()
                calendar.time = Date()
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.SECOND] = 0
                val toadyDateString =
                    getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
                for (element in dateList) {
                    if (element.dateTimeString == toadyDateString) {
                        etDateIntroduction.setText(element.introduction.toString())
                        currentShowDate = element
                        break
                    }
                }
            } else {
                tvNegative.visibility = View.GONE
            }
            //
            val iconDraw = ContextCompat.getDrawable(
                binding.root.context!!,
                SignInIconList.getSignInIconByIndex(signInWork.iconIndex).iconId
            )
            ivWorkIcon.setImageDrawable(iconDraw)
            tvWorkName.text = signInWork.name
            clSignInHistory.setOnClickListener { view ->
                val directions = signInWork.let { it1 ->
                    MainFragmentDirections.navToSignInDateFragment(
                        it1.signInWorkId
                    )
                }
                if (directions != null) {
                    binding.root.findNavController().navigate(directions)
                }
                dialog.dismiss()
            }
            rbShowRemind.isClickable = false
            rbShowRemind.isChecked = signInWork.isShowRemind
            clRb.setOnClickListener {
                rbShowRemind.isChecked = !rbShowRemind.isChecked
            }
            tvNegative.setOnClickListener {
                //deleteDate 知道要取消的对象
                callback.deleteSignInDate(
                    signInWork.finalSignInDateId!!,
                    signInWork.signInWorkId
                )
                dialog.dismiss()
            }
            tvPositive.setOnClickListener {
                if (hasNegative) {
                    //更新，并且知道本日期对象
                    currentShowDate!!.introduction = etDateIntroduction.text.toString()
                    callback.updateSignInDate(
                        currentShowDate
                    )
                } else {
                    //更新introduction 本对象查找最新的更新
                    /* callback.updateSignInDateIntroduction(
                         signInWorkId = signInWork
                             .signInWorkId, introduction = etDateIntroduction.text.toString()
                     )*/
                }
                //update work showMind status
                signInWork.isShowRemind = rbShowRemind.isChecked
                // callback.updateSignInWork(signInWork)
                dialog.dismiss()
            }

        }
    }
}

class SignInWorkDiffCallback : DiffUtil.ItemCallback<SignInWorkWithDates>() {
    override fun areItemsTheSame(
        oldItem: SignInWorkWithDates,
        newItem: SignInWorkWithDates
    ): Boolean {
        return oldItem.signInWork.signInWorkId == newItem.signInWork.signInWorkId
    }

    override fun areContentsTheSame(
        oldItem: SignInWorkWithDates,
        newItem: SignInWorkWithDates
    ): Boolean {
        return oldItem.signInWork.finalSignInDateId == newItem.signInWork.finalSignInDateId
                && oldItem.signInWork.isShowRemind == newItem.signInWork.isShowRemind
                && oldItem.signDates.size == newItem.signDates.size
                && oldItem.signInWork.name == newItem.signInWork.name
                && oldItem.signInWork.iconIndex == newItem.signInWork.iconIndex
    }

}