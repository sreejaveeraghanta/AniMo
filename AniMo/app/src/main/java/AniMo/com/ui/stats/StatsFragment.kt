package AniMo.com.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import AniMo.com.databinding.FragmentStatsBinding
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val statsViewModel =
            ViewModelProvider(this).get(StatsViewModel::class.java)

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        val username = sharedPreferences.getString("username", "")

        if (username != null && name != null) {
            statsViewModel.getUserData(username, name)
            val treats: TextView = binding.treats
            val tasksCompleted: TextView = binding.tasksCompleted
            val timeSpent: TextView = binding.timeSpent
            val visitors: TextView = binding.visitors
            val visited: TextView = binding.visited

            statsViewModel.user.observe(viewLifecycleOwner) {
                treats.text = it.treats.toString()
                tasksCompleted.text = it.tasksCompleted.toString()
                timeSpent.text = it.timeSpent.toString()
                visitors.text = it.visitors.toString()
                visited.text = it.visited.toString()
            }
        }

        val textView: TextView = binding.textStats
        statsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}