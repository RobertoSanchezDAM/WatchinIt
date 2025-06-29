package com.example.robertosanchez.watchinit.ui.screens.loginScreen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.data.AuthRes
import com.example.robertosanchez.watchinit.R
import com.example.robertosanchez.watchinit.db.FirestoreManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.zIndex

@Composable
fun LoginScreen(
    auth: AuthManager,
    firestoreManager: FirestoreManager,
    navigateToSignUp: () -> Unit,
    navigateToPrincipal: () -> Unit,
    navigateToContraseñaOlv: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val googleSignLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (val account =
            auth.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
            is AuthRes.Success -> {
                val credential = GoogleAuthProvider.getCredential(account.data?.idToken, null)
                scope.launch {
                    val firebaseUser = auth.googleSignInCredential(credential)
                    when (firebaseUser) {
                        is AuthRes.Success -> {
                            firebaseUser.data?.uid?.let {
                                firestoreManager.guardarUsuario(it)
                            }
                            Toast.makeText(
                                context,
                                "Inicio de sesión correcto",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToPrincipal()
                        }

                        is AuthRes.Error -> {
                            Toast.makeText(
                                context,
                                "Error al iniciar sesión",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            is AuthRes.Error -> {
                Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .offset(y = (-32).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            auth.signInWithGoogle(googleSignLauncher)
                        },
                        modifier = Modifier.size(width = 40.dp, height = 40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF272B30)
                        ),
                        shape = RoundedCornerShape(75.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google",
                            modifier = Modifier.size(25.dp),
                            tint = Color.Unspecified
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                signAnonimous(auth, navigateToPrincipal, context, firestoreManager)
                            }
                        },
                        modifier = Modifier.size(width = 40.dp, height = 40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF272B30)
                        ),
                        shape = RoundedCornerShape(75.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_incognito),
                                contentDescription = "Anónimo",
                                modifier = Modifier.size(25.dp),
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(5.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(5.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "¿Has olvidado la contraseña?",
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { navigateToContraseñaOlv() },
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color(0xFF3B82F6)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            signIn(email, password, context, auth, navigateToPrincipal, firestoreManager)
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
                        text = "INICIAR SESIÓN",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        color = Color.Gray
                    )
                    Text(
                        text = "Registrate",
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.clickable { navigateToSignUp() },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    }
}


suspend fun signAnonimous(auth: AuthManager, navigateToPrincipal: () -> Unit, context: Context, firestoreManager: FirestoreManager) {
    val res = withContext(Dispatchers.IO) {
        auth.signInAnonymously()
    }
    when (res) {
        is AuthRes.Success -> {
            Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
            navigateToPrincipal()
        }
        is AuthRes.Error -> {
            Toast.makeText(context, res.errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

suspend fun signIn(email: String, password: String, context: Context, auth: AuthManager, navigateToPrincipal: () -> Unit, firestoreManager: FirestoreManager) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        val result =
            withContext(Dispatchers.IO) {
                auth.signInWithEmailAndPassword(email, password)
            }
            when (result) {
                is AuthRes.Success -> {
                    result.data?.uid?.let {
                        firestoreManager.guardarUsuario(it)
                    }
                    Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    navigateToPrincipal()
                }
                is AuthRes.Error -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

    } else{
        Toast.makeText(context, "Email y password tienen que estar rellenos", Toast.LENGTH_SHORT).show()
    }
}
