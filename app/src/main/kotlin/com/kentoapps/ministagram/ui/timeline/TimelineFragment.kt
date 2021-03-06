package com.kentoapps.ministagram.ui.timeline

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.fragment.NavHostFragment
import com.kentoapps.ministagram.R
import com.kentoapps.ministagram.databinding.TimelineFragmentBinding
import com.kentoapps.ministagram.di.Injectable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class TimelineFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory).get(TimelineViewModel::class.java)
    }
    private lateinit var binding: TimelineFragmentBinding
    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        binding = TimelineFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = TimelineAdapter(viewModel)
        viewModel.posts.subscribe { adapter.submitList(it) }.addTo(disposables)
        binding.timelineRecycler.adapter = adapter

        viewModel.openCommentEvent.observe(this, Observer {
            NavHostFragment.findNavController(this)
                    .navigate(TimelineFragmentDirections.toComment(it))
        })

        if (savedInstanceState == null) viewModel.getPostList()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_timeline, menu)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }
}
