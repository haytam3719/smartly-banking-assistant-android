package com.ai.assistant.data.remote

import com.ai.assistant.BuildConfig

object BackendConfig {
    val BASE_URL: String = BuildConfig.BACKEND_BASE_URL

    // Development identity only. Production must use a secure BFF-managed token/session.
    const val DEMO_CUSTOMER_ID = "C1024"
    const val DEMO_SUBJECT_ID = "user-123"
    const val LOCALE = "fr-FR"
}
