package com.example.graduationproject.Api



data class RegisterResponse(
    val displayName:String,
    val email:String,
    val token:String
)

//
//@Parcelize
//data class SourcesResponse(
//
//    @field:SerializedName("displayName")
//    val name: String,
//
//    @field:SerializedName("email")
//    val email: String? = null,
//
//    @field:SerializedName("token")
//    val token: String?=null
//
//
//) : Parcelable