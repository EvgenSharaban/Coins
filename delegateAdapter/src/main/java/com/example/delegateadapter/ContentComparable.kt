package com.example.delegateadapter

interface ContentComparable {
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}