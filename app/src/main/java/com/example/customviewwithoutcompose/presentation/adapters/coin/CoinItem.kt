package com.example.customviewwithoutcompose.presentation.adapters.coin

import com.example.customviewwithoutcompose.presentation.adapters.ContentComparable
import com.example.customviewwithoutcompose.presentation.adapters.DelegateAdapterItem
import com.example.customviewwithoutcompose.presentation.models.coin.ModelForCoinsAdapter

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
                return coin.isExpanded == other.coin.isExpanded &&
                        coin.customViewModel.descriptionText == other.coin.customViewModel.descriptionText &&
                        coin.customViewModel.price == other.coin.customViewModel.price &&
                        coin.customViewModel.logo == other.coin.customViewModel.logo &&
                        coin.customViewModel.creationDate == other.coin.customViewModel.creationDate &&
                        coin.customViewModel.rank == other.coin.customViewModel.rank &&
                        coin.customViewModel.shortNameText == other.coin.customViewModel.shortNameText &&
                        coin.customViewModel.nameText == other.coin.customViewModel.nameText
            }
            return false
        }

        override fun hashCode(): Int {
            var result = coin.customViewModel.creationDate.hashCode()
            result = 125 * result + coin.customViewModel.descriptionText.hashCode()
            return result
        }
    }

    sealed interface ChangePayload : DelegateAdapterItem.PayloadAble {
        data class CoinExpandChanged(val isExpanded: Boolean) : ChangePayload
    }
}
