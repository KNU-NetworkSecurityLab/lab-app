package lab.nsl.nsl_app.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lab.nsl.nsl_app.R
import lab.nsl.nsl_app.models.BookItem
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import lab.nsl.nsl_app.databinding.ItemLanguageBinding
import lab.nsl.nsl_app.models.LanguagesModel
import lab.nsl.nsl_app.utils.LanguageColorConstants

class LangAdapter(val context: Context, private val langItems: ArrayList<LanguagesModel>) :
    RecyclerView.Adapter<LangAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_language, parent, false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.run {
            tvItemLangName.text = langItems[position].languageName
            tvItemLangRate.text = langItems[position].languageRate

            val textColor = ColorStateList.valueOf(
                LanguageColorConstants.colorList.find { it.first == langItems[position].languageName }?.second
                    ?: Color.BLACK
            )

            tvItemLangName.setTextColor(textColor)
        }
    }

    override fun getItemCount(): Int {
        return langItems.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemLanguageBinding.bind(itemView)
    }
}