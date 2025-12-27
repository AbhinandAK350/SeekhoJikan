package com.abhinand.seekhojikan.details.util

import android.net.Uri

object Util {

    fun extractYouTubeVideoId(url: String): String? {
        val uri = Uri.parse(url)

        // Case 1: embed URLs -> /embed/{videoId}
        uri.pathSegments?.let { segments ->
            val embedIndex = segments.indexOf("embed")
            if (embedIndex != -1 && segments.size > embedIndex + 1) {
                return segments[embedIndex + 1]
            }
        }

        // Case 2: standard watch URLs -> v={videoId}
        uri.getQueryParameter("v")?.let { return it }

        // Case 3: youtu.be short links
        if (uri.host == "youtu.be") {
            return uri.lastPathSegment
        }

        return null
    }

}