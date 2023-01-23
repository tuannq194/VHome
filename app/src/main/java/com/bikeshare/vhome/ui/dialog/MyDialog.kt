package com.bikeshare.vhome.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikeshare.vhome.R
import com.bikeshare.vhome.databinding.DialogBottomBinding
import com.bikeshare.vhome.ui.adapter.CameraAdapter

class MyDialog constructor(var cameraAdapter: CameraAdapter?) : DialogFragment() {
    constructor() : this(null)
    //private lateinit var cameraAdapter: CameraAdapter
    //val cameraItems: ArrayList<CameraItem> = ArrayList()

    companion object {
        const val EVENT_DIALOG = "getEvent"
        const val TYPE_DIALOG = "getType"
        const val CAMERA_DIALOG = "getCamera"
    }

    private var _bindingDialog: DialogBottomBinding? = null
    private val bindingDialog get() = _bindingDialog!!
    var onClickItem: ((String) -> Unit)? = null
    var onClickItemEvent: ((Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var dialog: Dialog? = null
        if (tag.equals(EVENT_DIALOG)) dialog = getEventDialog()
        if (tag.equals(TYPE_DIALOG)) dialog = getTypeDialog()
        if (tag.equals(CAMERA_DIALOG)) dialog = getCameraDialog()
        dialog?.apply {
            show()
            window!!.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes.windowAnimations = R.style.DialogAnimation
                setGravity(Gravity.BOTTOM)
            }
        }
        return dialog!!
    }

    private fun getEventDialog(): Dialog? {
        val builder = AlertDialog.Builder(requireActivity())

        _bindingDialog = DialogBottomBinding.inflate(LayoutInflater.from(activity))
        builder.setView(bindingDialog.root)

        bindingDialog.apply {
            radioGroup.visibility = View.VISIBLE
            recyclerViewListcam.visibility = View.GONE
            dialogTitle.setText("Chọn sự kiện")
            radioButton1.apply {
                setText("Tất cả")
                setOnClickListener {
                    onClickItemEvent?.invoke(0)
                    dismiss()
                }
            }
            radioButton2.apply {
                setText("Phát hiện chuyển động")
                setOnClickListener {
                    onClickItemEvent?.invoke(17)
                    dismiss()
                }
            }
            radioButton3.apply {
                setText("Phát hiện người")
                setOnClickListener {
                    onClickItemEvent?.invoke(6)
                    dismiss()
                }
            }
        }

        /*val listItems = arrayOf("Tất cả", "Phát hiện chuyển động", "Phát hiện người")
        builder.setTitle("Chọn sự kiện")
        builder.setSingleChoiceItems(listItems, 0) { dialogInterface, i ->
            onEventClick?.invoke(listItems[i])
            dismiss()
        }*/
        return builder.create()
    }

    private fun getTypeDialog(): Dialog? {
        val builder = AlertDialog.Builder(requireActivity())
        _bindingDialog = DialogBottomBinding.inflate(LayoutInflater.from(activity))
        builder.setView(bindingDialog.root)

        bindingDialog.apply {
            radioGroup.visibility = View.VISIBLE
            recyclerViewListcam.visibility = View.GONE
            dialogTitle.setText("Chọn Camera")
            radioButton1.apply {
                setText("HC2/HC3")
                setOnClickListener {
                    onClickItem?.invoke(bindingDialog.radioButton1.text.toString())
                    dismiss()
                }
            }
            radioButton2.apply {
                setText("HC22/HC32")
                setOnClickListener {
                    onClickItem?.invoke(bindingDialog.radioButton2.text.toString())
                    dismiss()
                }
            }
            radioButton3.visibility = View.GONE
        }

        return builder.create()
    }

    private fun getCameraDialog(): Dialog? {
        val builder = AlertDialog.Builder(requireActivity())
        _bindingDialog = DialogBottomBinding.inflate(LayoutInflater.from(activity))
        builder.setView(bindingDialog.root)

        val layoutManager = LinearLayoutManager(activity)

        bindingDialog.apply {
            radioGroup.visibility = View.GONE
            recyclerViewListcam.visibility = View.VISIBLE
            dialogTitle.setText("Chọn Camera")

            recyclerViewListcam.layoutManager = layoutManager
            recyclerViewListcam.setHasFixedSize(true)
            recyclerViewListcam.adapter = cameraAdapter
        }

        return builder.create()
    }
}