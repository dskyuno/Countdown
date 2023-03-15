package com.project.timemanagerment.feature.home.ui.signinwork

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.project.timemanagerment.R
import com.project.timemanagerment.app.presentation.MainFragmentDirections

import com.project.timemanagerment.databinding.FragmentTabTwoBinding
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.Countdown
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInDate
import com.project.timemanagerment.feature.home.data.datasource.database.model.entity.SignInWork
import com.project.timemanagerment.feature.home.ui.countdwon.recyclerview.CountdownAdapter
import com.project.timemanagerment.feature.home.ui.signinwork.recyclerview.SignInWorkAdapter
import com.project.timemanagerment.feature.home.ui.signinwork.recyclerview.callback.SignInWorkAdapterCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class SignInWorkFragment : Fragment() {
    lateinit var binding: FragmentTabTwoBinding
    val viewModel: SignInWorkViewModel by viewModels()
    lateinit var adapter: SignInWorkAdapter
    var isCreate = false
    lateinit var newDayUpdateTimer: Timer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabTwoBinding.inflate(inflater, container, false)
        adapter = SignInWorkAdapter()
        adapter.callback = object : SignInWorkAdapterCallback {
            override fun insertSignInDate(signInDate: SignInDate) {
                viewModel.insertSignInDateAndWorkFinalDate(signInDate)
            }

            override fun deleteSignInDate(signDateId: Long, signInWorkId: Long) {
                viewModel.deleteSignInDateByIdAndWorkFinalDate(signDateId, signInWorkId)
            }

            override fun updateSignInWork(signInWork: SignInWork) {
                viewModel.updateSignInWork(signInWork)
            }

            override fun updateSignInDate(signInDate: SignInDate) {
                viewModel.updateSignInDate(signInDate)
            }

            override fun updateSignInDateIntroduction(signInWorkId: Long, introduction: String) {
                viewModel.updateSignInDateIntroduction(signInWorkId, introduction)
            }
        }
        binding.recyclerViewSignInWork.adapter = adapter

        binding.fab.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val directions = MainFragmentDirections.navToSignInWorkEditFragment(0L)
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subScribeUi(adapter)
        timerNewDayUpdate()
        viewLifecycleOwner
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
                    viewModel.preNullAndUpdateList()
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


    private fun subScribeUi(adapter: SignInWorkAdapter) {
        viewModel.signInWorkWithDatesList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (!isCreate) {
                    isCreate = true
                    //delay(250)
                }
                adapter.submitList(it)
            }
        }
    }


}