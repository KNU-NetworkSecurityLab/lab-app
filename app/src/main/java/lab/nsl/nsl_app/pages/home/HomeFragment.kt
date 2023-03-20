package lab.nsl.nsl_app.pages.home

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lab.nsl.nsl_app.databinding.FragmentHomeBinding
import lab.nsl.nsl_app.pages.book.BookListActivity
import lab.nsl.nsl_app.pages.introduce.IntroduceActivity
import lab.nsl.nsl_app.pages.member.MemberActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val display = activity?.windowManager?.defaultDisplay
        val size = Point()
        display?.getRealSize(size)

        val deviceWidth = size.x
        val containerSize = (deviceWidth * 0.4).toInt()

        binding.run {

            btnHomeBooks.setOnClickListener {
                val intent = Intent(requireActivity(), BookListActivity::class.java)
                startActivity(intent)
            }

            btnHomeLabMember.setOnClickListener {
                startActivity(Intent(requireActivity(), MemberActivity::class.java))
            }

            btnHomeLabShow.setOnClickListener {
                startActivity(Intent(requireActivity(), IntroduceActivity::class.java))
            }
        }

        return binding.root
    }
}