package com.android.architecture.travel.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.android.architecture.core.BaseFragment
import com.android.architecture.core.ErrorResult
import com.android.architecture.databinding.OrderFragmentBinding
import com.android.architecture.travel.model.OrderDomainModel
import java.lang.IllegalArgumentException

class OrderFragment : BaseFragment<OrderViewModel>() {

    companion object {
        const val KEY_ORDER_ID = "KEY_ORDER_ID"
    }

    override val viewModel: OrderViewModel by viewModels {
        val orderId = arguments?.getString(KEY_ORDER_ID) ?: throw IllegalArgumentException()
        OrderViewModelFactory(orderId)
    }

    private lateinit var binding: OrderFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            submitButton.setOnClickListener {
                viewModel.enterprisePay()
            }
            viewModel.order.observe(viewLifecycleOwner) {
                hideErrorView()
                updateOrderView(it)
            }
            viewModel.payRequestLoading.observe(viewLifecycleOwner) {
                if (it) showPayRequestLoading() else hidePayRequestLoading()
            }
            viewModel.retryLoading.observe(viewLifecycleOwner) {
                if (errorView.isVisible) {
                    retryLoading.isVisible = it
                    retryButton.isVisible = !it
                }
            }
            viewModel.loadDataLoading.observe(viewLifecycleOwner) {
                loadDataProgress.isVisible = it
            }
            viewModel.paymentStatus.observe(viewLifecycleOwner) { paid ->
                submitButton.isEnabled = !paid
            }
            viewModel.paymentStatusText.observe(viewLifecycleOwner) {
                statusTextView.setText(it)
            }
            viewModel.submitButtonText.observe(viewLifecycleOwner) {
                submitButton.setText(it)
            }
            viewModel.paymentErrorResult.observe(viewLifecycleOwner) {
                if (it != null) showPaymentErrorView(it) else hideErrorView()
            }
            viewModel.getOrderErrorResult.observe(viewLifecycleOwner) {
                if (it != null) showGetOrderErrorView(it) else hideErrorView()
            }
        }
    }

    private fun updateOrderView(order: OrderDomainModel?) {
        order?.let {
            binding.apply {
                titleTextView.text = it.title()
                orderIdTextView.text = it.id()
                travelDateTextView.text = it.departureDate
                originTextView.text = it.origin()
                airlinesTextView.text = it.airlines
                destinationTextView.text = it.destination()
                passengerTextView.text = it.passenger()
                priceTextView.text = it.price()
                submitButton.isVisible = true
            }
        }
    }

    private fun showPayRequestLoading() {
        binding.submitLoading.isVisible = true
        binding.submitButton.isInvisible = true
    }

    private fun hidePayRequestLoading() {
        binding.submitLoading.isVisible = false
        binding.submitButton.isInvisible = false
    }

    private fun showPaymentErrorView(error: ErrorResult) {
        binding.errorView.isVisible = true
        binding.errorTextView.setText(error.messageRes)
        binding.retryButton.setOnClickListener {
            viewModel.getEnterprisePayConfirmation()
        }
    }

    private fun showGetOrderErrorView(error: ErrorResult) {
        binding.errorView.isVisible = true
        binding.errorTextView.setText(error.messageRes)
        binding.retryButton.setOnClickListener {
            viewModel.getOrder()
        }
    }

    private fun hideErrorView() {
        binding.errorView.isVisible = false
    }
}
