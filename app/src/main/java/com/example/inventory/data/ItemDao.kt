package com.example.inventory.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


// Interface DAO untuk mengelola operasi CRUD pada tabel 'items' di database Room, termasuk:
// - insert: Menambahkan item baru, mengabaikan jika ada konflik (primary key sama).
// - update: Memperbarui data item yang ada.
// - delete: Menghapus item tertentu.
// - getItem: Mengambil item berdasarkan id, mengembalikan data secara real-time menggunakan Flow.
// - getAllItems: Mengambil semua item, diurutkan berdasarkan nama secara ascending, juga dengan Flow untuk pembaruan real-time.
@Dao
interface ItemDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from items WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

    @Query("SELECT * from items ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

}