package com.ligastaball

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ligastaball.databinding.FragmentLevelBinding
import kotlin.math.min


class LevelFragment : Fragment() {


    private lateinit var binding: FragmentLevelBinding
    var level = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLevelBinding.inflate(inflater,container,false)
        binding.theme.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        val list = listOf(binding.imageView5,binding.imageView3,binding.imageView4,binding.imageView6,binding.imageView7,binding.imageView8,binding.imageView9,binding.imageView10,binding.imageView11)
        level = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("level",0)
        for(i in list.indices) {
            list[i].setOnClickListener {
                if(i<=level) {
                    val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    navController.navigate(R.id.gameFragment,Bundle().apply { putInt("level",i) })
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.textView.setTextColor(requireContext().getColor(requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("theme",R.color.light_green)))
        val list = listOf(binding.imageView5,binding.imageView3,binding.imageView4,binding.imageView6,binding.imageView7,binding.imageView8,binding.imageView9,binding.imageView10,binding.imageView11)
        level = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("level",0)
        if(level>0) {
            for (i in 0 until min(level,list.size)) {
                list[i].setTextColor(requireContext().getColor(R.color.green))
                //list[i].setBackgroundResource(R.drawable.level2)
                list[i].background.setColorFilter(requireContext().getColor(requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("theme",R.color.light_green)),PorterDuff.Mode.SRC)
            }
        }
    }
}