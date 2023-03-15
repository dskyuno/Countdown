package com.project.timemanagerment.feature.home.ui.countdowndetail

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.project.librarycountdown.countdownview.DynamicConfig
import com.project.timemanagerment.R
import com.project.timemanagerment.base.util.CountdownThemeList
import com.project.timemanagerment.base.util.DateFormatUtil
import com.project.timemanagerment.base.util.DateWeek
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.databinding.FragmentCountdownDetailBinding
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.ui.countdowndetail.recyclerview.CountdownThemeAdapter
import com.project.timemanagerment.feature.home.ui.countdowndetail.recyclerview.TextTypefaceAdapter
import com.project.timemanagerment.feature.home.ui.countdownsave.CountdownSaveImageFragment
import com.project.timemanagerment.feature.home.ui.countdwon.recyclerview.CountdownAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class CountdownDetailFragment : Fragment() {
    lateinit var binding: FragmentCountdownDetailBinding
    private val viewModel: CountdownDetailViewModel by viewModels()
    lateinit var myExitTransition: Any
    lateinit var myReenterTransition: Any


    companion object {
        const val mainShare = 1
        const val mainSave = 2
        const val LARGE_EXPAND_DURATION = 300L
        const val LARGE_COLLAPSE_DURATION = 250L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountdownDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //shareElement
        ViewCompat.setTransitionName(binding.cardView, "cardView")
        initTransition()


        setToolBar()
        initListener()
        subScribeUi()
        timerNewDayUpdate()
    }

    private fun initTransition() {
        myExitTransition = Fade(Fade.OUT).apply {
            duration = LARGE_EXPAND_DURATION / 2
            interpolator = FAST_OUT_LINEAR_IN
        }
        myReenterTransition = Fade(Fade.IN).apply {
            duration = LARGE_COLLAPSE_DURATION / 2
            startDelay = LARGE_COLLAPSE_DURATION / 2
            interpolator = LINEAR_OUT_SLOW_IN
        }
    }


    private fun timerNewDayUpdate() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val tomorrowStartTime = beginOfDay(tomorrow)
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.copyUpdateCurrentCountdown()
                }
            }
        }
        timer.scheduleAtFixedRate(
            task,
            //startTime
            tomorrowStartTime,
            //intervalTime
            CountdownAdapter.CountdownViewHolder.dayTimes.toLong()
        )
    }

    private fun beginOfDay(date: Date?): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }


    private fun subScribeUi() {
        viewModel.countdown.observe(viewLifecycleOwner) {
            //字体
            binding.cvIntervalTime.timeTextTypefaceIndex = it.textFontIndex
            //标题和计时时间
            val toTargetInterval = it.targetTime!!.time - System.currentTimeMillis()
            val targetTimeYMD = DateFormatUtil.getFormatYMD(it.targetTime.time)
            val currentTimeYMD = DateFormatUtil.getFormatYMD(Date().time)
            if (targetTimeYMD == currentTimeYMD) {
                binding.tvTitle.text = it.title + "已经到了"
                val builder = DynamicConfig.Builder()
                builder.setShowDay(true)
                builder.setShowHour(false)
                binding.cvIntervalTime.dynamicShow(builder.build())
                binding.cvIntervalTime.stop()
            } else if (toTargetInterval > 0) {
                binding.tvTitle.text = it.title + "还有"
                setIntervalUnit(toTargetInterval, binding)
                binding.cvIntervalTime.start(toTargetInterval)
            } else {
                binding.tvTitle.text = it.title + "已经"
                setIntervalUnit(
                    (Math.abs(toTargetInterval) - CountdownAdapter.CountdownViewHolder.dayTimes),
                    binding
                )
                binding.cvIntervalTime.start(Math.abs(toTargetInterval), false)
            }
            //
            binding.tvIntroduction.text = it.introduction
            if (it.themeIndex != 0) {
                val theme = CountdownThemeList.getThemeByIndex(it.themeIndex)
                if (theme != null) {
                    binding.tvTitle.setBackgroundResource(theme.topColor)
                    binding.cvIntervalTime.setBackgroundResource(theme.centerColor)
                }
            } else {
                setThemeIfDefaultTheme(it)
            }
            if (it.endTimeActive) {
                val calendar = Calendar.getInstance()
                calendar.time = it.endTime
                val enTimeString =
                    getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth);
                binding.tvTargetTime.text = it.targetTimeString +
                        "~" + enTimeString
            } else {
                binding.tvTargetTime.text = "目标日：" + it.targetTimeString
            }

        }
    }

    private fun toSaveOrShareCountdown(mainType: Int) {
        exitTransition = myExitTransition
        reenterTransition = myReenterTransition
        val directions =
            CountdownDetailFragmentDirections.navToCountdownSaveImageFragment(
                viewModel.countdownId,
                mainType
            )
        val extras = FragmentNavigatorExtras(
            binding.cardView to CountdownSaveImageFragment.CARD_VIEW,
        )
        findNavController().navigate(directions, extras)
    }

    private fun initListener() {
        binding.tvSaveToImage.setOnClickListener {
            toSaveOrShareCountdown(mainSave)
        }
        binding.llShare.setOnClickListener {
            toSaveOrShareCountdown(mainShare)
        }
        binding.llBackground.setOnClickListener {
            val dialog =
                MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
                    customView(
                        R.layout.dialog_countdown_background,
                        scrollable = true,
                        horizontalPadding = false,
                        noVerticalPadding = true
                    )
                    onDismiss {
                        viewModel.refreshCountdown()
                    }
                }

            val customView = dialog.getCustomView()
            //binding
            val recyclerViewTheme = customView.findViewById<RecyclerView>(R.id.recyclerView_theme)
            val recyclerViewTextTypeface =
                customView.findViewById<RecyclerView>(R.id.recyclerView_typeface)
            val tvSaveCountdownAboutBackground =
                customView.findViewById<TextView>(R.id.tv_save_countdown_obout_background)
            //typeface
            recyclerViewTextTypeface.addItemDecoration(HorizontalSpace())
            val textTypefaceAdapter = TextTypefaceAdapter()
            textTypefaceAdapter.itemTypefaceClickCallback = {
                binding.cvIntervalTime.timeTextTypefaceIndex = it
                binding.cvIntervalTime.invalidate()
            }
            recyclerViewTextTypeface.adapter = textTypefaceAdapter
            viewModel.textTypefaceList.observe(viewLifecycleOwner) {
                viewLifecycleOwner.lifecycleScope.launch {
                    textTypefaceAdapter.submitList(it)
                    val curCountdown = viewModel.getGetCountDownNotFlow()
                    recyclerViewTextTypeface.post {
                        recyclerViewTextTypeface.scrollToPosition(curCountdown.textFontIndex - 1)
                        textTypefaceAdapter.setCurrentSelectTextIndexAndUpdate(curCountdown.textFontIndex)
                    }
                }
            }
            //theme
            recyclerViewTheme.addItemDecoration(HorizontalSpace())
            val themeAdapter = CountdownThemeAdapter()
            themeAdapter.itemThemeClickCallBack = { index ->
                if (index != 0) {
                    val theme = CountdownThemeList.getThemeByIndex(index)
                    if (theme != null) {
                        binding.tvTitle.setBackgroundResource(theme.topColor)
                        binding.cvIntervalTime.setBackgroundResource(theme.centerColor)
                        binding.tvTargetTime.setBackgroundResource(theme.bottomColor)
                    }
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val currentCountdown = viewModel.getGetCountDownNotFlow()
                        setThemeIfDefaultTheme(currentCountdown)
                    }
                }
            }
            recyclerViewTheme.adapter = themeAdapter
            viewModel.themeList.observe(viewLifecycleOwner) {
                viewLifecycleOwner.lifecycleScope.launch {
                    themeAdapter.submitList(it)
                    val curCountdown = viewModel.getGetCountDownNotFlow()
                    recyclerViewTheme.post {
                        recyclerViewTheme.scrollToPosition(curCountdown.themeIndex)
                        themeAdapter.setCurrentSelectThemeIndexAndUpdate(curCountdown.themeIndex)
                    }
                }
            }
            //save
            tvSaveCountdownAboutBackground.setOnClickListener {
                val fontIndex = textTypefaceAdapter.currentSelectTextIndex
                val themeIndex = themeAdapter.currentSelectThemeIndex
                viewModel.updateBackground(fontIndex, themeIndex)
                dialog.dismiss()

            }
        }
        binding.llToTop.setOnClickListener {
            viewModel.itemToTop()
            ToastUtilsDraK.showMsg("置顶成功")
        }
    }

    private fun setThemeIfDefaultTheme(countdown: Countdown) {
        countdown.let {
            val toTargetInterval = it.targetTime!!.time - System.currentTimeMillis()
            val targetTimeYMD = DateFormatUtil.getFormatYMD(it.targetTime.time)
            val currentTimeYMD = DateFormatUtil.getFormatYMD(Date().time)
            if (targetTimeYMD == currentTimeYMD) {
                binding.tvTitle.setBackgroundResource(R.color.countdown_item_color_is_now_add_alpha)
                binding.cvIntervalTime.setBackgroundResource(R.color.countdown_item_color_is_now)
                binding.tvTargetTime.setBackgroundResource(R.color.window_bg)
            } else if (toTargetInterval > 0) {
                binding.tvTitle.setBackgroundResource(R.color.countdown_item_color_have_interval_add_alpha)
                binding.cvIntervalTime.setBackgroundResource(R.color.countdown_item_color_have_interval)
                binding.tvTargetTime.setBackgroundResource(R.color.window_bg)
            } else {
                binding.tvTitle.setBackgroundResource(R.color.countdown_item_color_already_add_alpha)
                binding.cvIntervalTime.setBackgroundResource(R.color.countdown_item_color_already)
                binding.tvTargetTime.setBackgroundResource(R.color.window_bg)
            }
        }
    }

    private fun getDateStringByYMD(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        val gregorianCalendar = GregorianCalendar(year, monthOfYear, dayOfMonth - 1)
        val dayOfWeek: Int = gregorianCalendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = DateWeek.getWeekStringByWeekIndex(dayOfWeek)
        val realMoth = monthOfYear + 1
        val dateString = "$year-$realMoth-$dayOfMonth $dayOfWeekString"
        return dateString
    }

    class HorizontalSpace : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = 20
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
        }

    }

    private fun setToolBar() {
        val toolbar = binding.toolbar
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_action_edit -> {
                    exitTransition = null
                    reenterTransition = null
                    val directions =
                        CountdownDetailFragmentDirections.navToCountdownEditFragment(viewModel.countdownId)
                    findNavController().navigate(directions)
                }
                R.id.menu_action_delete -> {
                    MaterialDialog(requireContext()).show {
                        title(null, "删除倒计时")
                        message(null, "确定删除倒计时")
                        negativeButton(null, "取消")
                        positiveButton(null, "确定") {
                            viewModel.deleteCountdown()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
            return@setOnMenuItemClickListener true
        }
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    /**
     * countdownView  show day or hour
     */
    private fun setIntervalUnit(toTargetInterval: Long, binding: FragmentCountdownDetailBinding) {
        if (toTargetInterval / CountdownAdapter.CountdownViewHolder.dayTimes > 0) {
            val builder = DynamicConfig.Builder()
            builder.setShowDay(true)
            builder.setShowHour(false)
            binding.cvIntervalTime.dynamicShow(builder.build())
        } else {
            val builder = DynamicConfig.Builder()
            builder.setShowDay(false)
            builder.setShowHour(true)
            binding.cvIntervalTime.dynamicShow(builder.build())
        }
    }
}