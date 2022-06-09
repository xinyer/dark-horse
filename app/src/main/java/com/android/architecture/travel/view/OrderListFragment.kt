package com.android.architecture.travel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.architecture.R
import com.android.architecture.core.BaseFragment
import com.android.architecture.databinding.OrderListFragmentBinding
import com.android.architecture.travel.view.OrderFragment.Companion.KEY_ORDER_ID

class OrderListFragment : BaseFragment<OrderListViewModel>() {

    override val viewModel: OrderListViewModel by viewModels()

    private lateinit var binding: OrderListFragmentBinding
    private val orderListAdapter = OrderListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderListView.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = orderListAdapter.apply {
                setOnItemClickListener { adapter, view, position ->
                    val id = (adapter.data[position] as String).substring(1, 12)
                    navigateToOrderFragment(id)
                }
            }
        }
        viewModel.orderList.observe(viewLifecycleOwner) {
            orderListAdapter.data = it.toMutableList()
        }
    }

    private fun navigateToOrderFragment(id: String) {
        activity?.findNavController(R.id.nav_host_fragment)?.navigate(
            R.id.action_orderList_to_order,
            bundleOf(KEY_ORDER_ID to id)
        )
    }
}
