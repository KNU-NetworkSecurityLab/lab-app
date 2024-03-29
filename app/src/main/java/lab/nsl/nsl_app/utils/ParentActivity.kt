package lab.nsl.nsl_app.utils

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import lab.nsl.nsl_app.R

open class ParentActivity : AppCompatActivity() {
    var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
    }

    fun showProgress(activity: Activity, text: String) {
        if (progressDialog != null) {
            progressDialog = null
        }

        progressDialog = Dialog(activity)
        progressDialog?.setContentView(R.layout.dialog_progress)
        progressDialog?.findViewById<TextView>(R.id.tv_dialog_progress)?.text = text
        progressDialog?.setCancelable(false) // 다이얼로그 외 터치 시 취소 막음
        progressDialog?.show()
    }

    fun hideProgress() {
        if (progressDialog != null) {
            progressDialog?.dismiss()
        }
        progressDialog = null
    }

    fun showShortToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}