package eh.example.watertics.model

data class ScientificExplanation(
    val code: String,
    val title: String,
    val description: String,
    val icon: String
) {
    fun getSeverityLevel(): SeverityLevel {
        return when (code) {
            "BLOOD_VOLUME_DROP" -> SeverityLevel.MEDIUM
            "DEHYDRATION_RISK" -> SeverityLevel.HIGH
            "RENAL_STRESS" -> SeverityLevel.HIGH
            else -> SeverityLevel.LOW
        }
    }

    // Menggunakan 'L' (Long) untuk warna agar kompatibel dengan Color(0xFF...)
    fun getSeverityColor(): Long {
        return when (getSeverityLevel()) {
            SeverityLevel.LOW -> 0xFF4CAF50L
            SeverityLevel.MEDIUM -> 0xFFFF9800L
            SeverityLevel.HIGH -> 0xFFF44336L
        }
    }
}

enum class SeverityLevel {
    LOW, MEDIUM, HIGH
}