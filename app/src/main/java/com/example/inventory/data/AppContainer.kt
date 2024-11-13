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

package com.example.inventory.data

import android.content.Context

/**
 * AppContainer - Interface ini bertindak sebagai wadah DI untuk menyediakan instance dari
 * `ItemsRepository` di seluruh aplikasi. Hal ini memungkinkan penggunaan `ItemsRepository`
 * secara konsisten tanpa perlu menginisialisasi ulang di setiap komponen yang membutuhkan.
 *
 * @property itemsRepository Objek yang bertugas mengelola data item.
 */
interface AppContainer {
    val itemsRepository: ItemsRepository
}

/**
 * AppDataContainer - Implementasi dari `AppContainer` yang bertugas menyediakan instance dari
 * `OfflineItemsRepository`. Class ini menggunakan konteks aplikasi untuk mendapatkan akses
 * ke `InventoryDatabase` dan menginisialisasi `ItemsRepository`.
 *
 * @param context Context aplikasi yang digunakan untuk mendapatkan instance dari database.
 *
 * @property itemsRepository Menggunakan `lazy` untuk menunda inisialisasi `OfflineItemsRepository`
 * sampai pertama kali digunakan, menghemat resource aplikasi.
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [ItemsRepository]
     */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }
}
