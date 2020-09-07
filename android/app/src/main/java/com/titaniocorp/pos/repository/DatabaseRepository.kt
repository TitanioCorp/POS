package com.titaniocorp.pos.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.app.model.Resource
import com.titaniocorp.pos.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.File
import java.lang.Exception
import java.util.*
import javax.inject.Inject

/**
 * Maneja el export e import de base de datos.
 * @author Juan Ortiz
 * @date 23/05/2020
 */
@ExperimentalCoroutinesApi
class DatabaseRepository @Inject constructor(private val applicationContext: Context):  BaseRepository(){

    private fun StorageReference.uploadToFirebase(file: File){
        val uploadTask = putFile(Uri.fromFile(file))

        uploadTask
            .addOnFailureListener {
                Timber.tag(Constants.TAG_APP_DEBUG).e("${file.path} file upload error!")
            }
            .addOnSuccessListener {
                Timber.tag(Constants.TAG_APP_DEBUG).d("${file.path} file upload success!")
            }
    }

    private fun StorageReference.importFromFirebase(
        dataDirectory: File,
        deviceFilePath: String,
        suffix: String
    ){
        try {
            val filenamePath = "${deviceFilePath}.${suffix}"
            val file = File.createTempFile(DATABASE_NAME, suffix)

            getFile(file)
                .addOnSuccessListener {
                    File(dataDirectory, filenamePath).delete()
                    file.copyTo(File(dataDirectory, filenamePath), true)

                    Timber.tag(Constants.TAG_APP_DEBUG).d("$filenamePath file import success!")
                }
                .addOnFailureListener {
                    Timber.tag(Constants.TAG_APP_DEBUG).e("$filenamePath file import error!")
                }
        }catch (exception: Exception){
            Timber.tag(Constants.TAG_APP_DEBUG).e(exception)
        }
    }

    fun export(): Flow<Resource<Boolean>> {
        val dataDirectory = Environment.getDataDirectory()
        val databaseDevicePath= "/data/${BuildConfig.APPLICATION_ID}/databases/$DATABASE_NAME"

        val date = Date().toFormatFileString()
        val filename = "${FIREBASE_DATABASE_FOLDER}/${date}/${DATABASE_NAME}"

        val storageRef = FirebaseStorage.getInstance().reference

        val flow: Flow<Resource<Boolean>> = flow{

            File(dataDirectory, "$databaseDevicePath.db").also {
                storageRef.child("${filename}.db").uploadToFirebase(it)
            }

            File(dataDirectory, "$databaseDevicePath.db-shm").also {
                storageRef.child("${filename}.db-shm").uploadToFirebase(it)
            }

            File(dataDirectory, "$databaseDevicePath.db-wal").also {
                storageRef.child("${filename}.db-wal").uploadToFirebase(it)
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
        val filenameDevice = "/data/${BuildConfig.APPLICATION_ID}/databases/$DATABASE_NAME"
        val filenameFirebase = "${FIREBASE_DATABASE_FOLDER}/import/${DATABASE_NAME}"

        val storageRef = FirebaseStorage.getInstance().reference

        val flow: Flow<Resource<Boolean>> = flow{
            storageRef
                .child("${filenameFirebase}.db")
                .importFromFirebase(dataDirectory, filenameDevice, "db")

            storageRef
                .child("${filenameFirebase}.db-shm")
                .importFromFirebase(dataDirectory, filenameDevice, "db-shm")

            storageRef
                .child("${filenameFirebase}.db-wal")
                .importFromFirebase(dataDirectory, filenameDevice, "db-wal")

            emit(Resource.success(true))
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
        private const val FIREBASE_DATABASE_FOLDER = "databases"
        private const val DATABASE_NAME = "appdatabase"
    }
}