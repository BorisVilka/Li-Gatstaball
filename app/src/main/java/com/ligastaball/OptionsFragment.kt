package com.ligastaball

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.Navigation
import com.ligastaball.databinding.FragmentOptionsBinding


class OptionsFragment : Fragment() {

    private lateinit var binding: FragmentOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOptionsBinding.inflate(inflater,container,false)
        var music = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",false)
        var sound = requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("sound",false)
        var color = requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("theme",R.color.light_green)
        binding.switch1.thumbTintList = AppCompatResources.getColorStateList(requireContext(),color)
        binding.switch2.thumbTintList = AppCompatResources.getColorStateList(requireContext(),color)
        binding.theme.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        binding.switch1.isChecked = music
        binding.switch2.isChecked = sound
        binding.textView.setTextColor(requireContext().getColor(color))
        binding.imageView12.background.setColorFilter(requireContext().getColor(color),PorterDuff.Mode.SRC)
        binding.imageView13.background.setColorFilter(requireContext().getColor(color),PorterDuff.Mode.SRC)
        if(color==R.color.white) {
            binding.switch1.thumbTintList = AppCompatResources.getColorStateList(requireContext(),R.color.green)
            binding.switch2.thumbTintList = AppCompatResources.getColorStateList(requireContext(),R.color.green)
            binding.imageView12.setColorFilter(requireContext().getColor(R.color.green))
            binding.imageView13.setColorFilter(requireContext().getColor(R.color.green))
        }
        if(music) {
            binding.textView2.visibility = View.VISIBLE
            binding.textView3.visibility = View.INVISIBLE
        } else {
            binding.textView2.visibility = View.INVISIBLE
            binding.textView3.visibility = View.VISIBLE
        }
        if(sound) {
            binding.textView4.visibility = View.VISIBLE
            binding.textView5.visibility = View.INVISIBLE
        } else {
            binding.textView4.visibility = View.INVISIBLE
            binding.textView5.visibility = View.VISIBLE
        }
        binding.switch2.setOnCheckedChangeListener { buttonView, isChecked ->
            sound = ! sound
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putBoolean("sound",sound).apply()
            if(sound) {
                binding.textView4.visibility = View.VISIBLE
                binding.textView5.visibility = View.INVISIBLE
            } else {
                binding.textView4.visibility = View.INVISIBLE
                binding.textView5.visibility = View.VISIBLE
            }
        }
        binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            music = ! music
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putBoolean("music",music).apply()
            if(music) {
                binding.textView2.visibility = View.VISIBLE
                binding.textView3.visibility = View.INVISIBLE
            } else {
                binding.textView2.visibility = View.INVISIBLE
                binding.textView3.visibility = View.VISIBLE
            }
        }
        return binding.root
    }


}