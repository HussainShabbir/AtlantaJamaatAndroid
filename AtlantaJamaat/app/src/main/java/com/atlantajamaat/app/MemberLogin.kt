package com.atlantajamaat.app

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
import com.atlantajamaat.app.ui.theme.AppTopBar
import com.example.app.ui.theme.DarkBlue
import com.example.app.ui.theme.LightGreyBg

class WebAppInterface(private val onDataReceived: (itsId: String, title: String) -> Unit) {
    @JavascriptInterface
    fun postMessage(data: String, title: String) {
        onDataReceived(data, title)
    }
}

fun saveCredentialsToPreferences(context: Context, itsId: String) {
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit {
        putString("ITS_ID", itsId)
    }
    Log.d("Preference", "ITS_ID Saved Successfully: $itsId")
}

@Composable
fun MemberLoginScreen(onNavigateToHome: () -> Unit) {
    val loginUrl = "https://www.its52.com/Login.aspx?OneLogin"
    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            AppTopBar(title = "Member Login")
        },
        containerColor = LightGreyBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        @Suppress("SetJavaScriptEnabled")
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true

                        addJavascriptInterface(
                            WebAppInterface { itsId, title ->
                                Handler(Looper.getMainLooper()).post {
                                    if (itsId.isNotEmpty() && itsId != "null") {
                                        saveCredentialsToPreferences(context, itsId)
                                    }
                                }
                            },
                            "Android"
                        )
                        webChromeClient = object : WebChromeClient() {
                            override fun onReceivedTitle(view: WebView?, title: String?) {
                                super.onReceivedTitle(view, title)
                                if (title?.trim() == "Mumin Home Page | Idaratut Ta'reef al Shakhsi") {
                                    view?.stopLoading()
                                    onNavigateToHome()
                                }
                            }
                        }

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true

                                if (url != null && url.contains("MuminHome", ignoreCase = true)) {
                                    view?.stopLoading()
                                    onNavigateToHome()
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false

                                val jsCode = """
                                    javascript:(function() {
                                        var element = document.getElementById('Header1_lblITS_ID');
                                        var itsId = element ? element.innerText : '';
                                        var pageTitle = document.title;
                                        
                                        if (window.Android && window.Android.postMessage) {
                                            window.Android.postMessage(itsId, pageTitle);
                                        }
                                    })();
                                """.trimIndent()

                                view?.evaluateJavascript(jsCode, null)
                            }
                        }

                        loadUrl(loginUrl)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = DarkBlue
                )
            }
        }
    }
}