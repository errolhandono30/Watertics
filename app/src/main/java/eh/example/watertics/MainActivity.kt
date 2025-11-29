package eh.example.watertics

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- IMPORT PENTING (Pastikan Package sesuai folder kamu) ---
import eh.example.watertics.ui.theme.HydrationAppTheme
import eh.example.watertics.ui.theme.PrimaryPurple
import eh.example.watertics.ui.theme.SecondaryPurple
import eh.example.watertics.uiwatertics.HistoryStatsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Panggil Tema Aplikasi
            HydrationAppTheme {
                SplashScreen()
            }
        }
    }

    @Composable
    fun SplashScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        // Background Gradasi Ungu
                        colors = listOf(PrimaryPurple, SecondaryPurple)
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Text(
                text = "💧",
                fontSize = 100.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Judul
            Text(
                text = "Smart Hydration",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subjudul
            Text(
                text = "Wawasan Hidrasi Cerdas",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- TOMBOL MULAI (Ungu dengan Border Putih) ---
            Button(
                onClick = {
                    startActivity(Intent(this@MainActivity, HistoryStatsScreen::class.java))
                    finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple // Warna dasar tombol Ungu
                ),
                // Garis tepi Putih agar terlihat jelas
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Mulai",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Teks Putih
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Versi
            Text(
                text = "v1.0.0",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}