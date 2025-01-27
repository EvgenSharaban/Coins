package com.example.customviewwithoutcompose.presentation

sealed interface Events {

    class MessageForUser(val message: String) : Events
    class PositionToScrolling(val position: Int) : Events

}