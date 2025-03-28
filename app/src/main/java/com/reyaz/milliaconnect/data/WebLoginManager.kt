package com.reyaz.milliaconnect.data

import android.util.Log
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput
import com.gargoylesoftware.htmlunit.html.HtmlTextInput
import com.reyaz.milliaconnect.util.NetworkConnectivityObserver
import com.reyaz.milliaconnect.util.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WebLoginManager(
    private val notificationHelper: NotificationHelper,
//    private val wifiNetworkManager: WifiNetworkManager,
    private val wifiNetworkManager: NetworkConnectivityObserver,
) {
    val webClient = WebClient(BrowserVersion.CHROME).apply {
        options.isJavaScriptEnabled = true
        options.isCssEnabled = false
        options.isThrowExceptionOnScriptError = false
        options.isThrowExceptionOnFailingStatusCode = false
        options.isPrintContentOnFailingStatusCode = false
        ajaxController = NicelyResynchronizingAjaxController()
        refreshHandler = WaitingRefreshHandler()
    }

    suspend fun performLogin(username: String, password: String): Result<String> =
        withContext(Dispatchers.IO) {
            wifiNetworkManager.forceUseWifi()
            //notificationHelper.showNotification("performing login", "in weblogin manager")
            val loginUrl = "http://10.2.0.10:8090/login?dummy"
//            Log.d("WebScrapingService", "it is login portal")
            try {
                Log.d("WebScrapingService", "1")
                val page: HtmlPage = webClient.getPage(loginUrl)
                val usernameField: HtmlTextInput =
                    page.getFirstByXPath("//input[@type='text']")
                val passwordField: HtmlPasswordInput =
                    page.getFirstByXPath("//input[@type='password']")
                val loginButton: HtmlElement =
                    page.getFirstByXPath("/html/body/div[1]/form/div[3]/button")
                Log.d("WebScrapingService", "$username, $password")

                usernameField.setValueAttribute(username)
                passwordField.setValueAttribute(password)
                val responsePage: HtmlPage = loginButton.click()
//                                delay(2000)
                val pageText = responsePage.asNormalizedText()
                // Closing the webClient to release resources.
                // The WebClient is used to simulate a web browser.
                // It is important to close it after use to free up resources.
                Log.d("WebScrapingService", "Response Page: $pageText")

                if (pageText.contains("Note: Please enter your valid credentials."))
                    Result.failure(Exception("Wrong Username or Password"))
                else Result.success("Successfully Logged in!")
            } catch (e: Exception) {
                Log.d("WebScrapingService", "3")
                Log.e("WebScrapingService", "Error While Connecting", e)
                Result.failure(e)
            }
        }

    suspend fun performLogout(): Result<String> = withContext(Dispatchers.IO) {
        try {
            wifiNetworkManager.forceUseWifi()
            val logoutUrl = "http://10.2.0.10:8090/logout?dummy"
            val page: HtmlPage = webClient.getPage(logoutUrl)

            Log.d("WebScrapingService", "Logout Response Page text\n: ${page.asNormalizedText()}")
//            Log.d("WebScrapingService", "Logout Response Page xml: ${page.asXml()}")
            return@withContext Result.success("Successfully Logged out!")
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}
