package com.example.customviewwithoutcompose.presentation.summary


sealed interface EventsSummary {

    class MessageForUser(val message: String) : EventsSummary

}