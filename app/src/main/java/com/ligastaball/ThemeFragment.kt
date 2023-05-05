package com.ligastaball

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ligastaball.databinding.FragmentThemeBinding


class ThemeFragment : Fragment() {

    private lateinit var binding: FragmentThemeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentThemeBinding.inflate(inflater,container,false)
        binding.theme.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        var ind = requireActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("theme",R.color.light_green)
        set(ind)
        binding.imageView5.setOnClickListener {
            ind = R.color.white
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("theme",ind).apply()
            set(ind)
        }
        binding.imageView3.setOnClickListener {
            ind = R.color.yellow
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("theme",ind).apply()
            set(ind)
        }
        binding.imageView4.setOnClickListener {
            ind = R.color.light_green
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("theme",ind).apply()
            set(ind)
        }
        binding.imageView6.setOnClickListener {
            ind = R.color.cyan
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("theme",ind).apply()
            set(ind)
        }
        binding.imageView7.setOnClickListener {
            ind = R.color.blue
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("theme",ind).apply()
            set(ind)
        }
        binding.imageView8.setOnClickListener {
            ind = R.color.orange
            requireContext().getSharedPreferences("prefs",Context.MODE_PRIVATE).edit().putInt("theme",ind).apply()
            set(ind)
        }
        return binding.root
    }

    fun set(ind:Int) {
        binding.textView.setTextColor(requireContext().getColor(ind))
        binding.imageView3.setColorFilter(android.R.color.transparent,PorterDuff.Mode.SRC)
        binding.imageView4.setColorFilter(android.R.color.transparent,PorterDuff.Mode.SRC)
        binding.imageView5.setColorFilter(android.R.color.transparent,PorterDuff.Mode.SRC)
        binding.imageView6.setColorFilter(android.R.color.transparent,PorterDuff.Mode.SRC)
        binding.imageView7.setColorFilter(android.R.color.transparent,PorterDuff.Mode.SRC)
        binding.imageView8.setColorFilter(android.R.color.transparent,PorterDuff.Mode.SRC)
        when(ind) {
            R.color.white -> binding.imageView5.setColorFilter(requireContext().getColor(R.color.green),PorterDuff.Mode.SRC)
            R.color.yellow -> binding.imageView3.setColorFilter(requireContext().getColor(R.color.white),PorterDuff.Mode.SRC)
            R.color.light_green -> binding.imageView4.setColorFilter(requireContext().getColor(R.color.white),PorterDuff.Mode.SRC)
            R.color.cyan -> binding.imageView6.setColorFilter(requireContext().getColor(R.color.white),PorterDuff.Mode.SRC)
            R.color.blue -> binding.imageView7.setColorFilter(requireContext().getColor(R.color.white),PorterDuff.Mode.SRC)
            R.color.orange -> binding.imageView8.setColorFilter(requireContext().getColor(R.color.white),PorterDuff.Mode.SRC)
        }
    }

}