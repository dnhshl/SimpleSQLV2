package com.example.simplesqlv2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simplesqlv2.databinding.FragmentFirstBinding
import com.example.simplesqlv2.roomdb.Sensordata
import com.example.simplesqlv2.roomdb.convertDateStringToLong
import com.example.simplesqlv2.viewmodel.AppViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val vm: AppViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        vm.allSensordata.observe(viewLifecycleOwner) { sensordata ->
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, sensordata)
            binding.listView.adapter = adapter
        }

        binding.btnAdd.setOnClickListener {
            writeDataToDb()
        }

        binding.buttonDelete.setOnClickListener {
            val input = binding.editDelete.text.toString()
            vm.deleteById(input)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun writeDataToDb() {
        val sRaum = binding.etRoom.text.toString()
        val sTemp = binding.etTemp.text.toString()
        val sHum = binding.etAir.text.toString()
        val sDate = binding.etDate.text.toString()

        if (sRaum.isEmpty() || sTemp.isEmpty() || sHum.isEmpty() || sDate.isEmpty()) {
            Log.i(">>>>", getString(R.string.eingabefehler))
        } else {
            val temp = sTemp.toInt()
            val hum = sHum.toInt()
            val date = convertDateStringToLong(sDate)
            vm.insertSensordata(Sensordata(location = sRaum, temp = temp, hum = hum, date = date))
            Log.i(">>>>", "$sRaum $sTemp $sHum $sDate")
        }
    }
}