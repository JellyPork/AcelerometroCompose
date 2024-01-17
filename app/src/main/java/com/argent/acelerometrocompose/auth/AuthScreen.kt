package com.argent.acelerometrocompose.auth


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.argent.acelerometrocompose.auth.components.LoginContent
import com.argent.acelerometrocompose.auth.components.RegisterContent
import com.argent.acelerometrocompose.ktor.PostsService
import com.argent.acelerometrocompose.ktor.dto.AuthRequest
import com.argent.acelerometrocompose.ktor.dto.UserRequest
import com.argent.acelerometrocompose.R
import com.argent.acelerometrocompose.data.StoreData
import kotlinx.coroutines.flow.first
//import com.changrojasalex1168165.dosa.data.StoreData
//import com.changrojasalex1168165.dosa.data.dto.ClientResponse
//import com.changrojasalex1168165.dosa.data.dto.ServiceProviderResponse
//import com.changrojasalex1168165.dosa.data.dto.TokenAccess
//import com.changrojasalex1168165.dosa.data.dto.UserRequest
//import com.changrojasalex1168165.dosa.data.dto.UserResponse
//import com.changrojasalex1168165.dosa.ktor.auth.AuthService
//import com.changrojasalex1168165.dosa.ktor.client.ClientService
//import com.changrojasalex1168165.dosa.ktor.provider.ProviderService
//import com.changrojasalex1168165.dosa.ktor.user.UserService

import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, storeData: StoreData) {
    var showLogin by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    var code by remember { mutableStateOf(TextFieldValue()) }

    var dataCode by remember { mutableStateOf("") }


    LaunchedEffect(Unit){
        val authService = PostsService.create()
        val data = authService.obtainCode()
        if (data != null){
            dataCode = data.data ?: ""
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (showLogin) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(R.drawable.gyroscope),
                contentDescription = null,
                modifier=Modifier.height(200.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyLarge, // Adjust the style for your preferred text size
                modifier = Modifier.padding(bottom = 16.dp) // Add padding if needed
            )
            LoginContent(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLoginClick = {
                    // Perform login action
                    scope.launch {
                        val authService = PostsService.create()
                        val data = authService.login(
                            AuthRequest(
                                email = email.text,
                                password = password.text
                            )
                        )
                        if (data != null ){
                            Log.d("To StoreData", data.toString())
                            storeData.saveUserId(data.user!!.id)
                            Log.d("StoreData ID", storeData.getUserId.first())
                            storeData.saveUserName(data.user.name)
                            Log.d("StoreData Name", storeData.getUserName.first())
                            storeData.saveUserEmail(data.user.email)
                            Log.d("StoreData Email", storeData.getUserEmail.first())




                            navController.navigate("home")
                        }
                        Log.d("Debug from login user", data.toString())

                    }
                },
                onRegisterClick = { showLogin = false }
            )
        } else {
            Text(
                text = "Register",
                style = MaterialTheme.typography.bodyLarge, // Adjust the style for your preferred text size
                modifier = Modifier.padding(bottom = 16.dp) // Add padding if needed
            )
            RegisterContent(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                firstName = firstName,
                onFirstNameChange = { firstName = it },
                code = code,
                onCodeChange = { code = it },
                dataCode = dataCode,
                onRegisterClick = { selectedOption ->
//                    var userCreateData: UserRequest? = null
                    scope.launch {
                        try {
                            //Creating user
                            val authService = PostsService.create()
                            val data = authService.register(
                                UserRequest(
                                    id = dataCode,
                                    name = firstName.text,
                                    email = email.text,
                                    password = password.text,
                                    rol = "Paciente"
                                )
                            )

                            if (data != null ){
                                storeData.saveUserId(data.data.id)
                                storeData.saveUserName(data.data.name)
                                storeData.saveUserEmail(data.data.email)

                                Log.d("StoreData information", storeData.getUserName.first())
                                Log.d("StoreData information", storeData.getUserId.first())
                                Log.d("StoreData information", storeData.getUserEmail.first())
                                navController.navigate("home")
                            }

                            Log.d("Debug from registration user", data.toString())
                        } catch (e: Exception) {
                            // Handle exceptions, log or display errors
                            Log.e("Registration Error", e.message ?: "Unknown error")
                        }
                    }
                },
                onLoginClick = { showLogin = true }
            )
        }
    }
}