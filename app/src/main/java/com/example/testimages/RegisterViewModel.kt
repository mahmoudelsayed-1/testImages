package com.example.testimages

import android.net.Uri

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RegisterViewModel:ViewModel() {



    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phone = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val userNameError = MutableLiveData<String?>()
    val emailError = MutableLiveData<String?>()
    val phoneError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()

    val txtImg1 = MutableLiveData<String?>()
    val txtImg2 = MutableLiveData<String?>()
    val txtImg3 = MutableLiveData<String?>()
    val txtImg4 = MutableLiveData<String?>()

    val txtImg1Error = MutableLiveData<String?>()
    val txtImg2Error = MutableLiveData<String?>()
    val txtImg3Error = MutableLiveData<String?>()
    val txtImg4Error = MutableLiveData<String?>()

    private val act = MainActivity()


    fun upload(userName : String,
               email : String,
               phone : String,
               password: String,
               fileUris: List<Uri>){

        if(!validForm()) return

        act.uploadRegister(
            userName,
            email,
            phone,
            password,
            fileUris
        )



    }


    private fun validForm(): Boolean {
        var isValid = true

        if(userName.value.isNullOrBlank()) {
            // show error
            userNameError.postValue("please enter userName")
            isValid = false
        }
        else{
            userNameError.postValue(null)
        }


        if(email.value.isNullOrBlank()){
            emailError.postValue("please enter email")
            isValid = false
        }
        else{
            emailError.postValue(null)

        }

        if(phone.value.isNullOrBlank()){
            phoneError.postValue("please enter phone")
            isValid = false
        }
        else{
            phoneError.postValue(null)

        }


        if(password.value.isNullOrBlank()){

            passwordError.postValue("please enter password")
            isValid = false
        }
        else{
            passwordError.postValue(null)
        }

        // handle validation Images

        if(txtImg1.value.isNullOrBlank()&&txtImg2.value.isNullOrBlank()&&
            txtImg3.value.isNullOrBlank()&&txtImg4.value.isNullOrBlank()){

            txtImg1Error.postValue("please pick at least one Image")

//            txtImg2Error.postValue("please pick at least one Image")
//            txtImg3Error.postValue("please pick at least one Image")
//            txtImg4Error.postValue("please pick at least one Image")
            isValid = false
        }
        else{
            txtImg1Error.postValue(null)
            txtImg2Error.postValue(null)
            txtImg3Error.postValue(null)
            txtImg4Error.postValue(null)
        }

        return isValid

    }


}