package com.example.shoppingonline.ui.product.adress

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class AddressFormModel(
    private val state: SavedStateHandle
): ViewModel() {
    var name: String?
        get() = state["name"]
        set(value) { state["name"] = value }
    var phone: String?
        get() = state["phone"]
        set(value) { state["phone"] = value }
    var note: String?
        get() = state["note"]
        set(value) { state["note"] = value }
}