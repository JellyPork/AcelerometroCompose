package com.argent.acelerometrocompose.ktor

import android.util.Log
import com.argent.acelerometrocompose.ktor.dto.AuthRequest
import com.argent.acelerometrocompose.ktor.dto.AuthResponse
import com.argent.acelerometrocompose.ktor.dto.CodeResponse
import com.argent.acelerometrocompose.ktor.dto.FileUploadRequest
import com.argent.acelerometrocompose.ktor.dto.FileUploadResponse
import com.argent.acelerometrocompose.ktor.dto.Instrument
import com.argent.acelerometrocompose.ktor.dto.Root
import com.argent.acelerometrocompose.ktor.dto.UserRequest
import com.argent.acelerometrocompose.ktor.dto.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class PostsServiceImpl(
    private val client: HttpClient
) : PostsService {



    override suspend fun getInstruments(): List<Instrument> {
        return try {
            client.get { url(KtorService.TOOLS) }
        } catch(e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: ServerResponseException) {
            // 5xx - responses
            println("Error: ${e.response.status.description}")
            emptyList()
        } catch(e: Exception) {
            println("Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getInstrumentsFromRoot(): ArrayList<Instrument> {
        try {
            // Use Ktor's HTTP client with the JSON feature to make the request.
            val response = client.get<HttpResponse> {
                url(KtorService.TOOLS)
            }

            // Check if the response status code is OK (2xx).
            if (response.status == HttpStatusCode.OK) {
                val root = response.receive<Root>()
                // Deserialize the JSON response into a Root object.
                println(response)
                val responseBody = response.readText()
                Log.d("Response Body Raw: ", responseBody)
                Log.d("Response Body Decoded: ", "Possibly decoding stuff" )
                Log.d("Response Raw: ", root.toString())
                Log.d("Response Raw Data: ", root.data.toString())

                // Create a new Instrument object for each item in the root object's data property.
                val instruments = root.data.map {
                    Instrument(
                        _id = it._id,
                        nombre = it.nombre,
                        acronimo = it.acronimo,
                        categoria = it.categoria,
                        items = it.items,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt
                    )
                }

                Log.d("Instrument created", instruments.toString())
                return root.data as ArrayList<Instrument>


            } else {
                // Handle other HTTP status codes accordingly.
                println("Error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

        // If there's an error or the response status is not OK, return an empty Root object.

        return ArrayList()
    }

    override suspend fun register(userRequest: UserRequest): UserResponse? {
        try {
            val response = client.post<HttpResponse> {
                url(KtorService.USERS)
                contentType(ContentType.Application.Json)
                body = userRequest // Serializing UserRequest to JSON for POST request
            }

            if (response.status == HttpStatusCode.Created) {
                return response.receive<UserResponse>() // Mapping the response to UserResponse
            } else {
                // Handle other HTTP status codes accordingly.
                println("Error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

        return null // Return null for unsuccessful cases
    }

    override suspend fun login(authRequest: AuthRequest): AuthResponse? {
        try {
            val response = client.post<HttpResponse> {
                url(KtorService.USERS_LOGIN)
                contentType(ContentType.Application.Json)
                body = authRequest
            }

            if (response.status == HttpStatusCode.OK) {
                return response.receive<AuthResponse>() // Mapping the response to TokenData
            } else {
                // Handle other HTTP status codes accordingly.
                println("Error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

        return null
    }

    override suspend fun obtainCode(): CodeResponse? {
        try {
            // Use Ktor's HTTP client with the JSON feature to make the request.
            val response = client.get<HttpResponse> {
                url(KtorService.USERS_CODE)
            }
            // Check if the response status code is OK (2xx).
            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                return response.receive<CodeResponse>() // Mapping the response to TokenData
            } else {
                // Handle other HTTP status codes accordingly.
                println("Error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
        return null
    }

    override suspend fun uploadFile(fileUploadRequest: FileUploadRequest): FileUploadResponse? {
        try {
            val response = client.post<HttpResponse> {
                url(KtorService.FILES)
                contentType(ContentType.Application.Json)
                body = fileUploadRequest // Serializing UserRequest to JSON for POST request
            }

            if (response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK) {
                return response.receive<FileUploadResponse>()

            } else {
                // Handle other HTTP status codes accordingly.
                println("Error: ${response.status}")
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

        return null // Return null for unsuccessful cases
    }

    //Examples
//    override suspend fun login(tokenAccess: TokenAccess): TokenData? {
//        try {
//            val response = client.post<HttpResponse> {
//                url(KtorService.LOGIN + "email=${tokenAccess.email}&password=${tokenAccess.password}")
//                contentType(ContentType.Application.Json)
//                body = tokenAccess
//            }
//
//            if (response.status == HttpStatusCode.OK) {
//                return response.receive<TokenData>() // Mapping the response to TokenData
//            } else {
//                // Handle other HTTP status codes accordingly.
//                println("Error: ${response.status}")
//            }
//        } catch (e: Exception) {
//            println("Error: ${e.message}")
//        }
//
//        return null
//    }
//
//
//    override suspend fun register(registerResponse: RegisterResponse): UserRequest? {
//        try {
//            val response = client.post<HttpResponse> {
//                url(KtorService.CREATE_ACCOUNT_POST)
//                contentType(ContentType.Application.Json)
//                body = registerResponse // Serializing UserRequest to JSON for POST request
//            }
//
//            if (response.status == HttpStatusCode.Created) {
//                return response.receive<UserRequest>() // Mapping the response to UserResponse
//            } else {
//                // Handle other HTTP status codes accordingly.
//                println("Error: ${response.status}")
//            }
//        } catch (e: Exception) {
//            println("Error: ${e.message}")
//        }
//
//        return null // Return null for unsuccessful cases
//    }

}