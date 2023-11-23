package com.example.testimages

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.graduationproject.Api.RegisterResponse
import com.example.graduationproject.Api.RetrofitService
import com.example.testimages.databinding.ActivityMainBinding
import com.example.testimages.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity() {

    val connectionError = MutableLiveData("")
    val serverResponse = MutableLiveData("")
    private lateinit var viewModel: RegisterViewModel

    //private lateinit var viewBinding:ActivityMainBinding
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button

    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView

    private val PICK_IMAGE_REQUEST = 1
    private val imageUrls = mutableListOf<String>()
    private val STORAGE_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1 = findViewById(R.id.btn1)
        button2 = findViewById(R.id.btn2)
        button3 = findViewById(R.id.btn3)
        button4 = findViewById(R.id.btn4)

        textView1 = findViewById(R.id.textView1)
        textView2 = findViewById(R.id.textView2)
        textView3 = findViewById(R.id.textView3)
        textView4 = findViewById(R.id.textView4)

        button1.setOnClickListener {
            checkPermissionAndOpenGallery(1)
        }

        button2.setOnClickListener {
            checkPermissionAndOpenGallery(2)
        }

        button3.setOnClickListener {
            checkPermissionAndOpenGallery(3)
        }

        button4.setOnClickListener {
            checkPermissionAndOpenGallery(4)
        }
    }




    private fun checkPermissionAndOpenGallery(buttonIndex: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, open gallery
            openGallery(buttonIndex)
        } else {
            // Permission not granted, request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun openGallery(buttonIndex: Int) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST + buttonIndex)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery(requestCode - PICK_IMAGE_REQUEST)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No Permission Granted",
                    Toast.LENGTH_SHORT
                )
                    .show()
                // Permission denied, handle accordingly
                // ...
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val buttonIndex = requestCode - PICK_IMAGE_REQUEST

        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            val imageUrl = imageUri.toString()
            when (buttonIndex) {
                1 -> {
                    textView1.text = imageUrl
                    imageUrls.add(0, imageUrl)
                    button1.isEnabled = false
                }

                2 -> {
                    textView2.text = imageUrl
                    imageUrls.add(1, imageUrl)
                    button2.isEnabled = false
                }

                3 -> {
                    textView3.text = imageUrl
                    imageUrls.add(2, imageUrl)
                    button3.isEnabled = false
                }

                4 -> {
                    textView4.text = imageUrl
                    imageUrls.add(3, imageUrl)
                    button4.isEnabled = false
                }
            }
        }

        checkUrlsNotEmpty()
    }

    private fun checkUrlsNotEmpty() {
        for (url in imageUrls) {
            if (url.isEmpty()) {
                // Handle the case when a URL is empty
                return
            }
        }

        // All URLs are not empty
        // Proceed with further actions
        // ...
    }

    fun uploadRegister(
        DisplayName: String,
        Email: String,
        PhoneNumber: String,
        Password: String,
        imageUrls: List<Uri>
    ) {


        val fileParts = mutableListOf<MultipartBody.Part>()

        imageUrls.forEachIndexed { index, fileUri ->
            val fileToSend = prepareFilePart("Image${index + 1}", fileUri)
            fileParts.add(fileToSend)
        }



        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("DisplayName", DisplayName)
            .addFormDataPart("Email", Email)
            .addFormDataPart("PhoneNumber", PhoneNumber)
            .addFormDataPart("Password", Password)
            .apply {
                fileParts.forEach { addPart(it) }
            }
            .build()

        RetrofitService.getInstance().register(
            requestBody
        ).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                Log.i(TAG, response.headers().toString())
                Log.i(TAG, response.toString())

                Log.i(TAG, "DONE")
                Log.i(TAG, response.body().toString())
                Log.i(TAG, response.message().toString())
                Log.i(TAG, response.code().toString())
                if (response.body() != null && response.isSuccessful) {
                    try {

                        if (response.code() == 200) {
                            serverResponse.value = "uploaded"


                        } else {

                            connectionError.value = response.errorBody().toString()
                        }
                    } catch (e: Exception) {
                        connectionError.value = e.message.toString()
                    }
                }

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
                connectionError.value = t.message.toString()

            }
        })

    }


    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file: File = FileUtils.getFile(this, fileUri)
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(this.contentResolver.getType(fileUri)!!), file
        )
        return MultipartBody.Part.createFormData(partName, file.name,requestFile)

    }


    fun signUp(view: View) {
        Toast.makeText(this, "uploading...", Toast.LENGTH_SHORT).show()

        viewModel.upload(
            userName = viewModel.userName.value.toString(),
            email = viewModel.email.value.toString(),
            phone = viewModel.phone.value.toString(),
            password = viewModel.password.value.toString(),
            imageUrls
        )
    }






}