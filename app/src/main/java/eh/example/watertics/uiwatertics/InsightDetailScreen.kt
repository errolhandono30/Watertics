package eh.example.watertics.uiwatertics

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eh.example.watertics.models.*
import eh.example.watertics.model.MedicalReasonRepository
import eh.example.watertics.ui.theme.*

class InsightDetailScreen : ComponentActivity() {

    // 1. Cuma deklarasi variabel (JANGAN DIISI DISINI)
    private lateinit var insightData: InsightArguments
    private var showDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 2. ISI DATA DISINI (Di dalam onCreate)
        // Karena 'intent' baru tersedia di sini
        insightData = InsightArguments(
            currentVolume = intent.getIntExtra("currentVolume", 0),
            targetVolume = intent.getIntExtra("targetVolume", 2000),
            timeContext = intent.getStringExtra("timeContext") ?: "12:00",
            predictedSymptom = intent.getStringExtra("predictedSymptom") ?: "Lelah",
            scientificReasonCode = intent.getStringExtra("scientificReasonCode") ?: "GENERAL",
            gapDescription = intent.getStringExtra("gapDescription") ?: "Data tidak tersedia."
        )

        setContent {
            HydrationAppTheme {
                InsightDetailContent()
            }
        }
    }

    // ... (Sisa kode ke bawah tidak perlu diubah, biarkan sama) ...

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InsightDetailContent() {
        val explanation = MedicalReasonRepository.getExplanation(insightData.scientificReasonCode)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Wawasan Hidrasi", color = Color.White, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) { Icon(Icons.Default.Close, "Close", tint = Color.White) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryPurple)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundWhite)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // ... (Kode UI tetap sama) ...

                // CONTOH BAGIAN TOMBOL (Biar tidak bingung)
                // Kartu Data
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceWhite), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Mengapa Muncul Peringatan?", fontWeight = FontWeight.Bold, color = PrimaryPurple)
                        Spacer(Modifier.height(8.dp))
                        Text(insightData.gapDescription, color = TextSecondary, lineHeight = 20.sp)
                    }
                }
                Spacer(Modifier.height(16.dp))

                // Kartu Penjelasan
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(explanation.icon, fontSize = 32.sp)
                            Spacer(Modifier.width(12.dp))
                            Text("Penjelasan Medis", fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 16.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(explanation.title, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(explanation.description, color = TextSecondary, lineHeight = 20.sp)
                    }
                }
                Spacer(Modifier.height(32.dp))

                // Tombol
                Text("Klarifikasi Kondisi", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, color = TextPrimary)
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { sendFeedback(true, null) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessText)
                ) {
                    Text("✅ Benar, Saya ${insightData.predictedSymptom}")
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showDialog = true },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    border = BorderStroke(1.dp, ErrorRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed)
                ) {
                    Text("❌ Salah, Koreksi...")
                }
            }

            // Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Apa yang kamu rasakan?", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            val symptoms = listOf("Sakit Kepala", "Pusing", "Mulut Kering", "Hanya Haus", "Saya Bugar")
                            symptoms.forEach { symptom ->
                                TextButton(
                                    onClick = {
                                        sendFeedback(false, symptom)
                                        showDialog = false
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(horizontal = 0.dp)
                                ) {
                                    Text(symptom, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth(), color = TextPrimary, fontSize = 16.sp)
                                }
                                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                            }
                        }
                    },
                    confirmButton = {},
                    dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Batal", color = TextSecondary) } },
                    containerColor = SurfaceWhite
                )
            }
        }
    }

    private fun sendFeedback(isAccurate: Boolean, correction: String?) {
        val intent = Intent().apply {
            putExtra("isPredictionAccurate", isAccurate)
            putExtra("userCorrection", correction)
            putExtra("predictedSymptom", insightData.predictedSymptom)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}