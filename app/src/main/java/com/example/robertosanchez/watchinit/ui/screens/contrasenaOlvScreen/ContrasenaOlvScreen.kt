package com.example.robertosanchez.watchinit.ui.screens.contrasenaOlvScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.data.AuthRes
import com.example.robertosanchez.watchinit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.zIndex

@Composable
fun ContrasenaOlvScreen(auth: AuthManager, navigateToLogin: () -> Unit, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.pxfuel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón de retroceso
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Volver atrás",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(24.dp)
                .zIndex(1001F)
                .clickable { onNavigateBack() },
            tint = Color.White
        )

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF191B1F)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pxfuel),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000))
                )
            }

            Box(
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(120.dp)
                    .background(Color(0xFF191B1F), shape = CircleShape)
                    .padding(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(5.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.padding(54.dp, 0.dp, 54.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            forgotPassword(email, auth, context, navigateToLogin)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "RECUPERAR CONTRASEÑA",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
}

suspend fun forgotPassword(email: String, auth: AuthManager, context: Context, navigateToLogin: () -> Unit) {
    if (email.isNotEmpty()) {
        val res = withContext(Dispatchers.IO) {
            auth.resetPassword(email)
        }
        when (res) {
            is AuthRes.Success -> {
                Toast.makeText(
                    context,
                    "Se ha enviado un correo para restablecer la contraseña",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToLogin()
            }

            is AuthRes.Error -> {
                Toast.makeText(context, "Error: ${res.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
    }
}
