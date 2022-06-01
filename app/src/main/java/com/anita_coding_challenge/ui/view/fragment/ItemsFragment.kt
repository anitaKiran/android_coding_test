package com.anita_coding_challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
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
    private var pageNo = 1
    private var itemCount = 10
    private var totalPageCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.items_layout,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupUI()
        loadData()
        return binding.root
    }

    private fun loadData(){
        viewModel.getSearchItems("tetris",itemCount,pageNo)
        viewModel.items.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    totalPageCount = it.data?.total_count?.div(10) ?: 0
                    binding.progressBar.visibility = View.GONE
                    binding.rowLoader.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rowLoader.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rowLoader.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupUI(){

        binding.searchEditText.setText("tetris")
        binding.searchEditText.doAfterTextChanged {
            if(it?.length!! > 3) {
                viewModel.getSearchItems(it.toString(),itemCount,pageNo)
            }
        }

        binding.imgCross.setOnClickListener {
            binding.searchEditText.setText("")
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && pageNo <= totalPageCount) {
                        pageNo++
                        binding.rowLoader.visibility = View.VISIBLE
                        viewModel.getSearchItems("tetris",itemCount,pageNo)
                    }
                }
            })
    }
}