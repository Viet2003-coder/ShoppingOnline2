package com.example.shoppingonline

sealed class Destination {
    object MAIN : Destination()
    object LOGIN : Destination()
}