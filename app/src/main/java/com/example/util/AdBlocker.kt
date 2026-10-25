package com.example.util

import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream

/**
 * Client-side ad blocker and tracker filter for STUDYPAL WebViews.
 * Intercepts network resource requests and cancels common ad networks, pop-ups,
 * and analytics trackers before they load.
 */
object AdBlocker {

    /**
     * Standard contemporary mobile Chrome User-Agent signature for Android.
     * Prevents "update required" or "unsupported browser" notices and ensures smooth HTML5 video playback.
     */
    const val MOBILE_CHROME_USER_AGENT =
        "Mozilla/5.0 (Linux; Android 14; Pixel 8 Pro) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.6613.127 Mobile Safari/537.36"

    private val adDomains = hashSetOf(
        "doubleclick.net",
        "googleads",
        "googlesyndication.com",
        "adservice.google.com",
        "google-analytics.com",
        "googletagmanager.com",
        "googletagservices.com",
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
        "adskeeper.co.uk",
        "adskeeper.com",
        "adcash.com",
        "admaven.com",
        "trafficstars.com",
        "richads.com",
        "hilltopads.net",
        "clickadu.com",
        "effectivemeasure.net",
        "moatads.com",
        "yieldmo.com",
        "contextweb.com",
        "yandex.ru/ads",
        "an.yandex.ru",
        "doubleverify.com",
        "innovid.com",
        "iasds.com"
    )

    /**
     * Checks if a URL points to a known ad network domain or tracking hostname.
     */
    fun isAd(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val lowerUrl = url.lowercase()
        return adDomains.any { domain -> lowerUrl.contains(domain) }
    }

    /**
     * Creates an empty WebResourceResponse (zero bytes) to intercept and prevent the ad resource from loading.
     */
    fun createEmptyResource(): WebResourceResponse {
        return WebResourceResponse(
            "text/plain",
            "utf-8",
            ByteArrayInputStream(ByteArray(0))
        )
    }
}
