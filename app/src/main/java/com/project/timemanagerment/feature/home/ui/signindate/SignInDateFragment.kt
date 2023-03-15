package com.project.timemanagerment.feature.home.ui.signindate


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.MaterialDialog
import com.project.librarycalendarview.calendarview.Calendar
import com.project.librarycalendarview.calendarview.CalendarView
import com.project.timemanagerment.R
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.base.util.DateWeek
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.base.util.inputNoAutoKeyboard
import com.project.timemanagerment.databinding.FragmentSignInDateBinding
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.ui.countdwon.recyclerview.CountdownAdapter
import com.project.timemanagerment.feature.home.ui.signindate.calendar.CalendarBaseFragment
import com.project.timemanagerment.feature.home.ui.signindate.calendar.FullMonthView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class SignInDateFragment : CalendarBaseFragment() {
    lateinit var binding: FragmentSignInDateBinding
    val viewModel: SignInDateViewModel by viewModels()
    lateinit var newDayUpdateTimer: Timer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCalendarListener()
        initWorkData()
        initDateStats()
        initContinuesStats()
        initToolbar()
        subscribeUiDateScheme()
        binding.flCurrent.setOnClickListener {
            binding.calendarView.scrollToCurrent()
        }
        timerNewDayUpdate()
    }

    private fun subscribeUiDateScheme() {
        viewModel.signInDateList.observe(viewLifecycleOwner) {
            val map: MutableMap<String, Calendar> = mutableMapOf()
            val calendar = java.util.Calendar.getInstance()
            for (element in it) {
                calendar.time = element.dateTime
                val scheme = getSchemeCalendar(
                    calendar.year,
                    calendar.month + 1,
                    calendar.dayOfMonth,
                    Color.WHITE,
                    "签"
                )
                map[scheme.toString()] = scheme!!
            }
            binding.calendarView.setSchemeDate(map)
            /*更新数据*/
        }
    }

    private fun timerNewDayUpdate() {
        val calendar = java.util.Calendar.getInstance()
        calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val tomorrowStartTime = beginOfDay(tomorrow)
        newDayUpdateTimer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                viewLifecycleOwner.lifecycleScope.launch {
                    //viewModel.copyUpdateCurrentDates()
                    //work updateCalendar
                    viewModel.copyUpdateCurrentWork()
                    binding.calendarView.updateCurrentDate()

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
        val cal = java.util.Calendar.getInstance()
        cal.time = date
        cal[java.util.Calendar.HOUR_OF_DAY] = 0
        cal[java.util.Calendar.MINUTE] = 0
        cal[java.util.Calendar.SECOND] = 0
        cal[java.util.Calendar.MILLISECOND] = 0
        return cal.time
    }

    private fun initToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_action_delete -> {
                    MaterialDialog(requireContext()).show {
                        title(null, "删除任务")
                        message(null, "确定删除打卡任务?")
                        positiveButton(null, "确定") {
                            viewModel.deleteCurrentSignInWork()
                            findNavController().navigateUp()
                        }
                        negativeButton(null, "取消")
                    }
                }
                R.id.menu_action_edit -> {
                    val directions =
                        SignInDateFragmentDirections.navToSignInWorkEditFragment(viewModel.parentWorkId!!)
                    findNavController().navigate(directions)
                }
                R.id.menu_action_to_top -> {
                    viewModel.itemToTop()
                    ToastUtilsDraK.showMsgCenter("置顶成功")

                }
            }
            return@setOnMenuItemClickListener true
        }
        viewModel.signInWork.observe(viewLifecycleOwner) {
            binding.toolbar.title = it!!.name
        }

    }

    private fun initContinuesStats() {
        viewModel.signInDateByDateTimeAsc.asLiveData().observe(viewLifecycleOwner) { dates ->
            val oneDayTimeLong = 24 * 60 * 60 * 1000L
            val datesMap = mutableMapOf<Long, MutableList<SignInDate>>()
            for ((index, value) in dates.withIndex()) {
                if (index + 1 <= dates.size - 1) {
                    if ((dates[index + 1].dateTime.time - dates[index].dateTime.time) == oneDayTimeLong) {
                        if (datesMap.containsKey(index.toLong())) {
                            val dateList = datesMap[index.toLong()]
                            dateList!!.add(dates[index + 1])
                            datesMap[index + 1L] = dateList
                            datesMap.remove(index.toLong())
                        } else {
                            datesMap[index + 1L] =
                                mutableListOf(dates[index], dates[index + 1])
                        }
                    }
                }
            }

            val maxRectContinues = datesMap.maxByOrNull { it.key }
            if (maxRectContinues == null) {
                binding.tvRecentContinuesSignInDate.text = "最近连续打卡:" + if (dates.size > 0) 1 else 0
            } else {
                if (maxRectContinues.key == (dates.size - 1).toLong()) {
                    binding.tvRecentContinuesSignInDate.text =
                        "最近连续打卡:" + maxRectContinues.value.size
                } else {
                    binding.tvRecentContinuesSignInDate.text = "最近连续打卡:" + 1
                }
            }
            val maxContinues = datesMap.maxByOrNull { it.value.size }
            if (maxContinues == null) {
                binding.tvMaxContinuesSignInDate.text = "最大连续打卡:" + if (dates.size > 0) 1 else 0

            } else {
                binding.tvMaxContinuesSignInDate.text = "最大连续打卡:" + maxContinues.value.size
            }
        }
    }

    private fun initDateStats() {
        viewLifecycleOwner.lifecycleScope.launch {
            val calendarToday = java.util.Calendar.getInstance()
            calendarToday.time = Date()
            calendarToday[java.util.Calendar.HOUR_OF_DAY] = 0
            calendarToday[java.util.Calendar.MINUTE] = 0
            calendarToday[java.util.Calendar.SECOND] = 0
            calendarToday[java.util.Calendar.MILLISECOND] = 0
            val signInDateAlready = viewModel.getSignInDateByParentAndDateTime(calendarToday.time)
            showDateIntroduction(signInDateAlready)
        }

        viewModel.sinInDateSize.asLiveData().observe(viewLifecycleOwner) {
            binding.tvAllSignInDate.text = "累计打卡:" + it
        }
    }

    private fun initWorkData() {
        viewModel.signInWork.observe(viewLifecycleOwner) { signInWork ->
            signInWork?.let {
                //setRange
                val currentDayCalendar = java.util.Calendar.getInstance()
                currentDayCalendar.time = Date()
                currentDayCalendar[java.util.Calendar.HOUR_OF_DAY] = 0
                currentDayCalendar[java.util.Calendar.MINUTE] = 0
                currentDayCalendar[java.util.Calendar.SECOND] = 0
                currentDayCalendar[java.util.Calendar.MILLISECOND] = 0
                if (it.endTimeActive) {
                    val dateEnd =
                        if (currentDayCalendar.time.time >= it.endTime!!.time) it.endTime else currentDayCalendar.time
                    currentDayCalendar.time = dateEnd
                }
                binding.calendarView.setStartAndEndDate(it.startTime, currentDayCalendar.time)
                //setBottomData
                if (it.endTimeActive) {
                    binding.tvStartTimeOrWithEnnTime.text =
                        "日期:" + DateFormatUtil.getFormatYMD(it.startTime.time) + " ~ " + DateFormatUtil.getFormatYMD(
                            it.endTime!!.time
                        )

                } else {
                    binding.tvStartTimeOrWithEnnTime.text = "开始日期:" + it.startTimeString
                }
            }
            //统计更新
            viewModel.updateStats()
        }
    }

    private fun getDateStringByYMD(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        val gregorianCalendar = GregorianCalendar(year, monthOfYear, dayOfMonth - 1)
        val dayOfWeek: Int = gregorianCalendar.get(java.util.Calendar.DAY_OF_WEEK)
        val dayOfWeekString = DateWeek.getWeekStringByWeekIndex(dayOfWeek)
        val realMoth = monthOfYear + 1
        val dateString = "$year-$realMoth-$dayOfMonth $dayOfWeekString"
        return dateString
    }

    private fun getDateTimeByDateString(dateString: String): Date {
        val dateStringSpit = dateString.split("-")
        val year = dateStringSpit[0].trim()
        val month = dateStringSpit[1].trim()
        val day = dateStringSpit[2].substring(0, 2).trim()
        return GregorianCalendar(year.toInt(), (month.toInt() - 1), day.toInt()).time
    }

    private fun initCalendarListener() {
        setStatusBarDarkMode()
        binding.calendarView.setOnCalendarSelectListener(object :
            CalendarView.OnCalendarSelectListener {
            override fun onCalendarOutOfRange(calendar: Calendar?) {

            }

            override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
                if (calendar != null) {
                    val date = GregorianCalendar(
                        calendar
                        !!.year, (calendar.month.toInt() - 1), calendar.day.toInt()
                    ).time
                    viewLifecycleOwner.lifecycleScope.launch {
                        val signInDateAlready = viewModel.getSignInDateByParentAndDateTime(date)
                        showDateIntroduction(signInDateAlready)
                    }
                    setViewShowByCalendar(calendar)
                }
            }

        })
        binding.calendarView.setOnCalendarLongClickListener(object :
            CalendarView.OnCalendarLongClickListener {
            override fun onCalendarLongClickOutOfRange(calendar: Calendar?) {

            }

            @SuppressLint("CheckResult")
            override fun onCalendarLongClick(calendar: Calendar?) {
                if (!FullMonthView.isInMyRange(calendar)) return
                val date = GregorianCalendar(
                    calendar
                    !!.year, (calendar.month.toInt() - 1), calendar.day.toInt()
                ).time
                viewLifecycleOwner.lifecycleScope.launch {
                    val signInDateAlready = viewModel.getSignInDateByParentAndDateTime(date)
                    showDateIntroduction(signInDateAlready)

                    val showCancel = signInDateAlready != null
                    MaterialDialog(requireContext()).show {
                        title(null, "打卡" + calendar!!.month + "月" + calendar.day)
                        inputNoAutoKeyboard(
                            hint = "备注",
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS,
                            allowEmpty = true
                        ) { _, text ->
                            if (!showCancel) {
                                val myCalendar = java.util.Calendar.getInstance();
                                myCalendar.time = date
                                val signInDate = SignInDate(
                                    0L,
                                    date,
                                    getDateStringByYMD(
                                        myCalendar.year,
                                        myCalendar.month,
                                        myCalendar.dayOfMonth
                                    ),
                                    text.toString(),
                                    0L,
                                    Date()
                                )
                                viewModel.insertSignInDate(signInDate)
                                showDateIntroduction(signInDate)
                            } else {
                                signInDateAlready!!.introduction = text.toString()
                                viewModel.updateSignInDate(signInDateAlready)
                                showDateIntroduction(signInDateAlready)
                            }

                        }
                        positiveButton(null, "打卡") {
                        }
                        if (showCancel) {
                            negativeButton(null, "取消打卡") {

                                signInDateAlready?.let {
                                    viewModel.deleteSignInDate(signInDateAlready)
                                    showDateIntroduction(null)
                                }
                            }
                        }
                    }
                }
            }

        })
        binding.calendarView.setOnYearChangeListener {
        }
        setViewShowByCalendarView(binding.calendarView)
    }

    private fun showDateIntroduction(signInDateAlready: SignInDate?) {
        if (signInDateAlready != null && !signInDateAlready.introduction.isNullOrEmpty() && signInDateAlready.introduction != "") {
            binding.tvDateIntroduction.text = "备注:" + signInDateAlready.introduction
            binding.clDateIntroduction.visibility = View.VISIBLE
        } else {
            binding.clDateIntroduction.visibility = View.GONE
        }
    }

    private fun setViewShowByCalendar(calendar: Calendar) {
        binding.tvYear.text = calendar.year.toString()
        binding.tvMonthDay.text =
            calendar.month.toString() + "月" + calendar.day.toString() + "日"
        binding.tvCurrentDay.text = calendar.day.toString()
        binding.tvLunar.text = calendar.lunar
    }

    private fun setViewShowByCalendarView(calendarView: CalendarView) {
        binding.tvYear.text = calendarView.curYear.toString()
        binding.tvMonthDay.text =
            calendarView.curMonth.toString() + "月" + calendarView.curDay.toString() + "日"
        binding.tvCurrentDay.text = calendarView.curDay.toString()
        binding.tvLunar.text = "今日"
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar? {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color //如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        calendar.addScheme(color, "假")
        calendar.addScheme(if (day % 2 == 0) -0xff3300 else -0x2ea012, "节")
        calendar.addScheme(if (day % 2 == 0) -0x9a0000 else -0xbe961f, "记")
        return calendar
    }

}