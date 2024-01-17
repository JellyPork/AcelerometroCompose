package com.argent.acelerometrocompose.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.acelerometrocompose.ui.theme.BlackCustom
import com.argent.acelerometrocompose.ui.theme.WhiteCustom
import com.argent.acelerometrocompose.ui.theme.WhiteCustom2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(email: TextFieldValue,
                 password: TextFieldValue,
                 onEmailChange: (TextFieldValue) -> Unit,
                 onPasswordChange: (TextFieldValue) -> Unit,
                 onLoginClick: () -> Unit,
                 onRegisterClick: () -> Unit) {
    var showPassword by remember {
        mutableStateOf(false)
    }


    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text("Email") },
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Email,
                    contentDescription = "Email Icon")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { onPasswordChange(it) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Ingresa tu contraseña")},
            shape = RoundedCornerShape(32.dp),
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock Icon" )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (showPassword) "Show Password" else "Hide Password"
                    )
                }
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),

            )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhiteCustom2,
                contentColor = BlackCustom,
            ),
            border = BorderStroke(4.dp, BlackCustom)

        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("¿No tienes una cuenta?")
        Text(
            "Registrate",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }
}
