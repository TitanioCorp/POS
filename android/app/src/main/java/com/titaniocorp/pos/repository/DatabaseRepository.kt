package com.titaniocorp.pos.repository

import android.content.Context
import android.os.Environment
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.ImportDatabaseException
import com.titaniocorp.pos.util.getCode
import com.titaniocorp.pos.util.toFormatFileString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

/**
 * Maneja el export e import de base de datos.
 * @author Juan Ortiz
 * @date 23/05/2020
 */
@ExperimentalCoroutinesApi
class DatabaseRepository @Inject constructor(private val applicationContext: Context):  BaseRepository(){
    fun export(): Flow<Resource<Boolean>> {
        val dataDirectory = Environment.getDataDirectory()
        val databaseDirectoryPath= "/data/${BuildConfig.APPLICATION_ID}/databases/$DATABASE_NAME"

        val exportDirectory = applicationContext.getExternalFilesDir("databases/export")
        val exportFilename = "database-v${BuildConfig.VERSION_NAME}-${Date().toFormatFileString()}-"

        val flow: Flow<Resource<Boolean>> = flow{
            File(dataDirectory, "$databaseDirectoryPath.db").apply {
                File.createTempFile(exportFilename, ".db", exportDirectory).also {
                    copyTo(it, true)
                }
            }

            File(dataDirectory, "$databaseDirectoryPath.db-shm").apply {
                File.createTempFile(exportFilename, ".db-shm", exportDirectory).also {
                    copyTo(it, true)
                }
            }

            File(dataDirectory, "$databaseDirectoryPath.db-wal").apply {
                File.createTempFile(exportFilename, ".db-wal", exportDirectory).also {
                    copyTo(it, true)
                }
            }

            emit(Resource.success(true))
        }

        with(flow){
            onStart { Resource.loading(null) }
            catch {exception ->
                with(exception){
                    emit(Resource.error<Boolean>(null, getCode(), message))
                }
            }
            flowOn(Dispatchers.IO)
        }

        return flow
    }

    fun import(): Flow<Resource<Boolean>> {
        val dataDirectory = Environment.getDataDirectory()
        val databaseDirectoryPath= "/data/${BuildConfig.APPLICATION_ID}/databases"

        val importDirectory = applicationContext?.getExternalFilesDir("databases/import")
        val importFilename = "database.db"

        val flow: Flow<Resource<Boolean>> = flow{
            if(importDirectory?.exists() == true){
                File(dataDirectory, "$databaseDirectoryPath/$DATABASE_NAME.db-shm").delete()
                File(dataDirectory, "$databaseDirectoryPath/$DATABASE_NAME.db-wal").delete()

                FileInputStream(File(importDirectory, importFilename)).channel.also {
                    FileOutputStream(File(dataDirectory, "$databaseDirectoryPath/$DATABASE_NAME.db")).channel.apply {
                        transferFrom(it, 0, it.size())
                        it.close()
                        close()
                    }
                }

                emit(Resource.success(true))
            }else{
                throw ImportDatabaseException("Import directory does not exits!")
            }
        }

        with(flow){
            onStart { emit(Resource.loading(null)) }
            catch {exception ->
                with(exception){
                    emit(Resource.error<Boolean>(null, getCode(), message))
                }
            }
            flowOn(Dispatchers.IO)
        }

        return flow
    }

    companion object{
        private const val DATABASE_NAME = "appdatabase"
    }
}