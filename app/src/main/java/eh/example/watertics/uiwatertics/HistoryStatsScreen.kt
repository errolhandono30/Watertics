package eh.example.watertics.uiwatertics

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import eh.example.watertics.models.*
import eh.example.watertics.ui.theme.*

class HistoryStatsScreen : ComponentActivity() {

    // --- STATE UI ---
    private var showInsightCard by mutableStateOf(true)
    private var showConfirmedCard by mutableStateOf(false)
    private var finalConditionText by mutableStateOf("")

    // --- PENERIMA DATA DARI SCREEN B ---
    private val insightLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val isAccurate = data?.getBooleanExtra("isPredictionAccurate", false) ?: false
            val correction = data?.getStringExtra("userCorrection")
            val predicted = data?.getStringExtra("predictedSymptom")

            showInsightCard = false
            showConfirmedCard = true

            finalConditionText = if (isAccurate) {
                "$predicted (Terkonfirmasi)"
            } else {
                correction ?: "Kondisi Lain"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HydrationAppTheme {
                HistoryStatsContent()
            }
        }
    }

    @Composable
    fun HistoryStatsContent() {
        val currentVolume = 400
        val targetVolume = 2000
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val insight = remember { InsightCalculator.generateInsight(currentVolume, targetVolume, currentHour) }

        // --- DATA DUMMY DENGAN STATUS KESEHATAN ---
        val historyData = listOf(
            DailyHistory("Senin, 21 Nov", 1400, 2000, isSick = false), // Sehat
            DailyHistory("Minggu, 20 Nov", 2000, 2000, isSick = false), // Sehat
            DailyHistory("Sabtu, 19 Nov", 900, 2000, isSick = true),    // Sakit (Label Merah)
            DailyHistory("Jumat, 18 Nov", 1100, 2000, isSick = true)    // Sakit (Label Merah)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("Riwayat & Statistik", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))

            // Grafik
            StatsGraphCard(currentVolume, targetVolume)
            Spacer(modifier = Modifier.height(24.dp))

            // --- KARTU INSIGHT (Flow Gambar 1 & 4) ---
            if (showInsightCard && insight != null) {
                InsightTeaserCard(
                    insight = insight,
                    onClick = { openInsightDetail(insight) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (showConfirmedCard) {
                ConfirmedCard(condition = finalConditionText)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- LIST RIWAYAT ---
            Text("Analisa Minggu Ini", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))

            historyData.forEach { day ->
                HistoryItemCard(day)
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // --- ITEM KARTU RIWAYAT (UPDATE VISUAL) ---
    @Composable
    fun HistoryItemCard(data: DailyHistory) {
        val percent = (data.drink.toFloat() / data.target)
        val percentText = (percent * 100).toInt()

        // Kalau Sakit, warna progress beda dikit biar matching (opsional, disini saya samain PrimaryPurple)
        val progressColor = if (percent >= 1f) SuccessGreen else PrimaryPurple

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // KIRI: Tanggal & Label Status
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(data.date, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(modifier = Modifier.width(8.dp))

                        // LOGIKA LABEL (CHIP)
                        if (data.isSick) {
                            HealthStatusChip("Sakit", Color(0xFFFFEBEE), Color(0xFFD32F2F))
                        } else {
                            HealthStatusChip("Sehat", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${data.drink}ml / ${data.target}ml", fontSize = 12.sp, color = TextSecondary)
                }

                // KANAN: Progress
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(progress = { percent }, modifier = Modifier.size(45.dp), color = progressColor, trackColor = TrackPurple)
                    Text("$percentText%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        }
    }

    // --- KOMPONEN LABEL KECIL (CHIP) ---
    @Composable
    fun HealthStatusChip(text: String, bgColor: Color, textColor: Color) {
        Surface(
            color = bgColor,
            shape = RoundedCornerShape(50), // Bulat lonjong
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }

    // --- KOMPONEN LAINNYA (SAMA SEPERTI SEBELUMNYA) ---
    private fun openInsightDetail(insight: InsightArguments) {
        val intent = Intent(this, InsightDetailScreen::class.java).apply {
            putExtra("currentVolume", insight.currentVolume)
            putExtra("targetVolume", insight.targetVolume)
            putExtra("timeContext", insight.timeContext)
            putExtra("predictedSymptom", insight.predictedSymptom)
            putExtra("scientificReasonCode", insight.scientificReasonCode)
            putExtra("gapDescription", insight.gapDescription)
        }
        insightLauncher.launch(intent)
    }

    @Composable
    fun InsightTeaserCard(insight: InsightArguments, onClick: () -> Unit) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarningOrangeBg),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Face, "AI", Modifier.size(40.dp), tint = WarningText)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Analisa Pola Minum", fontWeight = FontWeight.Bold, color = WarningText)
                    Text("Pola minummu berpotensi menyebabkan ${insight.predictedSymptom}. Cek detail >", fontSize = 13.sp, color = TextPrimary)
                }
            }
        }
    }

    @Composable
    fun ConfirmedCard(condition: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SuccessGreenBg),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, "OK", Modifier.size(40.dp), tint = SuccessText)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Validasi Selesai", fontWeight = FontWeight.Bold, color = SuccessText)
                    Text("Kondisi si: $condition", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Text("Saran: Minum 2 gelas air sekarang.", fontSize = 13.sp, color = TextSecondary)
                }
            }
        }
    }

    @Composable
    fun StatsGraphCard(current: Int, target: Int) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Konsumsi Hari Ini", color = TextSecondary, fontSize = 14.sp)
                Text("$current ml", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                Text("dari target $target ml", fontSize = 14.sp, color = TextSecondary)
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = { current.toFloat() / target },
                    modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                    color = PrimaryPurple,
                    trackColor = TrackPurple
                )
            }
        }
    }
}