package com.project.timemanagerment.feature.home.ui.countdwon.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.librarycountdown.countdownview.CountdownView
import com.project.librarycountdown.countdownview.DynamicConfig
import com.project.timemanagerment.R
import com.project.timemanagerment.app.presentation.MainFragmentDirections
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import java.util.*
import javax.inject.Inject

class CountdownAdapter @Inject constructor() :
    ListAdapter<Countdown, CountdownAdapter.CountdownViewHolder>(CountdownDiffCallback()) {
    init {
        //setHasStableIds(true)
    }

    var layoutId: Int = R.layout.list_countdown
     override fun getItemId(position: Int): Long {
        return getItem(position).countdownId
    }

    fun setLayoutResourceId(layoutId: Int) {
        this.layoutId = layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountdownViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
        return CountdownViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountdownViewHolder, position: Int) {
        val countdown = getItem(position)
        (holder as CountdownViewHolder).bind(countdown)
        if (holder.itemView.layoutParams is GridLayoutManager.LayoutParams) {

        } else {
            if (position == itemCount - 1) {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 200
                holder.itemView.layoutParams = params
            } else {
                val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
                params.bottomMargin = 0
                holder.itemView.layoutParams = params
            }
        }
    }

    override fun onViewAttachedToWindow(holder: CountdownViewHolder) {
        (holder as CountdownViewHolder).refreshCountDown()
    }

    override fun onViewDetachedFromWindow(holder: CountdownViewHolder) {
        (holder as CountdownViewHolder).stopCountdown()
    }

    class CountdownViewHolder(val view: View) :
        RecyclerView.ViewHolder(
            view
        ) {
        private lateinit var mCountdown: Countdown
        fun bind(countdown: Countdown?) {
            mCountdown = countdown!!
            val view = itemView
            var haveTargetTime = false
            val tvTargetTime = view.findViewById<TextView?>(R.id.tv_target_time)
            tvTargetTime?.let { targetTime ->
                haveTargetTime = true
            }
            tvTargetTime?.let { targetTime ->
                targetTime.text = "目标:" + countdown.targetTimeString
            }
            val tvCountdownType = view.findViewById<TextView>(R.id.tv_work_type)
            val tvCountdownTitle = view.findViewById<TextView>(R.id.tv_countdown_title)
            val tvDayOrHour = view.findViewById<TextView>(R.id.tv_day_or_hour)
            val cvIntervalTime = view.findViewById<CountdownView>(R.id.cv_interval_time)
            view.findViewById<ConstraintLayout>(R.id.cl_item).setOnClickListener {
                val directions =
                    MainFragmentDirections.navToCountdownDetailFragment(countdownId = countdown.countdownId)
                it.findNavController().navigate(directions)
            }
            countdown.let {
                //countdownType
                tvCountdownType?.let { type ->
                    if (it.countdownType == Countdown.countdownUrgency) {
                        tvCountdownType.visibility = View.VISIBLE
                    } else {
                        tvCountdownType.visibility = View.GONE
                    }
                }
                //date
                val toTargetInterval = it.targetTime!!.time - System.currentTimeMillis()
                val targetTimeYMD = DateFormatUtil.getFormatYMD(it.targetTime.time)
                val currentTimeYMD = DateFormatUtil.getFormatYMD(Date().time)
                //cvIntervalTime.allShowZero()
                if (targetTimeYMD == currentTimeYMD) {
                    tvCountdownTitle.text = it.title + "已经到了"
                    tvDayOrHour.text = "天"
                    tvDayOrHour.setBackgroundResource(R.drawable.bg_countdowm_item_day)
                    val builder = DynamicConfig.Builder()
                    builder.setShowDay(true)
                    builder.setShowHour(false)
                    cvIntervalTime.dynamicShow(builder.build())
                    if (haveTargetTime) {
                        tvCountdownTitle.setBackgroundResource(R.color.countdown_item_color_is_now)
                    } else {
                        cvIntervalTime.setBackgroundResource(R.color.countdown_item_color_is_now)
                    }
                    cvIntervalTime.start(0, false)
                } else if (toTargetInterval > 0) {
                    tvCountdownTitle.text = it.title + "还有"
                    setIntervalUnit(toTargetInterval, view)
                    if (haveTargetTime) {
                        tvCountdownTitle.setBackgroundResource(R.color.countdown_item_color_have_interval)
                    } else {
                        cvIntervalTime.setBackgroundResource(R.color.countdown_item_color_have_interval)
                    }
                    cvIntervalTime.start(toTargetInterval)
                } else {
                    tvCountdownTitle.text = it.title + "已经"
                    setIntervalUnit((Math.abs(toTargetInterval) - dayTimes), view)
                    if (haveTargetTime) {
                        tvCountdownTitle.setBackgroundResource(R.color.countdown_item_color_already)
                    } else {
                        cvIntervalTime.setBackgroundResource(R.color.countdown_item_color_already)
                    }
                    cvIntervalTime.start(Math.abs(toTargetInterval), false)
                }
            }

        }

        private fun setIntervalUnit(toTargetInterval: Long, view: View) {
            val tvDayOrHour = view.findViewById<TextView>(R.id.tv_day_or_hour)
            val cvIntervalTime = view.findViewById<CountdownView>(R.id.cv_interval_time)
            if (toTargetInterval / dayTimes > 0) {
                if (toTargetInterval / dayTimes >= 100) {
                    cvIntervalTime.setNeedLayout(false)
                } else {
                    cvIntervalTime.setNeedLayout(false)
                }
                val builder = DynamicConfig.Builder()
                builder.setShowDay(true)
                builder.setShowHour(false)
                cvIntervalTime.dynamicShow(builder.build())
                tvDayOrHour.text = "天"
                tvDayOrHour.setBackgroundResource(R.drawable.bg_countdowm_item_day)
            } else {
                val builder = DynamicConfig.Builder()
                builder.setShowDay(false)
                builder.setShowHour(true)
                cvIntervalTime.dynamicShow(builder.build())
                tvDayOrHour.text = "时"
                tvDayOrHour.setBackgroundResource(R.drawable.bg_countdowm_item_hour)
            }

        }

        fun stopCountdown() {
            val cvIntervalTime = itemView.findViewById<CountdownView>(R.id.cv_interval_time)
            cvIntervalTime.stop()
        }

        fun refreshCountDown() {
            val cvIntervalTime = itemView.findViewById<CountdownView>(R.id.cv_interval_time)
            val interval = mCountdown!!.targetTime!!.time - System.currentTimeMillis()
            if (interval > 0) {
                cvIntervalTime.start(interval)
            } else {
                cvIntervalTime.start(Math.abs(interval), false)
            }
        }

        companion object {
            const val dayTimes = 60 * 60 * 24 * 1000
        }
    }

    class CountdownDiffCallback : DiffUtil.ItemCallback<Countdown>() {
        override fun areItemsTheSame(oldItem: Countdown, newItem: Countdown): Boolean {
            return oldItem.countdownId == newItem.countdownId
        }

        override fun areContentsTheSame(oldItem: Countdown, newItem: Countdown): Boolean {
            return oldItem == newItem
        }

    }
}