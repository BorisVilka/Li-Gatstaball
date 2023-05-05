package com.ligastaball

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ligastaball.databinding.FragmentGameBinding
import kotlin.math.max


class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGameBinding.inflate(inflater,container,false)
        binding.game.level = requireArguments().getInt("level")
        binding.textView11.text = (requireArguments().getInt("level")+1).toString()
        var color = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getInt("theme",R.color.light_green)
        binding.textView10.setTextColor(requireContext().getColor(color))
        binding.textView9.setTextColor(requireContext().getColor(color))
        binding.level2.setBackgroundColor(requireContext().getColor(color))
        binding.again.setBackgroundColor(requireContext().getColor(color))
        binding.game.setEndListener(object : GameView.Companion.EndListener {
            override fun end() {
                requireActivity().runOnUiThread {
                    if(!binding.game.isWin) binding.fail.visibility = View.VISIBLE
                    else {
                        requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putInt("level",
                            max(requireArguments().getInt("level")+1,requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("level",0))
                            ).apply()
                        val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                        navController.popBackStack()
                        navController.navigate(R.id.endFragment, Bundle().apply {
                            putInt("level",requireArguments().getInt("level")+1)
                        })
                    }
                }
            }

            override fun score(score: Int) {

            }

        })
        binding.imageView14.setOnClickListener {
            binding.game.togglePause()
            binding.pause.visibility = View.VISIBLE
        }
        binding.level2.setOnClickListener {
            binding.pause.visibility = View.GONE
            binding.game.togglePause()
        }
        binding.theme3.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.menu.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.again.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            navController.navigate(R.id.gameFragment,Bundle().apply { putInt("level",requireArguments().getInt("level")) })
        }
        return binding.root
    }


}