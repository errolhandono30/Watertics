package eh.example.watertics.model // Pastikan package ini sesuai folder kamu

/**
 * Data class untuk menyimpan penjelasan ilmiah tentang dampak dehidrasi
 */
data class ScientificExplanation(
    val code: String,
    val title: String,
    val description: String, // 👈 SUDAH DIGANTI (Tadinya 'explanation')
    val icon: String
) {
    /**
     * Mendapatkan kategori severity berdasarkan code
     */
    fun getSeverityLevel(): SeverityLevel {
        return when (code) {
            "BLOOD_VOLUME_DROP" -> SeverityLevel.MEDIUM
            "DEHYDRATION_RISK" -> SeverityLevel.HIGH
            "RENAL_STRESS" -> SeverityLevel.HIGH
            "COGNITIVE_IMPACT" -> SeverityLevel.MEDIUM
            else -> SeverityLevel.LOW
        }
    }

    /**
     * Mendapatkan warna berdasarkan severity
     */
    fun getSeverityColor(): Long { // Ganti Int ke Long biar aman di Compose Color(0xFF...)
        return when (getSeverityLevel()) {
            SeverityLevel.LOW -> 0xFF4CAF50    // Green
            SeverityLevel.MEDIUM -> 0xFFFF9800  // Orange
            SeverityLevel.HIGH -> 0xFFF44336    // Red
        }
    }

    /**
     * Mendapatkan rekomendasi aksi cepat
     */
    fun getQuickAction(): String {
        return when (code) {
            "BLOOD_VOLUME_DROP" -> "Minum 2 gelas air dalam 15 menit"
            "DEHYDRATION_RISK" -> "Segera minum 3 gelas air dan istirahat"
            "RENAL_STRESS" -> "Tingkatkan konsumsi air, perhatikan warna urin"
            "COGNITIVE_IMPACT" -> "Minum 2 gelas air dan hindari aktivitas berat"
            else -> "Tingkatkan konsumsi air secara bertahap"
        }
    }

    /**
     * Mendapatkan informasi tambahan untuk user
     */
    fun getAdditionalInfo(): String {
        return when (code) {
            "BLOOD_VOLUME_DROP" -> "💡 Tips: Minum air putih lebih efektif daripada minuman berkafein."
            "DEHYDRATION_RISK" -> "💡 Tips: Jangan tunggu haus! Minum air secara teratur."
            "RENAL_STRESS" -> "💡 Tips: Urin kuning muda menandakan hidrasi yang baik."
            "COGNITIVE_IMPACT" -> "💡 Tips: Dehidrasi 1-2% sudah bisa mengganggu fokus."
            else -> "💡 Tips: Air adalah nutrisi terpenting untuk tubuh."
        }
    }

    /**
     * Validasi apakah explanation lengkap
     */
    fun isComplete(): Boolean {
        return code.isNotEmpty() &&
                title.isNotEmpty() &&
                description.isNotEmpty() && // 👈 Ikut berubah jadi description
                icon.isNotEmpty()
    }
}

/**
 * Enum untuk level keparahan kondisi
 */
enum class SeverityLevel {
    LOW,      // Kondisi ringan
    MEDIUM,   // Kondisi sedang
    HIGH      // Kondisi serius
}