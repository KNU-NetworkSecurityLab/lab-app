package com.example.nsl_app.pages.myPage

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.nsl_app.R
import com.example.nsl_app.databinding.FragmentMyPageBinding
import com.example.nsl_app.pages.loginAndRegister.LoginActivity
import com.example.nsl_app.utils.SharedPreferenceHelper
import com.google.android.material.bottomsheet.BottomSheetDialog

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        binding.run {
            tvMyPageTheme.setOnClickListener {
                Toast.makeText(requireContext(), "지원 예정 기능입니다.", Toast.LENGTH_SHORT).show()
            }

            tvMyPageWithdrawal.setOnClickListener {
                val dlWithdrawal = BottomSheetDialog(requireContext())

                dlWithdrawal.setContentView(R.layout.dialog_withdrawal)
                dlWithdrawal.behavior.isDraggable = false
                dlWithdrawal.setCanceledOnTouchOutside(false)
                dlWithdrawal.setCancelable(false)
                dlWithdrawal.show()

                val btnWithdrawalYes = dlWithdrawal.findViewById<Button>(R.id.btn_withdrawal_yes)
                val btnWithdrawalNo = dlWithdrawal.findViewById<Button>(R.id.btn_withdrawal_no)

                btnWithdrawalNo!!.setOnClickListener {
                    Toast.makeText(requireContext(), "취소하였습니다", Toast.LENGTH_SHORT).show()
                    dlWithdrawal.dismiss()
                }

                btnWithdrawalYes!!.setOnClickListener {
                    Toast.makeText(requireContext(), "탈퇴하였습니다", Toast.LENGTH_SHORT).show()
                    dlWithdrawal.dismiss()
                }
            }

            // 로그아웃 클릭 시
            tvLogout.setOnClickListener {
                val builder = activity.let { AlertDialog.Builder(it) }
                builder.setMessage(R.string.msg_logout_content)
                    .setPositiveButton(R.string.glb_logout,
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            startActivity(Intent(requireContext(), LoginActivity::class.java))
                            SharedPreferenceHelper.setAutoLoginEnable(requireContext(), false)
                            SharedPreferenceHelper.setAutoLoginID(requireContext(), null)
                            SharedPreferenceHelper.setAutoLoginPassword(requireContext(), null)
                            requireActivity().finish()
                        })
                    .setNegativeButton(R.string.glb_cancel,
                        DialogInterface.OnClickListener { dialogInterface, i ->

                        })

                val dialog = builder.create()

                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                }
                dialog.show()
            }
        }

        return binding.root
    }
}