package com.example.nsl_app.pages.schedule

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nsl_app.R
import com.example.nsl_app.databinding.FragmentSchduleAddTagDialogBinding
import com.example.nsl_app.adapters.SchTagAddAdapter
import com.example.nsl_app.adapters.TagAddClickListener


class ScheduleAddTagDialogFragment : DialogFragment() {
    private lateinit var binding:FragmentSchduleAddTagDialogBinding
    lateinit var tagAddClickListener: TagAddClickListener
    lateinit var tags: ArrayList<String>

    fun newInstance(tags: ArrayList<String>, tagAddClickListener: TagAddClickListener) : ScheduleAddTagDialogFragment {
        val scheduleAddTagDialogFragment = ScheduleAddTagDialogFragment()
        this.tags = tags
        this.tagAddClickListener = tagAddClickListener
        return scheduleAddTagDialogFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        val displayMetrics = DisplayMetrics()
        requireActivity().window.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val deviceHeight = displayMetrics.heightPixels
        val deviceWidth = displayMetrics.widthPixels

        //try 2
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = (deviceWidth * 0.9).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val window = dialog!!.window
        window!!.attributes = lp

        dialog?.window?.setBackgroundDrawable(context?.getDrawable(R.drawable.round_white_10));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSchduleAddTagDialogBinding.inflate(inflater, container, false)

        val adapter = SchTagAddAdapter(requireContext(), tags)

        adapter.tagAddClickListener = tagAddClickListener

        binding.run {

            listSchTagsAdd.adapter = adapter
            listSchTagsAdd.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter.notifyDataSetChanged()


            if(tags.isEmpty()) {
                listSchTagsAdd.visibility = View.GONE
                tvSchAddNoTags.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
}