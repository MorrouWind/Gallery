package com.example.gallery.screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.R
import com.example.gallery.adapter.ImageAdapter
import com.example.gallery.databinding.FragmentHomeBinding
import com.example.gallery.model.ImageModel
import com.example.gallery.model.SearchModel
import com.example.gallery.service.ApiUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var imageAdapter: ImageAdapter
    private var images: MutableList<ImageModel> = ArrayList()
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = true
    private var page = 1
    private var pageLimit = 30

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val recycler = binding.recyclerView
        val layoutM = GridLayoutManager(requireContext(), 3)

        imageAdapter = ImageAdapter(images){ url, descr ->
                getCurrentImage(imageRegular = url, imageDescr = descr)
            }
        recycler.layoutManager = layoutM
        recycler.adapter = imageAdapter
        getImages()


        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItem: Int = layoutM.childCount
                val totalItem: Int = layoutM.itemCount
                val firstVisibleItemPosition: Int = layoutM.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPosition) >= totalItem &&
                        firstVisibleItemPosition >= 0 &&
                        totalItem >= pageLimit
                    ) {
                        page++
                        getImages()
                    }
                }
            }
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> {
                    val searchView: SearchView = item.actionView as SearchView
                    searchView.setOnQueryTextListener(object : OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            searchImagesRes(query!!)
                            return true
                        }
                        override fun onQueryTextChange(newText: String?): Boolean {
                            return true
                        }
                    })
            }
        }
        return true
    }

    private fun getImages() {
        isLoading = true
        ApiUtils.api.getImagesResponse(page, pageLimit)
            .enqueue(object : Callback<MutableList<ImageModel>> {
                override fun onResponse(
                    call: Call<MutableList<ImageModel>>,
                    response: Response<MutableList<ImageModel>>
                ) {
                    if (response.isSuccessful) {
//                        images.clear()
                        response.body()?.let {
                            images.addAll(it)
                        }
                        imageAdapter.notifyDataSetChanged()
                    }
                    isLoading = false
                    isLastPage = if (images.size > 0) {
                        images.size < pageLimit
                    } else {
                        true
                    }
                }

                override fun onFailure(call: Call<MutableList<ImageModel>>, t: Throwable) {
                    Log.d("Response", "Failed")
                }
            })
    }


    fun searchImagesRes(query: String) {
        ApiUtils.api.searchImages(query)
            .enqueue(object : Callback<SearchModel>{
                override fun onResponse(call: Call<SearchModel>, response: Response<SearchModel>) {
                    images.clear()
                    response.body()?.let {
                        images.addAll(it.results)
                        imageAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                    Log.d("Response", "Failed")
                }
            })

    }

    private fun getCurrentImage(imageRegular: String, imageDescr: String) {
        val direction = HomeFragmentDirections.homeToFullFragment(imageRegular, imageDescr)
        findNavController().navigate(direction)
    }



}