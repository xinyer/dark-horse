package com.android.architecture.core

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.android.architecture.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    protected abstract val viewModel: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.successResult.observe(viewLifecycleOwner) { result ->
            result?.getMessage(requireContext())?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.purple_500,
                            context?.theme
                        )
                    )
                    .show()
            }
        }
        viewModel.errorResult.observe(viewLifecycleOwner) { result ->
            result?.getMessage(requireContext())?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.red,
                            context?.theme
                        )
                    )
                    .show()
            }
        }
    }

    fun navigate(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment)
            .commit()
    }
}
