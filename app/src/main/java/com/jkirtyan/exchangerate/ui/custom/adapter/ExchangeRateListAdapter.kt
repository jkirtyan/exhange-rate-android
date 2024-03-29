package com.jkirtyan.exchangerate.ui.custom.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jkirtyan.exchangerate.R
import com.jkirtyan.exchangerate.databinding.ListItemExchangeRateBinding
import com.jkirtyan.exchangerate.entity.ExchangeRateResponse
import com.jkirtyan.exchangerate.ui.viewmodel.ExchangeRateListViewModel
import javax.inject.Inject

class ExchangeRateListAdapter @Inject constructor() :
    RecyclerView.Adapter<ExchangeRateListAdapter.Holder>(),
    TextWatcher,
    Observer<DiffUtil.DiffResult> {

    private val viewModel = ExchangeRateListViewModel().also {
        it.baseChangeAction.observeForever(this)
    }

    var exchangeRate: ExchangeRateResponse? = null
        set(value) {
            if (field == null) notifyDataSetChanged()
            field = value

            viewModel.exchangeRate = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ListItemExchangeRateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_exchange_rate,
            parent,
            false
        )

        return Holder(binding)
    }

    override fun getItemCount() = viewModel.itemCount()

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.position = position
        holder.binding.viewModel = viewModel

        holder.binding.etRate.removeTextChangedListener(this)
        if (viewModel.editable(position)) {
            holder.binding.etRate.addTextChangedListener(this)
        }
    }

    override fun onChanged(diffResult: DiffUtil.DiffResult?) {
        diffResult?.let { diffs ->
            diffs.dispatchUpdatesTo(this)
            recyclerView?.scrollToPosition(0)

            recyclerView?.postOnAnimationDelayed(
                {
                    notifyDataSetChanged() // TODO: improve it to update only the necessary items
                },
                ((recyclerView?.itemAnimator?.moveDuration ?: 250) * 1.2).toLong()
            )


            viewModel.onBaseChangeFinished()
        }
    }

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun afterTextChanged(s: Editable?) {
        // to do nothing
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // to do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.toString()?.toDoubleOrNull()?.let {
            viewModel.enteredValue = it
        }
    }

    class Holder(val binding: ListItemExchangeRateBinding) : RecyclerView.ViewHolder(binding.root)
}