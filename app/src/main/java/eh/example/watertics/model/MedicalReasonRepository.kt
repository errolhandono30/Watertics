package eh.example.watertics.model

import java.util.Calendar
import java.io.Serializable

// Data Class untuk passing data antar Activity (Serializable agar bisa masuk Intent)
data class InsightArguments(
    val currentVolume: Int,
    val targetVolume: Int,
    val timeContext: String,
    val predictedSymptom: String,
    val scientificReasonCode: String,
    val gapDescription: String
) : Serializable

object InsightCalculator {
    fun generateInsight(current: Int, target: Int, hour: Int): InsightArguments? {
        // Logika Dummy: Selalu munculkan insight untuk testing
        return InsightArguments(
            currentVolume = current,
            targetVolume = target,
            timeContext = "$hour:00",
            predictedSymptom = "Lelah & Mengantuk",
            scientificReasonCode = "BLOOD_VOLUME_DROP",
            gapDescription = "Asupan airmu tertinggal jauh dari jadwal harian."
        )
    }
}

object MedicalReasonRepository {
    fun getExplanation(code: String): ScientificExplanation {
        return when (code) {
            "BLOOD_VOLUME_DROP" -> ScientificExplanation(
                code = "BLOOD_VOLUME_DROP",
                title = "Risiko Dehidrasi Akut",
                description = "Kekurangan cairan menyebabkan ketidakseimbangan elektrolit dalam tubuh. Hal ini menimbulkan rasa Lemas, Pusing, dan kram otot.",
                icon = "🩸"
            )
            else -> ScientificExplanation(
                code = "GENERAL",
                title = "Pentingnya Hidrasi",
                description = "Air sangat penting untuk menjaga suhu tubuh dan melumasi sendi.",
                icon = "💧"
            )
        }
    }
}

data class DailyHistory(
    val date: String,
    val drink: Int,
    val target: Int,
    val isSick: Boolean = false
)