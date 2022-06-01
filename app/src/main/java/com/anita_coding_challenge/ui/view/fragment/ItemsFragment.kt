package com.anita_coding_challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.anita_coding_challenge.databinding.ItemsLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Anita Kiran on 6/1/2022.
 */
@AndroidEntryPoint
class ItemsFragment: Fragment() {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: ItemsLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.items_layout,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        loadData()
        return binding.root
    }

    private fun loadData(){
        viewModel.getSearchItems("tetris")
        viewModel.items.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}