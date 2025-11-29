package eh.example.watertics.models

import java.util.Calendar

// 1. Data Class untuk Pengiriman Data
data class InsightArguments(
    val currentVolume: Int,
    val targetVolume: Int,
    val timeContext: String,
    val predictedSymptom: String,
    val scientificReasonCode: String,
    val gapDescription: String
)

// 2. Data Class untuk Penjelasan Medis (PENTING AGAR TIDAK ERROR)
data class ScientificExplanation(
    val icon: String,
    val title: String,
    val description: String
)

// 3. Objek Kalkulator
object InsightCalculator {
    fun generateInsight(current: Int, target: Int, hour: Int): InsightArguments? {
        // Mode Testing: Selalu munculkan
        return InsightArguments(
            currentVolume = current,
            targetVolume = target,
            timeContext = "$hour:00",
            predictedSymptom = "Lelah & Mengantuk",
            scientificReasonCode = "BLOOD_VOLUME",
            gapDescription = "Asupan airmu tertinggal jauh dari jadwal harian."
        )
    }
}

// 4. Objek Repository (INI YANG MERAH DI TEMPATMU)
object MedicalReasonRepository {
    fun getExplanation(code: String): ScientificExplanation {
        return when (code) {
            "BLOOD_VOLUME" -> ScientificExplanation(
                icon = "🩸",
                title = "Volume Darah Menurun",
                description = "Kekurangan air membuat darah lebih kental. Jantung bekerja ekstra keras memompa oksigen, menyebabkan rasa lelah."
            )
            else -> ScientificExplanation(
                icon = "💧",
                title = "Pentingnya Hidrasi",
                description = "Air sangat penting untuk menjaga suhu tubuh dan melumasi sendi agar tubuh tetap bugar."
            )
        }
    }
}

// 5. Data Class untuk Riwayat
data class DailyHistory(
    val date: String,
    val drink: Int,
    val target: Int,
    val isSick: Boolean = false
)