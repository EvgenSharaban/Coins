package com.example.delegateadapter

interface DelegateAdapterItem {

    fun id(): String

    fun content(): ContentComparable

    fun payload(other: Any): PayloadAble = PayloadAble.None

    interface PayloadAble {
        object None : PayloadAble
    }
}