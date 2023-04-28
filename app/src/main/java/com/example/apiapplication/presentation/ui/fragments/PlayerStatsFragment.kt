package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiapplication.R
import com.example.apiapplication.data.Hero
import com.example.apiapplication.databinding.FragmentPlayerstatsBinding
import com.example.apiapplication.presentation.ui.adapters.MyAdapter
import com.example.apiapplication.presentation.viewmodel.MainViewModel

class PlayerStatsFragment : Fragment()  {
    private lateinit var binding: FragmentPlayerstatsBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_playerstats, container, false)
        binding = FragmentPlayerstatsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView = requireView().findViewById(R.id.myRecyclerView)
        myAdapter = MyAdapter()
        recyclerView.adapter = myAdapter

        /*val recyclerView:RecyclerView = requireView().findViewById(R.id.myRecyclerView)
        recyclerView.setAdapter(myAdapter)*/
    }

}