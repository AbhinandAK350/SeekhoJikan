package com.abhinand.seekhojikan.core.config

/**
 * A collection of feature flags to enable or disable certain features of the application.
 * This allows for easy configuration of the app's behavior without changing the core logic.
 */
object FeatureFlags {
    /**
     * If set to true, the application will display images.
     * If set to false, images will be hidden.
     */
    const val SHOW_IMAGES = true // Set to false to disable images
}
