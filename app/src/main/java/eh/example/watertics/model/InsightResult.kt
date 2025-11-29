package eh.example.watertics.model


/**
 * Data class untuk hasil validasi yang dikembalikan dari Route B (InsightDetailScreen)
 * ke Route A (HistoryStatsScreen)
 *
 * @property status Status validasi (default: "verified")
 * @property isPredictionAccurate Apakah prediksi AI akurat sesuai kondisi user
 * @property userCorrection Koreksi dari user jika prediksi tidak akurat
 * @property feedbackTimestamp Waktu user memberikan feedback dalam format "HH:mm"
 */
data class InsightResult(
    val status: String = "verified",
    val isPredictionAccurate: Boolean,
    val userCorrection: String? = null,
    val feedbackTimestamp: String
) {
    /**
     * Mendapatkan kondisi final yang dialami user
     * Jika prediksi akurat, return prediksi original
     * Jika tidak, return koreksi dari user
     */
    fun getFinalCondition(originalPrediction: String): String {
        return if (isPredictionAccurate) {
            "$originalPrediction (Terkonfirmasi)"
        } else {
            userCorrection ?: "Tidak ada gejala"
        }
    }

    /**
     * Mendapatkan emoji berdasarkan hasil validasi
     */
    fun getStatusEmoji(): String {
        return if (isPredictionAccurate) "✅" else "🔄"
    }

    /**
     * Mendapatkan pesan rekomendasi berdasarkan hasil
     */
    fun getRecommendationMessage(): String {
        return if (isPredictionAccurate) {
            "Minum 2-3 gelas air sekarang untuk memulihkan kondisi tubuhmu."
        } else {
            "Tetap jaga konsumsi air untuk mencegah gejala lain muncul."
        }
    }

    /**
     * Convert ke format yang bisa disimpan atau dikirim
     */
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "status" to status,
            "isPredictionAccurate" to isPredictionAccurate,
            "userCorrection" to userCorrection,
            "feedbackTimestamp" to feedbackTimestamp
        )
    }

    companion object {
        /**
         * Create InsightResult dari Map (untuk parsing data)
         */
        fun fromMap(map: Map<String, Any?>): InsightResult {
            return InsightResult(
                status = map["status"] as? String ?: "verified",
                isPredictionAccurate = map["isPredictionAccurate"] as? Boolean ?: false,
                userCorrection = map["userCorrection"] as? String,
                feedbackTimestamp = map["feedbackTimestamp"] as? String ?: ""
            )
        }
    }
}