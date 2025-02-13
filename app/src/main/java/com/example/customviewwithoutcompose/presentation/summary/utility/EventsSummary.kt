package com.example.customviewwithoutcompose.presentation.summary.utility


sealed interface EventsSummary {

    class MessageForUser(val message: String) : EventsSummary

}