package com.example.util

import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream

/**
 * Client-side ad blocker and tracker filter for STUDYPAL WebViews.
 * Intercepts network resource requests and cancels common ad networks, pop-ups,
 * and analytics trackers before they load.
 */
object AdBlocker {

    private val adDomains = hashSetOf(
        "doubleclick.net",
        "googleads",
        "googlesyndication.com",
        "adservice.google.com",
        "google-analytics.com",
        "adnxs.com",
        "adsystem.com",
        "adroll.com",
        "popads.net",
        "popcash.net",
        "adsterra.com",
        "propellerads.com",
        "exoclick.com",
        "juicyads.com",
        "trafficjunky.com",
        "taboola.com",
        "outbrain.com",
        "criteo.com",
        "zergnet.com",
        "mgid.com",
        "revcontent.com",
        "monetag.com",
        "onclickperformance.com",
        "bidvertiser.com",
        "infolinks.com",
        "amazon-adsystem.com",
        "smartadserver.com",
        "rubiconproject.com",
        "pubmatic.com",
        "openx.net",
        "advertising.com",
        "adform.net",
        "scorecardresearch.com",
        "adservice",
        "partner.googleadservices.com",
        "admob.com",
        "unityads",
        "applovin.com",
        "vungle.com",
        "ironsrc.com",
        "chartboost.com",
        "clarium.io",
        "adkeeper.co.uk",
        "effectivemeasure.net",
        "moatads.com",
        "yieldmo.com",
        "contextweb.com"
    )

    /**
     * Checks if a URL points to a known ad server, tracker, or unwanted pop-up network.
     */
    fun isAd(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val lowerUrl = url.lowercase()
        return adDomains.any { domain -> lowerUrl.contains(domain) }
    }

    /**
     * Creates an empty WebResourceResponse to effectively block the requested ad content.
     */
    fun createEmptyResource(): WebResourceResponse {
        return WebResourceResponse(
            "text/plain",
            "utf-8",
            ByteArrayInputStream(ByteArray(0))
        )
    }
}
