package com.example.customviewwithoutcompose.presentation.adapters

interface DelegateAdapterItem {

    fun id(): String

    fun content(): ContentComparable

    fun payload(other: Any): PayloadAble = PayloadAble.None

    interface PayloadAble {
        object None : PayloadAble
    }
}