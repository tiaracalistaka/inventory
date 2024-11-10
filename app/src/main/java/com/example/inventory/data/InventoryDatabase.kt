package com.example.inventory.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Kelas abstrak InventoryDatabase yang mewarisi RoomDatabase dan berfungsi sebagai titik akses utama ke database Room.
 * @Database: Anotasi dengan parameter `entities` yang mencakup daftar entity yang dimiliki database, di sini hanya `Item`.
 * version = 1: Menentukan versi database, digunakan untuk keperluan migrasi jika ada perubahan struktur database.
 * exportSchema = false: Menghentikan Room dari menghasilkan file schema, digunakan untuk menyimpan history schema dalam migrasi.
 * @Volatile memastikan bahwa perubahan pada variabel Instance langsung terlihat oleh thread lain.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        // - Parameter context: Diperlukan untuk membangun database.
        // - synchronized(this): Memastikan hanya satu instance database yang dibangun pada waktu yang bersamaan.
        // - Room.databaseBuilder: Membangun database dengan nama "item_database".
        fun getDatabase(context: Context): InventoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}