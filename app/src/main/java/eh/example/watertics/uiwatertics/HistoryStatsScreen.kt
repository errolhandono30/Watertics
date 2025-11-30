package eh.example.watertics.uiwatertics

import android.app.Activity
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
import eh.example.watertics.model.*
import eh.example.watertics.ui.theme.*

class HistoryStatsScreen : ComponentActivity() {

    // State untuk menyimpan hasil validasi (Misal: "Pusing" atau "Lelah")
    // Jika null, berarti belum divalidasi
    private var validatedCondition by mutableStateOf<String?>(null)

    // Launcher untuk menerima hasil dari InsightDetailScreen
    private val insightLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Ambil data yang dikirim dari Layar B
            val condition = result.data?.getStringExtra("FINAL_CONDITION")
            if (condition != null) {
                // Simpan ke state -> UI akan otomatis berubah jadi kartu hijau
                validatedCondition = condition
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
        // Generate insight dummy
        val insight = remember { InsightCalculator.generateInsight(currentVolume, targetVolume, currentHour) }

        // Data dummy riwayat
        val historyData = listOf(
            DailyHistory("Senin, 21 Nov", 1400, 2000, false),
            DailyHistory("Minggu, 20 Nov", 2000, 2000, false),
            DailyHistory("Sabtu, 19 Nov", 1800, 2000, false)
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

            // Grafik Card
            StatsGraphCard(currentVolume, targetVolume)
            Spacer(modifier = Modifier.height(24.dp))

            // --- LOGIKA GANTI KARTU ---
            if (validatedCondition != null) {
                // KARTU HIJAU (HASIL VALIDASI) - Sesuai screenshot terakhir
                ConfirmedCard(condition = validatedCondition!!)
            } else if (insight != null) {
                // KARTU ORANYE (ANALISA) - Sesuai screenshot pertama
                InsightTeaserCard(
                    insight = insight,
                    onClick = { openInsightDetail(insight) }
                )
            }
            // ---------------------------

            Spacer(modifier = Modifier.height(24.dp))

            // List Riwayat
            Text("Analisa Minggu Ini", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))

            historyData.forEach { day ->
                HistoryItemCard(day)
                Spacer(modifier = Modifier.height(12.dp))
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Fungsi membuka activity detail
    private fun openInsightDetail(insight: InsightArguments) {
        val intent = Intent(this, InsightDetailScreen::class.java).apply {
            putExtra("currentVolume", insight.currentVolume)
            putExtra("targetVolume", insight.targetVolume)
            putExtra("timeContext", insight.timeContext)
            putExtra("predictedSymptom", insight.predictedSymptom)
            putExtra("scientificReasonCode", insight.scientificReasonCode)
            putExtra("gapDescription", insight.gapDescription)
        }
        // Gunakan launcher, BUKAN startActivity biasa
        insightLauncher.launch(intent)
    }

    // --- UI COMPONENTS ---

    @Composable
    fun InsightTeaserCard(insight: InsightArguments, onClick: () -> Unit) {
        Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WarningOrangeBg), // Warna Oranye Pudar
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Face, "AI", Modifier.size(40.dp), tint = WarningText)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Analisa Pola Minum", fontWeight = FontWeight.Bold, color = WarningText)
                    Text(
                        "Pola minummu berpotensi menyebabkan ${insight.predictedSymptom}. Cek detail >",
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                }
            }
        }
    }

    @Composable
    fun ConfirmedCard(condition: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SuccessGreenBg), // Warna Hijau Pudar
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

    @Composable
    fun HistoryItemCard(data: DailyHistory) {
        val percent = (data.drink.toFloat() / data.target)
        val percentText = (percent * 100).toInt()

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
                Column {
                    Text(data.date, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${data.drink}ml / ${data.target}ml", fontSize = 12.sp, color = TextSecondary)
                }
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { percent },
                        modifier = Modifier.size(45.dp),
                        color = PrimaryPurple,
                        trackColor = TrackPurple
                    )
                    Text("$percentText%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        }
    }
}