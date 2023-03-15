package com.project.timemanagerment.feature.home.ui.countdwon.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.project.timemanagerment.R


class CreateCountdownDialog : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            val viewInflater = inflater.inflate(R.layout.fragment_countdown_create, null)

            builder.setView(viewInflater)
                // Add action buttons
                .setPositiveButton(
                    "确定"
                ) { dialog, id ->

                }
                .setNegativeButton(
                    "取消"
                ) { dialog, id ->
                    getDialog()?.cancel()
                   // listener?.onDialogNegativeClick()

                }
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    // Use this instance of the interface to deliver action events
   // lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    override fun onResume() {
        super.onResume()
        val alertDialog = dialog as AlertDialog
        alertDialog?.let {
          /*  val button = it.getButton(Dialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                if (projectName.text.toString().isEmpty()) {
                    Toast.makeText(context, "项目名称不能为空", Toast.LENGTH_SHORT).show()
                } else {
                    listener?.onDialogPositiveClick(
                        PrototypeProject(
                            projectName.text.toString(),
                            imageUrl,
                            introduction?.text.toString()
                        )
                    )
                    alertDialog.dismiss()
                }
            }*/
        }

  /*      projectName.postDelayed(Runnable {
            projectName.requestFocus()
            val imm: InputMethodManager = projectName.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null) imm.showSoftInput(
                projectName,
                InputMethodManager.SHOW_IMPLICIT
            )
        }, 30)*/
    }



}