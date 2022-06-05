package com.anita_coding_challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
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
    private var searchStrTetris = "tetris"
    private val itemsAdapter = ItemsAdapter()
    private var itemsList: ArrayList<Item> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemsLayoutBinding.inflate(inflater,container,false)

        setupUI()
        loadData()
        return binding.root
    }

    private fun loadData(){
        //load default data from api using 'Tetris'
        viewModel.getSearchItems(searchStrTetris)
        //observe searched items
        viewModel.items.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Status.SUCCESS -> {
                    // get total page numbers from total item count
                    totalPageCount = it.data?.total_count?.div(10) ?: 0
                    binding.progressBar.visibility = View.GONE
                    binding.rowLoader.visibility = View.GONE
                    // set list
                    it.data?.items?.let { items->
                        setListItems(items)
                    }
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

    // set up ui elements
    private fun setupUI(){
        // set recyclerview adapter
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = itemsAdapter
        }

        // set default search string "Tetris" in edittext
        binding.searchEditText.setText(searchStrTetris)

        // set after text change listener to edittext
        binding.searchEditText.doAfterTextChanged {
            //to avoid unnesscessary api call
            if(it?.length!! > 3) {
                // clear previously fetched data and start with new searched items
                pageNo = 1
                itemsList.clear()
                viewModel.getSearchItems(it.toString())
            }
        }

        // clear edittext
        binding.imgCross.setOnClickListener {
            binding.searchEditText.setText("")
        }

        // recyclerview scroll listerner for pagination
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && pageNo <= totalPageCount) {
                    pageNo++
                    // visible loader at the bottom
                    binding.rowLoader.visibility = View.VISIBLE
                    // api call for fetching more items from the api
                    viewModel.getSearchItems(binding.searchEditText.text.toString(),itemCount,pageNo)
                }
            }
        })
    }

    // set data in list
    private fun setListItems(data: ArrayList<Item>){
        itemsList.addAll(data)
        itemsAdapter.setContentList(itemsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}