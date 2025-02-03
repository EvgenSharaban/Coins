package com.example.customviewwithoutcompose.presentation.adapters.coin

import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinsAdapter
import com.example.delegateadapter.ContentComparable
import com.example.delegateadapter.DelegateAdapterItem

data class CoinItem(
    val coin: ModelForCoinsAdapter
) : DelegateAdapterItem {

    override fun id(): String = coin.customViewModel.id

    override fun content(): ContentComparable {
        return CoinContent(coin)
    }

    override fun payload(other: Any): DelegateAdapterItem.PayloadAble {
        if (other is CoinItem && other.coin.customViewModel.id != coin.customViewModel.id) {
            return ChangePayload.CoinExpandChanged(coin.isExpanded)
        }
        return DelegateAdapterItem.PayloadAble.None
    }


    inner class CoinContent(val coin: ModelForCoinsAdapter) : ContentComparable {
        override fun equals(other: Any?): Boolean {
            if (other is CoinContent) {
                return coin == other.coin
            }
            return false
        }

        override fun hashCode(): Int {
            return 125 * coin.hashCode()
        }
    }

    sealed interface ChangePayload : DelegateAdapterItem.PayloadAble {
        data class CoinExpandChanged(val isExpanded: Boolean) : ChangePayload
    }
}
