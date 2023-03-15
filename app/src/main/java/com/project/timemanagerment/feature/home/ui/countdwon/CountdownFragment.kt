package com.project.timemanagerment.feature.home.ui.countdwon

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.librarycountdown.countdownview.DynamicConfig
import com.project.timemanagerment.R
import com.project.timemanagerment.app.dataStore
import com.project.timemanagerment.app.presentation.MainFragmentDirections
import com.project.timemanagerment.base.constant.ConstantsPreferencesKey
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.databinding.FragmentTabOneBinding
import com.project.timemanagerment.feature.home.ui.countdwon.custom.CountdownNotificationBroadcastReceiver
import com.project.timemanagerment.feature.home.ui.countdwon.recyclerview.CountdownAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CountdownFragment : Fragment() {
    private val viewModel: CountdownViewModel by viewModels()
    lateinit var binding: FragmentTabOneBinding
    private var shortAnimationDuration: Int = 150


    @Inject
    lateinit var adapter: CountdownAdapter
    //  private val ARRANGEMENT_MODE = intPreferencesKey("arrangement_mode")

    lateinit var newDayUpdateTimer: Timer

    companion object {
        val MODE_LIST = 0
        val MODE_GRID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            requireContext().dataStore.data.first()
            // You should also handle IOExceptions here.
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //第二天更新
        timerNewDayUpdate()
        initArrangementMode()
        subScribeUi()
        initListener()
        //设置定时任务
        setAlarmsNotification()
    }

    private fun setAlarmsNotification() {
        createNotificationChannel()
        val alarmIntent = Intent(
            requireContext(),
            CountdownNotificationBroadcastReceiver::class.java
        ).let { intent ->
            PendingIntent.getBroadcast(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        }
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
        }
        val alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "countdown channel"
            val descriptionText = "countdown channel desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("countdown channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun initArrangementMode() {
        val modeFlow = requireContext().dataStore.data.map { preferences ->
            preferences[ConstantsPreferencesKey.countdownArrangementMode]
                ?: ConstantsPreferencesKey.countdownArrangementMode_mode_list
        }
        modeFlow.asLiveData().observe(viewLifecycleOwner) { model ->
            if (model == ConstantsPreferencesKey.countdownArrangementMode_mode_list) {
                showList()
            } else {
                showGrid()
            }
        }
    }

    private fun initListener() {
        binding.fab.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val directions = MainFragmentDirections.navToCountDownEditFragment()
                findNavController().navigate(directions)
            }
        }
    }


    private fun timerNewDayUpdate() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val tomorrowStartTime = beginOfDay(tomorrow)
        newDayUpdateTimer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.proNullAnSetList()
                    viewModel.updateTopCountdown()
                }
            }
        }
        newDayUpdateTimer.scheduleAtFixedRate(
            task,
            tomorrowStartTime,
            CountdownAdapter.CountdownViewHolder.dayTimes.toLong()
        )

    }

    override fun onStop() {
        super.onStop()
        newDayUpdateTimer.cancel()
    }

    fun beginOfDay(date: Date?): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }


    private fun setIntervalUnit(toTargetInterval: Long, binding: FragmentTabOneBinding) {
        if (toTargetInterval / CountdownAdapter.CountdownViewHolder.dayTimes > 0) {
            val builder = DynamicConfig.Builder()
            builder.setShowDay(true)
            builder.setShowHour(false)
            binding.cvTopIntervalTime.dynamicShow(builder.build())
            binding.tvDayOrHour.text = "天"
        } else {
            val builder = DynamicConfig.Builder()
            builder.setShowDay(false)
            builder.setShowHour(true)
            binding.cvTopIntervalTime.dynamicShow(builder.build())
            binding.tvDayOrHour.text = "时"
        }
    }


    private fun subScribeUi() {
        //top countdown
        viewModel.topCountdown.observe(viewLifecycleOwner) { countdownTop ->
            if (countdownTop == null) {
                binding.llCurrentTopCountDown.visibility = View.GONE
            } else {
                binding.llCurrentTopCountDown.visibility = View.VISIBLE
            }

            countdownTop?.let {
                val toTargetInterval = it.targetTime!!.time - System.currentTimeMillis()
                val targetTimeYMD = DateFormatUtil.getFormatYMD(it.targetTime.time)
                val currentTimeYMD = DateFormatUtil.getFormatYMD(Date().time)
                if (targetTimeYMD == currentTimeYMD) {
                    binding.tvTopTitle.text = it.title + "已经到了"
                    val builder = DynamicConfig.Builder()
                    builder.setShowHour(false)
                    builder.setShowDay(true)
                    binding.cvTopIntervalTime.dynamicShow(builder.build())
                    binding.cvTopIntervalTime.start(0, false)
                    binding.cvTopIntervalTime.allShowZero()
                    binding.cvTopIntervalTime.stop()
                    binding.tvDayOrHour.text = "天"
                } else if (toTargetInterval > 0) {
                    binding.tvTopTitle.text = it.title + "还有"
                    setIntervalUnit(toTargetInterval, binding)
                    binding.cvTopIntervalTime.start(toTargetInterval)
                } else {
                    binding.tvTopTitle.text = it.title + "已经"
                    setIntervalUnit(
                        (Math.abs(toTargetInterval) - CountdownAdapter.CountdownViewHolder.dayTimes),
                        binding
                    )
                    binding.cvTopIntervalTime.start(Math.abs(toTargetInterval), false)
                }
                binding.tvTopTargetTime.text = "目标日：" + it.targetTimeString
            }
            binding.llCurrentTopCountDown.setOnClickListener {
                countdownTop?.let { top ->

                    val directions =
                        MainFragmentDirections.navToCountdownDetailFragment(top.countdownId)
                    findNavController().navigate(directions)
                }

            }
        }

        //countdownList
        viewModel.countdownList.observe(viewLifecycleOwner) {
            adapter.submitList(it.toMutableList())
        }

    }


    private fun showList() {
        binding.recyclerView.visibility = View.GONE
        binding.llCurrentTopCountDown.visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        adapter.setLayoutResourceId(R.layout.list_countdown)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }

    private fun showGrid() {
        binding.recyclerView.visibility = View.GONE
        binding.llCurrentTopCountDown.visibility = View.GONE
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.layoutManager = layoutManager
        adapter.setLayoutResourceId(R.layout.list_countdown_grid)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(null)
        }
    }

}

