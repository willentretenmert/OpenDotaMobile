package com.example.apiapplication.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apiapplication.R
import com.example.apiapplication.databinding.FragmentMatchStatsBinding
import com.example.apiapplication.presentation.viewmodel.MatchStatsViewModel

class MatchStatsFragment : Fragment() {


    private lateinit var viewModel: MatchStatsViewModel
    private lateinit var binding: FragmentMatchStatsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MatchStatsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMatchStatsBinding.inflate(layoutInflater, container, false)
        return inflater.inflate(R.layout.fragment_match_stats, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}

