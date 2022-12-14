package com.lugares.ui.lugar

import LugarAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lugares.R
import com.lugares.databinding.FragmentLugarBinding
import com.lugares.viewmodel.LugarViewModel

class LugarFragment : Fragment() {

    private var _binding: FragmentLugarBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var lugarViewModel: LugarViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         lugarViewModel =
            ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentLugarBinding.inflate(inflater, container, false)

        binding.addLugar.setOnClickListener {
            findNavController().navigate(R.id.action_nav_lugar_to_addLugarFragment)
        }
        //Activar el RecyclerView
        val lugarAdapter=LugarAdapter()
        val reciclador= binding.reciclador
        reciclador.adapter=lugarAdapter
        reciclador.layoutManager=LinearLayoutManager(requireContext())

        //lugarViewModel=ViewModelProvider(this)[LugarViewModel::class.java]

        lugarViewModel.getAllData.observe(viewLifecycleOwner){
            lugarAdapter.setData(it)
        }

        return binding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}