package com.example.simplesqlv2

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.simplesqlv2.databinding.FragmentSecondBinding
import com.example.simplesqlv2.roomdb.Sensordata
import com.example.simplesqlv2.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private val vm: AppViewModel by activityViewModels()

    private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        vm.sensordata.observe(viewLifecycleOwner) { sensordata ->
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, sensordata)
            binding.listViewData.adapter = adapter
        }

        binding.buttonFilterLoc.setOnClickListener {
            val input = binding.editLoc.text.toString()
            vm.getSensordataFilterByLocation(input)
        }

        binding.buttonFilterTemp.setOnClickListener {
            val input = binding.editTemp.text.toString()
            vm.getSensordataFilterByTemperature(input)
        }

        binding.buttonFilterDate.setOnClickListener {
            val input = binding.editDate.text.toString()
            vm.getSensordataFilterByDate(input)
        }

        binding.buttonFilterHottest.setOnClickListener {
            val input = binding.editHottest.text.toString()
            vm.getSensordataFilterByHottest(input)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}