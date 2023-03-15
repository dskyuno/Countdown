package com.project.timemanagerment.feature.home.ui.countdownsave

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.project.timemanagerment.base.util.*
import com.project.timemanagerment.databinding.FragmentCountdownSaveImageBinding
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.ui.countdowndetail.CountdownDetailFragment
import com.project.timemanagerment.feature.home.ui.countdowndetail.recyclerview.*
import com.project.timemanagerment.feature.home.ui.countdwon.recyclerview.CountdownAdapter
import com.project.librarycountdown.countdownview.DynamicConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import com.project.timemanagerment.R


@AndroidEntryPoint
class CountdownSaveImageFragment : Fragment() {
    val viewModel: CountdownSaveImageViewModel by viewModels()
    lateinit var binding: FragmentCountdownSaveImageBinding
    val args: CountdownSaveImageFragmentArgs by navArgs()

    val delayLoadListTime = 300L

    companion object {
        val CARD_VIEW = "cardView"
        const val LARGE_EXPAND_DURATION = 300L
        const val LARGE_COLLAPSE_DURATION = 250L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = createSharedElementTransition(LARGE_EXPAND_DURATION)
        sharedElementReturnTransition = createSharedElementTransition(LARGE_COLLAPSE_DURATION)
        //卡
        /* sharedElementEnterTransition = MaterialContainerTransform(requireContext(), true)
         sharedElementReturnTransition = MaterialContainerTransform(requireContext(), false)*/
    }

    private fun createSharedElementTransition(duration: Long): Transition {
        return transitionTogether {
            this.duration = duration
            interpolator = FAST_OUT_SLOW_IN
            this += SharedFade()
            this += ChangeImageTransform()
            this += ChangeBounds()
            this += ChangeTransform()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountdownSaveImageBinding.inflate(inflater, container, false)
        //visible by main type
        if (args.mainType == CountdownDetailFragment.mainSave) {
            binding.clSaveAndShare.visibility = View.VISIBLE
            binding.toolbar.title = "存为图片"
        } else {
            binding.clShareAndSave.visibility = View.VISIBLE
            binding.toolbar.title = "分享倒计时"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.background, CARD_VIEW)
     //   postponeEnterTransition(200L, TimeUnit.MILLISECONDS)
        setToolbar()
        initListener()
        subScribeUi()
    }

    private fun initListener() {
        //save
        binding.llSaveMainSave.setOnClickListener {
            saveImage(binding.clSaveView)
        }
        binding.llSaveMainShare.setOnClickListener {
            saveImage(binding.clSaveView)
        }
        //share
        binding.llShareMainSave.setOnClickListener {
            shareImage(binding.clSaveView)
        }
        binding.llShareMainShare.setOnClickListener {
            shareImage(binding.clSaveView)
        }
    }

    private fun subScribeUi() {
        viewModel.countdown.observe(viewLifecycleOwner) { it ->
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
            //--
            binding.cvIntervalTime.timeTextTypefaceIndex = it.textFontIndex
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

            if (it.introduction.isNullOrEmpty()) {
                binding.tvIntroduction.visibility = View.GONE
            } else {
                binding.tvIntroduction.visibility = View.VISIBLE
                binding.tvIntroduction.text = it.introduction
            }
        }
        //list
        //typeface
        binding.recyclerViewTypeface.addItemDecoration(HorizontalSpace())
        val textTypefaceAdapter = TextTypefaceAdapter()
        textTypefaceAdapter.itemTypefaceClickCallback = {
            binding.cvIntervalTime.setTimeTextTypefaceIndex(it)
        }
        binding.recyclerViewTypeface.adapter = textTypefaceAdapter
        viewModel.textTypefaceList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                var curCountdown = viewModel.getGetCountDownNotFlow()
                textTypefaceAdapter.submitList(it)
                binding.recyclerViewTypeface.post {
                    binding.recyclerViewTypeface.scrollToPosition(curCountdown.textFontIndex - 1)
                    textTypefaceAdapter.setCurrentSelectTextIndexAndUpdate(curCountdown.textFontIndex)
                    binding.recyclerViewTypeface.visibility = View.VISIBLE
                }
            }
        }
        //theme
        binding.recyclerViewTheme.addItemDecoration(CountdownDetailFragment.HorizontalSpace())
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
        binding.recyclerViewTheme.adapter = themeAdapter
        viewModel.themeList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                themeAdapter.submitList(it)
                val curCountdown = viewModel.getGetCountDownNotFlow()
                binding.recyclerViewTheme.post {
                    binding.recyclerViewTheme.scrollToPosition(curCountdown.themeIndex)
                    themeAdapter.setCurrentSelectThemeIndexAndUpdate(curCountdown.themeIndex)
                    binding.recyclerViewTheme.visibility = View.VISIBLE
                }
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

    private fun setToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
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

    private fun shareImage(v: View) {
        ViewImage.getScreenShotFromView(v, requireActivity().window) { bitmap ->
            val path = saveImageToCacheByBitmap(bitmap, requireContext())
            val uriToImage =
                FileProvider.getUriForFile(
                    requireContext(),
                    "com.project.timemanagerment.fileprovider",
                    File(path)
                )
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uriToImage)
                type = "image/jpeg"
            }
            startActivity(Intent.createChooser(shareIntent, "分享到"))
        }

    }


    private fun saveImage(v: View) {
        ViewImage.getScreenShotFromView(v, requireActivity().window) { bitmap ->
            saveBitmap(
                requireContext(),
                bitmap,
                Bitmap.CompressFormat.JPEG,
                "image/png",
                System.currentTimeMillis().toString()
            )

            ToastUtilsDraK.showMsg("已保存到系统相册")
        }


    }

    class HorizontalSpace : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = 15
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(c, parent, state)
        }

    }

    private fun setIntervalUnit(
        toTargetInterval: Long,
        binding: FragmentCountdownSaveImageBinding
    ) {
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