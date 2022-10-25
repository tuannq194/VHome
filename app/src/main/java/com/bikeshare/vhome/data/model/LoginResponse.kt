package com.bikeshare.vhome.data.model

data class LoginResponse(
    val token: String,
    val expired_at: Int,
    val device_token: String,
    val is_admin: Boolean,
    val user_id: String,
    val role_id: String,
    val role_name: String,
    val project_id: String,
    val org_id: String,
    val org_name: String,
    val name: String,
    val phone: String,
    val system_role: String,
    val refresh_token: String
)
