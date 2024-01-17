package com.argent.acelerometrocompose.ktor

import com.argent.acelerometrocompose.ktor.dto.AuthRequest
import com.argent.acelerometrocompose.ktor.dto.AuthResponse
import com.argent.acelerometrocompose.ktor.dto.CodeResponse
import com.argent.acelerometrocompose.ktor.dto.FileUploadRequest
import com.argent.acelerometrocompose.ktor.dto.FileUploadResponse
import com.argent.acelerometrocompose.ktor.dto.Instrument
import com.argent.acelerometrocompose.ktor.dto.UserRequest
import com.argent.acelerometrocompose.ktor.dto.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging

interface PostsService {


//App Functions
    //Get all instruments (Not tested)
    suspend fun getInstruments(): List<Instrument>
    //Get all instruments and place it in root object

    suspend fun getInstrumentsFromRoot(): ArrayList<Instrument>
//    Create a user with a register method
    suspend fun register(userRequest: UserRequest): UserResponse?
//    Login to verify user
    suspend fun  login(authRequest: AuthRequest): AuthResponse?
//  Obtaining code for registering automatically, i think
    suspend fun obtainCode(): CodeResponse?

//    Send CSV data to backend

    suspend fun uploadFile(fileUploadRequest: FileUploadRequest): FileUploadResponse?
//suspend fun login(tokenAccess: TokenAccess): TokenData?
//
//suspend fun register(registerResponse: RegisterResponse): UserRequest?



    companion object {
        fun create(): PostsService {
            return PostsServiceImpl(
                client = HttpClient(Android) {
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    install(JsonFeature) {
                        serializer = GsonSerializer() {
                            setPrettyPrinting()
                            disableHtmlEscaping()
                        }
                    }
                }
            )
        }
    }
}