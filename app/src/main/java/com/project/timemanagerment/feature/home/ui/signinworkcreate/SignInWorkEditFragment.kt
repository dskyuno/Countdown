package com.project.timemanagerment.feature.home.ui.signinworkcreate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.blankj.utilcode.util.LogUtils
import com.lxj.xpopup.XPopup
import com.project.timemanagerment.R
import com.project.timemanagerment.base.util.*
import com.project.timemanagerment.databinding.FragmentSignInWorkEditBinding


import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.ui.signinworkcreate.recyclerview.SignInWorkIconAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SignInWorkEditFragment : Fragment() {
    lateinit var binding: FragmentSignInWorkEditBinding
    val viewModel: SignInWorkEditViewModel by viewModels()
    lateinit var adapter: SignInWorkIconAdapter

    companion object {
        val DESTINATION_START_TIME = 1
        val DESTINATION_END_TIME = 2
    }


    private var isShow = false
    private lateinit var toArrowRight: Animation
    private lateinit var toArrowDown: Animation

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return 0;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInWorkEditBinding.inflate(inflater, container, false)
        return return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clParent.setPadding(
            binding.clParent.paddingLeft,
            getStatusBarHeight(),
            binding.clParent.paddingRight,
            binding.clParent.paddingBottom
        )
        //icon select
        adapter = SignInWorkIconAdapter()
        adapter.currentSelectIconIndex = 1
        adapter.selectIconIndexCallback = {
            val drawId = SignInIconList.getSignInIconByIndex(it).iconId
            val iconDraw = ContextCompat.getDrawable(requireContext(), drawId)
            binding.ivWorkIcon.setImageDrawable(iconDraw)
        }
        binding.recyclerViewWorkIcon.adapter = adapter
        //
        setToolbar()
        initAnimal()
        initView()
        //save toolbar
        initViewListener()
        subscribeUi()
    }


    private fun initViewListener() {
        //select property
        binding.clStartTime.setOnClickListener {
            showChangeTimeDialog(
                DESTINATION_START_TIME, BottomSheet(
                    LayoutMode.WRAP_CONTENT
                )
            )
        }
        binding.clEndTime.setOnClickListener {
            showChangeTimeDialog(DESTINATION_END_TIME, BottomSheet(LayoutMode.WRAP_CONTENT))
        }
        binding.clWorkType.setOnClickListener {
            val currentType =
                SignInWorkType.getSignInWorkTypeIntBYString(binding.tvWorkType.text.toString())
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
        binding.clMore.setOnClickListener {
            isShow = if (isShow) {
                binding.ivArrow.startAnimation(toArrowRight)
                binding.llMore.visibility = View.GONE
                binding.clEndTime.visibility = View.GONE
                false
            } else {
                binding.ivArrow.startAnimation(toArrowDown)
                binding.llMore.visibility = View.VISIBLE
                if (binding.switchEndTime.isChecked) {
                    binding.clEndTime.visibility = View.VISIBLE
                }
                true
            }
        }
        binding.switchEndTime.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                binding.clEndTime.visibility = View.VISIBLE
            } else {
                binding.clEndTime.visibility = View.GONE
            }
        }
    }


    private fun subscribeUi() {
        viewModel.signInWorkIconList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitList(it)
                if (viewModel.signInWorkId != 0L) {
                    val currentWork =
                        viewModel.getSignInWorkByPrimaryKeyNotFlow(viewModel.signInWorkId)
                    binding.recyclerViewWorkIcon.post {
                        //LogUtils.e("post__________")
                        binding.recyclerViewWorkIcon.scrollToPosition(currentWork!!.iconIndex - 1)
                        adapter.setCurrentSelectIconIndexAndUpdate(currentWork.iconIndex)
                    }
                }
                //   delay(300L)
                LogUtils.e("delay__________")
                binding.recyclerViewWorkIcon.visibility = View.VISIBLE
            }
        }
    }

    private fun setToolbar() {
        if (viewModel.signInWorkId != 0L) {
            binding.toolbar.title = "编辑打卡任务"
        } else {
            binding.toolbar.title = "创建打卡任务"
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    val name = binding.etName.text.toString()
                    val startTime =
                        getDateTimeByDateString(binding.tvStartTimeString.text.toString())
                    val startTimeString = binding.tvStartTimeString.text.toString()
                    val iconIndex = adapter.currentSelectIconIndex
                    val introduction = binding.etIntroduction.text.toString()
                    val sinInWorkType =
                        SignInWorkType.getSignInWorkTypeIntBYString(binding.tvWorkType.text.toString())
                    val endTimeActive = binding.switchEndTime.isChecked
                    var endTime = getDateTimeByDateString(binding.tvEndTimeString.text.toString())
                    val isShowRemind = binding.rbShowMind.isChecked

                    if (name.isNullOrEmpty()) {
                        ToastUtilsDraK.showMsgTop("打卡名称不能为空")
                        return@setOnMenuItemClickListener true
                    }
                     hideKeyboard()
                    if (endTimeActive) {
                        if (endTime!!.time < startTime!!.time) {
                            MaterialDialog(requireContext()).show {
                                title(null, "日期区间异常")
                                message(null, "结束日期需大于开始日期")
                                positiveButton(null, "确定")
                            }
                            return@setOnMenuItemClickListener true
                        }
                    } else {
                        endTime = null
                    }

                    if (viewModel.signInWorkId == 0L) {
                        val work = SignInWork.createSignInWork(
                            name,
                            startTime!!,
                            startTimeString,
                            iconIndex,
                            introduction,
                            sinInWorkType,
                            endTimeActive,
                            endTime,
                            isShowRemind
                        )
                        viewModel.createSignInWork(work)

                    } else {
                        viewLifecycleOwner.lifecycleScope.launch {
                            val oldWork =
                                viewModel.getSignInWorkByPrimaryKeyNotFlow(viewModel.signInWorkId)
                            val work = SignInWork.updateSignInWork(
                                name,
                                startTime!!,
                                startTimeString,
                                iconIndex,
                                introduction,
                                sinInWorkType,
                                endTimeActive,
                                endTime,
                                isShowRemind,
                                oldWork!!
                            )
                            viewModel.updateSignInWork(work)
                        }
                    }
                    findNavController().navigateUp()

                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun Fragment.hideKeyboard() = ViewCompat.getWindowInsetsController(requireView())
        ?.hide(WindowInsetsCompat.Type.ime())

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
        if (destinationTimeType == DESTINATION_START_TIME) {
            val showDate = getDateTimeByDateString(binding.tvStartTimeString.text.toString())
            val calendar = Calendar.getInstance()
            calendar.time = showDate
            dpSelectTime.init(
                calendar.year, calendar.month, calendar.dayOfMonth
            ) { _, year, month, day ->
                binding.tvStartTimeString.text = getDateStringByYMD(year, month, day)
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


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {
        if (viewModel.signInWorkId != 0L) {
            //update
            viewModel.signInWork.observe(viewLifecycleOwner) {
                it?.let { work ->
                    val drawId = SignInIconList.getSignInIconByIndex(work.iconIndex).iconId
                    val iconDraw = requireContext().resources.getDrawable(
                        drawId,
                        requireContext().theme
                    )
                    binding.ivWorkIcon.setImageDrawable(iconDraw)
                    binding.etName.setText(work.name)
                    val position = binding.etName.text.length
                    binding.etName.setSelection(position)
                    binding.etIntroduction.setText(work.introduction)
                    binding.tvStartTimeString.text = work.startTimeString
                    changeTypeShowBySelectTypePosition(work.sinInWorkType)
                    if (it.endTimeActive) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            binding.llMore.visibility = View.VISIBLE
                            binding.switchEndTime.isChecked = true
                            binding.clEndTime.visibility = View.VISIBLE
                            isShow = true
                            //endTime
                            val calendar = Calendar.getInstance();
                            calendar.time = it.endTime
                            binding.tvEndTimeString.text =
                                getDateStringByYMD(
                                    calendar.year,
                                    calendar.month,
                                    calendar.dayOfMonth
                                )
                            delay(300)
                            binding.ivArrow.startAnimation(toArrowDown)
                        }
                    } else {
                        val calendar = Calendar.getInstance();
                        binding.tvEndTimeString.text =
                            getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
                    }
                    binding.rbShowMind.isChecked = work.isShowRemind
                }
            }

        } else {
            val iconDraw = ContextCompat.getDrawable(requireContext(), SignInIconList.icon_1.iconId)
            binding.ivWorkIcon.setImageDrawable(iconDraw)
            //create
            val calendar = Calendar.getInstance();
            binding.tvStartTimeString.text =
                getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
            binding.tvEndTimeString.text =
                getDateStringByYMD(calendar.year, calendar.month, calendar.dayOfMonth)
            binding.rbShowMind.isChecked = false
            showKeyBord(binding.etName)
        }

    }

    private fun changeTypeShowBySelectTypePosition(position: Int) {
        binding.tvWorkType.text = SignInWorkType.getSignInWorkTypeStringByInt(position)
        if (position == SignInWork.signInWorkTypeDefault) {
            binding.tvWorkTypeColor.setBackgroundResource(R.color.countdown_type_general)
        } else {
            binding.tvWorkTypeColor.setBackgroundResource(R.color.countdown_type_urgency)
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