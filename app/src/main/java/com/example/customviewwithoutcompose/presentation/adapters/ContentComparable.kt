package com.example.customviewwithoutcompose.presentation.adapters

interface ContentComparable {
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}