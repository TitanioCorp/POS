package com.titaniocorp.pos.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.titaniocorp.pos.database.entity.CategoryEntity

/**
 * Maneja las consultas a la base de datos de pelicula
 * @version 1.0
 * @author Juan Ortiz
 * @date 10/09/2019
 */
@Dao
interface CategoryDao {
    /* SELECT */
    @Query("SELECT * FROM category WHERE category_id = :id LIMIT 1")
    suspend fun getById(id: Long): CategoryEntity

    @Query("SELECT * FROM category WHERE active = 1")
    fun getAll(): LiveData<List<CategoryEntity>>

    /* INSERT */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg items: CategoryEntity): List<Long>

    @Update
    suspend fun update(vararg item: CategoryEntity): Int
}