package com.ligastaball

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ligastaball.databinding.FragmentEndBinding


class EndFragment : Fragment() {

    private lateinit var binding: FragmentEndBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEndBinding.inflate(inflater,container,false)
        binding.level.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            navController.navigate(R.id.gameFragment,Bundle().apply {
                putInt("level",requireArguments().getInt("level"))
            })
        }
        binding.level.visibility = if(requireArguments().getInt("level")<8) View.VISIBLE else View.INVISIBLE
        binding.theme2.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
        }
        return binding.root
    }


}