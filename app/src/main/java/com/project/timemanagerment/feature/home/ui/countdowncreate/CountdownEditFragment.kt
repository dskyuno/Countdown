package com.project.timemanagerment.feature.home.ui.countdowncreate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.date.dayOfMonth
import com.afollestad.date.month
import com.afollestad.date.year
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.lxj.xpopup.XPopup
import com.project.timemanagerment.R
import com.project.timemanagerment.app.dataStore
import com.project.timemanagerment.base.util.CountDownType
import com.project.timemanagerment.base.util.DateWeek
import com.project.timemanagerment.base.util.ToastUtilsDraK
import com.project.timemanagerment.databinding.FragmentCountdownCreateBinding

import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.ui.countdwon.CountdownFragment
import com.project.timemanagerment.feature.home.ui.countdwon.CountdownViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class CountdownEditFragment : Fragment() {
    companion object {
        val DESTINATION_TARGET_TIME = 1
        val DESTINATION_END_TIME = 2
    }

    private val viewModel: CountdownViewModel by activityViewModels()

    val args: CountdownEditFragmentArgs by navArgs()
    private lateinit var rocketAnimation: AnimationDrawable
    private var isShow = false
    private lateinit var toArrowRight: Animation
    private lateinit var toArrowDown: Animation

    private val ARRANGEMENT_MODE = intPreferencesKey("arrangement_mode")

    /*bindView*/
    private lateinit var binding: FragmentCountdownCreateBinding
    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return 0;
    }
    
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountdownCreateBinding.inflate(inflater, container, false)
        /*viewBinding*/
        /*paddingTop*/
        binding.clParent.setPadding(
            binding.clParent.paddingLeft,
            getStatusBarHeight(),
            binding.clParent.paddingRight,
            binding.clParent.paddingBottom
        )
        setToolbar()
        val clTargetTime = binding.clTargetTime
        val arrowIcon = binding.ivArrow
        val clMoreSwitch = binding.clMore
        val llMore = binding.llMore
        val switchEndTime = binding.switchEndTime
        val clEndTime = binding.clEndTime
        val clCountdownType = binding.clCountdownType
        initView()
        showKeyBord(binding.etTitle)
        initAnimal()
        /*save countdown*/
        binding.tvSaveCountdown.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val targetTime = getDateTimeByDateString(binding.tvTargetTimeString.text.toString())
            val targetTimeType = Countdown.timeTypeDefault
            val targetTimeTypeString = binding.tvTargetTimeString.text.toString()
            val targetTimeTypeChineseString = binding.tvTargetTimeString.text.toString()
            val countdownType =
                CountDownType.getCountDownTypeIntBYString(binding.tvCountdownType.text.toString())
            val introduction = binding.etIntroduction.text.toString()
            val endTimeActive = switchEndTime.isChecked
            var endTime = getDateTimeByDateString(binding.tvEndTimeString.text.toString())
            if (title.isNullOrEmpty()) {
                ToastUtilsDraK.showMsgTop("计时名称不能为空")
                return@setOnClickListener
            }
            if (!endTimeActive) {
                endTime = null
            }
           /* val model = runBlocking {
                requireContext().dataStore.data.map { preferences ->
                    preferences[ARRANGEMENT_MODE] ?: CountdownFragment.MODE_LIST
                }.first()
            }*/
            hideKeyboard()
            if (args.countdownId == 0L) {
                val countdown = Countdown.createCountdownByUserInput(
                    title,
                    targetTime,
                    targetTimeType,
                    targetTimeTypeString,
                    targetTimeTypeChineseString,
                    countdownType,
                    introduction,
                    endTimeActive,
                    endTime
                )
                viewModel.insertCountdown(countdown)
                ToastUtilsDraK.showMsgCenter("添加成功")
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    val countdownOld = viewModel.getCountdownByIdNotFlow(args.countdownId)
                    val countdownNew = Countdown.updateCountdownByUserInput(
                        title,
                        targetTime,
                        targetTimeType,
                        targetTimeTypeString,
                        targetTimeTypeChineseString,
                        countdownType,
                        introduction,
                        endTimeActive,
                        endTime, countdownOld
                    )
                    viewModel.updateCountdown(countdownNew)
                }
                ToastUtilsDraK.showMsgCenter("更新成功")
            }

            findNavController().navigateUp()
        }
        /*selectTime */
        clTargetTime.setOnClickListener {
            showChangeTimeDialog(DESTINATION_TARGET_TIME, BottomSheet(LayoutMode.WRAP_CONTENT))
        }
        clEndTime.setOnClickListener {
            showChangeTimeDialog(DESTINATION_END_TIME, BottomSheet(LayoutMode.WRAP_CONTENT))
        }
        clCountdownType.setOnClickListener {
            val currentType =
                CountDownType.getCountDownTypeIntBYString(binding.tvCountdownType.text.toString())
            val checkIndex = currentType - 1
            val icons = intArrayOf(R.color.countdown_type_general, R.color.countdown_type_urgency)
            XPopup.Builder(context)
                .maxHeight(400)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList(
                    "请选择类型", arrayOf("普通", "紧急"),
                    icons, checkIndex
                ) { position, text ->
                    changeTypeShowBySelectTypePosition(position + 1)
                }
                .show()
        }
        /*operate view show and hide*/
        clMoreSwitch.setOnClickListener {
            isShow = if (isShow) {
                arrowIcon.startAnimation(toArrowRight)
                llMore.visibility = View.GONE
                clEndTime.visibility = View.GONE
                false
            } else {
                arrowIcon.startAnimation(toArrowDown)
                llMore.visibility = View.VISIBLE
                if (switchEndTime.isChecked) {
                    clEndTime.visibility = View.VISIBLE
                }
                true
            }
        }
        switchEndTime.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                clEndTime.visibility = View.VISIBLE
            } else {
                clEndTime.visibility = View.GONE
            }
        }
        navigationBack(binding)
        return binding.root
    }

    private fun setToolbar() {
        if (args.countdownId != 0L) {
            binding.toolbar.title = "编辑倒计时"
        } else {
            binding.toolbar.title = "添加倒计时"
        }
    }

    private fun changeTypeShowBySelectTypePosition(position: Int) {
        binding.tvCountdownType.text = CountDownType.getCountDownTypeStringByInt(position)
        if (position == Countdown.countdownTypeGeneral) {
            binding.tvCountdownTypeColor.setBackgroundResource(R.color.countdown_type_general)
        } else {
            binding.tvCountdownTypeColor.setBackgroundResource(R.color.countdown_type_urgency)
        }
    }

    fun Fragment.hideKeyboard() = ViewCompat.getWindowInsetsController(requireView())
        ?.hide(WindowInsetsCompat.Type.ime())

    private fun initView() {
        if (args.countdownId != 0L) {
            viewLifecycleOwner.lifecycleScope.launch {
                val countdown = viewModel.getCountdownById(args.countdownId)
                val live = countdown.asLiveData()
                live.observe(viewLifecycleOwner) {
                    binding.etTitle.setText(it.title)
                    val position = binding.etTitle.text.length
                    binding.etTitle.setSelection(position)
                    binding.tvTargetTimeString.text = it.targetTimeString
                    binding.etIntroduction.setText(it.introduction)
                    changeTypeShowBySelectTypePosition(it.countdownType)
                    if (it.endTimeActive) {
                        binding.ivArrow.startAnimation(toArrowDown)
                        binding.llMore.visibility = View.VISIBLE
                        binding.switchEndTime.isChecked = true
                        binding.clEndTime.visibility = View.VISIBLE
                        isShow = true
                        //endTime
                        val calendar = Calendar.getInstance();
                        calendar.time = it.endTime
                        binding.tvEndTimeString.text =
                            getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
                    } else {
                        val calendar = Calendar.getInstance();
                        binding.tvEndTimeString.text =
                            getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
                    }
                }
            }
        } else {
            val calendar = Calendar.getInstance();
            binding.tvTargetTimeString.text =
                getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
            binding.tvEndTimeString.text =
                getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
        }
    }


    private fun showChangeTimeDialog(
        destinationTimeType: Int,
        dialogBehavior: DialogBehavior = ModalDialog
    ) {
        val dialog = MaterialDialog(requireContext(), dialogBehavior).show {
            title(null, "选择时间")
            customView(
                R.layout.dialog_bottom_datepicker,
                scrollable = true,
            )
        }
        val customView = dialog.getCustomView()
        val dpSelectTime = customView.findViewById<DatePicker>(R.id.dp_select_time)
        if (destinationTimeType == DESTINATION_TARGET_TIME) {
            val showDate = getDateTimeByDateString(binding.tvTargetTimeString.text.toString())
            val calendar = Calendar.getInstance()
            calendar.time = showDate
            dpSelectTime.init(
                calendar.year, calendar.month, calendar.dayOfMonth
            ) { _, year, month, day ->
                binding.tvTargetTimeString.text = getDateStringByYMD(year, month, day)
            }
        } else {
            val showDate = getDateTimeByDateString(binding.tvEndTimeString.text.toString())
            val calendar = Calendar.getInstance()
            calendar.time = showDate
            dpSelectTime.init(
                calendar.year, calendar.month, calendar.dayOfMonth
            ) { _, year, month, day ->
                binding.tvEndTimeString.text = getDateStringByYMD(year, month, day)
            }
        }
    }

    private fun getDateStringByYMD(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        val gregorianCalendar = GregorianCalendar(year, monthOfYear, dayOfMonth - 1)
        val dayOfWeek: Int = gregorianCalendar.get(Calendar.DAY_OF_WEEK)
        val dayOfWeekString = DateWeek.getWeekStringByWeekIndex(dayOfWeek)
        val realMoth = monthOfYear + 1
        val dateString = "$year-$realMoth-$dayOfMonth $dayOfWeekString"
        // getDateTimeByDateString(dateString)
        return dateString
    }

    private fun getDateTimeByDateString(dateString: String): Date? {
        val dateStringSpit = dateString.split("-")
        val year = dateStringSpit[0].trim()
        val month = dateStringSpit[1].trim()
        val day = dateStringSpit[2].substring(0, 2).trim()
        return GregorianCalendar(year.toInt(), (month.toInt() - 1), day.toInt()).time
    }


    private fun navigationBack(binding: FragmentCountdownCreateBinding) {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initAnimal() {
        toArrowRight = AnimationUtils.loadAnimation(requireContext(), R.anim.arrow_to_right)
            .also { hyperspaceJumpAnimation ->
                hyperspaceJumpAnimation.fillAfter = true
            }
        toArrowDown = AnimationUtils.loadAnimation(requireContext(), R.anim.arrow_to_down)
            .also { hyperspaceJumpAnimation ->
                hyperspaceJumpAnimation.fillAfter = true
            }
    }


    private fun showKeyBord(editText: EditText) {
        editText.postDelayed(Runnable {
            editText.requestFocus()
            val imm: InputMethodManager = editText.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) imm.showSoftInput(
                editText,
                InputMethodManager.SHOW_IMPLICIT
            )
        }, 300)
    }
}