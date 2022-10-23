package com.example.wahyustoryapp.ui.main.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wahyustoryapp.*
import com.example.wahyustoryapp.constant.MapArgs
import com.example.wahyustoryapp.preferences.AuthPreference
import com.example.wahyustoryapp.data.database.Story
import com.example.wahyustoryapp.databinding.FragmentHomeBinding
import com.example.wahyustoryapp.di.Injection
import com.example.wahyustoryapp.helper.Async
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! //dari dokumentasinya begini, memakai double bang
    //( menghindari memory leaks)

    private lateinit var adapter: HomeAdapter

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory(Injection.provideStoryRepository(requireActivity()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.requestApplyInsets(binding.coorLayout)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        binding.fab.setOnClickListener(this)
    }

    private fun observeViewModel() {
        adapter = HomeAdapter()

        binding.rvHome.adapter = adapter
        adapter.setOnclick(object : HomeAdapter.OnItemCallbackListener {
            override fun setButtonClickListener(
                data: Story,
                image: View,
                name: View,
                desc: View,
                date: View
            ) {
                val extras = FragmentNavigatorExtras(
                    image to "imageTarget",
                    name to "titleTarget",
                    desc to "descTarget",
                    date to "dateTarget"
                )
                val go = HomeFragmentDirections.actionHomeFragmentToDetailFragment(data)
                findNavController().navigate(go, extras)
            }
        })
        viewModel.story.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            adapter.addLoadStateListener {
                val mediatorLoadState = it.mediator?.refresh
                if(mediatorLoadState is LoadState.Error){
                    Toast.makeText(requireActivity(), "Couldn't refresh feed", Toast.LENGTH_SHORT).show()
                }
            }

        }

        viewModel.refreshDb.observe(viewLifecycleOwner) {
            when (it) {
                is Async.Error -> {
                    binding.progressBar2.gone()
                    Toast.makeText(requireActivity(), it.error, Toast.LENGTH_SHORT).show()
                }
                is Async.Loading -> {
                    binding.progressBar2.visible()
                }
                is Async.Success -> {
                    binding.progressBar2.gone()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val orientation = requireActivity().resources.configuration.orientation
        val manager = when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(requireActivity())
            else -> GridLayoutManager(requireActivity(), 2)
        }
        binding.rvHome.layoutManager = manager
        binding.rvHome.setHasFixedSize(true)
    }

    private fun setupToolbar() {
        val navCont = findNavController()
        navCont.graph.setStartDestination(R.id.homeFragment)
        val appBarConfig = AppBarConfiguration(navCont.graph)
        binding.toolbar.setupWithNavController(navCont, appBarConfig)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_setting -> {
                    val go = HomeFragmentDirections.actionHomeFragmentToSettingFragment()
                    findNavController().navigate(go)
                    true
                }
                R.id.action_refresh -> {
//                    viewModel.refreshDatabase()
                    adapter.refresh()
                    true
                }
                R.id.action_maps -> {
                    val go =
                        HomeFragmentDirections.actionHomeFragmentToMapsFragment(MapArgs.CheckAllMaps)
                    findNavController().navigate(go)
                    true
                }
                R.id.action_credit -> {
                    findNavController().navigate(R.id.action_homeFragment_to_creditFragment)
                    true
                }
                R.id.action_logout -> {
                    lifecycleScope.launch {
                        viewModel.clearDatabase()
                        val pref = AuthPreference.getInstance(requireActivity().authDataStore)
                        pref.logout()
                        val go = MainNavDirections.actionGlobalLoginFragment2()
                        findNavController().navigate(go)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding.fab -> {
                val go = HomeFragmentDirections.actionHomeFragmentToAddStoryFragment()
                findNavController().navigate(go)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}