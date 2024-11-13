/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import java.text.NumberFormat

/**
 * ViewModel to validate and insert items in the Room database.
 *
 * ItemEntryViewModel - ViewModel yang bertugas untuk validasi dan penyimpanan data item ke database Room. ViewModel ini menyimpan dan memperbarui status UI terkait item.
 * @property itemsRepository Repository yang bertanggung jawab mengelola data item.
 *
 */
class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {


    /**
     * Menyimpan status UI item saat ini (itemUiState), yang berisi detail item dan validitas entri. State ini hanya dapat diubah oleh ViewModel.
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Memperbarui itemUiState dengan nilai itemDetails yang diterima sebagai argumen.
     * Metode ini juga memvalidasi input item untuk memastikan data yang dimasukkan valid.
     *
     * @param itemDetails Objek berisi data item yang akan diperbarui pada state UI.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }
    /**
     * Memvalidasi input dari itemDetails. Mengecek apakah nama, harga, dan jumlah
     * tidak kosong atau tidak valid.
     *
     * @param uiState Data detail item yang akan divalidasi. Nilai defaultnya adalah itemDetails dari itemUiState.
     * @return Boolean yang menunjukkan apakah input valid.
     */
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Ekstensi untuk mengonversi Item menjadi ItemUiState. Berguna untuk menampilkan data item
 * di layar dengan status validasi entri.
 *
 * @param isEntryValid Menentukan apakah entri dianggap valid dalam konversi ini.
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Ekstensi untuk mengonversi Item menjadi ItemDetails, memetakan data dari format database
 * ke format UI yang siap untuk ditampilkan atau dimodifikasi di layar.
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString()
)
