package com.reyaz.feature.portal.data.remote

import android.util.Log
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.htmlunit.WebClient
import org.htmlunit.html.*
import org.htmlunit.xpath.operations.Bool
import java.net.HttpURLConnection
import java.net.URL

class PortalScraper(
    private val networkManager: NetworkManager,
    private val webClient: WebClient
) {

    fun performLogin(username: String, password: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading("Logging In"))

        if (isTestUser(username, password)) {
            emit(dummyLoginSuccess())
            return@flow
        }

        networkManager.bindToWifiNetwork()

        try {
            val page = webClient.getPage<HtmlPage>(LOGIN_URL)
//            log("Page: ${page.asNormalizedText()}")

            val usernameField = page.getFirstByXPath<HtmlTextInput>(USERNAME_XPATH)
            val passwordField = page.getFirstByXPath<HtmlPasswordInput>(PASSWORD_XPATH)
            val loginButton = page.getFirstByXPath<HtmlElement>(LOGIN_BUTTON_XPATH)

            usernameField.valueAttribute = username
            passwordField.valueAttribute = password
            val responsePage: HtmlPage = loginButton.click()
            val pageText = responsePage.asNormalizedText()

            if (pageText.contains(INVALID_CREDENTIALS_TEXT)) {
                emit(Resource.Error("Wrong Username or Password"))
                return@flow
            }

            networkManager.resetNetworkBinding()
            networkManager.reportCaptivePortalDismissed()

            val isWifiPrimary = isJmiWifi(forceWifi = false) && isInternetAvailable(isCheckingForWifi = false).getOrNull() ?: true
            log("Is Wifi Primary: $isWifiPrimary")
            val message = if (isWifiPrimary) null else WIFI_NOT_PRIMARY_MSG
            emit(Resource.Success(data = "Successfully Logged in!", message = message))

        } catch (e: Exception) {
            log("Login error: ${e.message}")
            emit(Resource.Error("You were not connected to JMI-WiFi"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun performLogout(): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            networkManager.bindToWifiNetwork()
            webClient.getPage<HtmlPage>(LOGOUT_URL)
            Result.success("Successfully Logged out!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isJmiWifi(forceWifi: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Checking JMI-WiFi (for wifi: $forceWifi)")
            if (forceWifi) {
                networkManager.bindToWifiNetwork()
                delay(500)
            }
            val connection = (URL(LOGIN_URL).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                connect()
            }
            log("Response code: ${connection.responseCode}")
            val responseCode = connection.responseCode
            return@withContext responseCode == 200 || connection.responseCode == 302
        } catch (e: Exception) {
            log("isJmiWifi error: ${e.message}")
            return@withContext false
        } finally {
            networkManager.resetNetworkBinding()
        }
    }

     suspend fun isInternetAvailable(isCheckingForWifi: Boolean) : Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Checking Internet Availability (for wifi: $isCheckingForWifi)")
            if (isCheckingForWifi)
                networkManager.bindToWifiNetwork()
            val connection = (URL(URL_204).openConnection() as HttpURLConnection).apply {
                connectTimeout = 5000
                connect()
            }
            val responseCode = connection.responseCode
//            log("Response code: $responseCode, Is internet available: ${responseCode == 204}")
            log("Result:  Is internet available: ${responseCode == 204}")
            return@withContext Result.success(responseCode == 204)
        } catch (e: Exception){
            log("Error: $e")
            return@withContext Result.failure(Exception("Error Occur: ${e.message}"))
        } finally {
            networkManager.resetNetworkBinding()
        }
    }
    private fun isTestUser(username: String, password: String): Boolean {
        return username == "99999" && password == "sssss"
    }

    private suspend fun dummyLoginSuccess(): Resource.Success<String> {
        delay(2000)
        networkManager.reportCaptivePortalDismissed()
        return Resource.Success("Successfully Logged in!", null)
    }

    private fun log(message: String) {
        if (ENABLE_LOGGING) Log.d(TAG, message)
    }

}


private const val TAG = "PORTAL_SCRAPER"
private const val ENABLE_LOGGING = true

private const val LOGIN_URL = "http://10.2.0.10:8090/login?dummy"
private const val LOGOUT_URL = "http://10.2.0.10:8090/logout?dummy"
//private const val URL_204 = "http://www.gstatic.com/generate_204"
private const val URL_204 = "http://clients3.google.com/generate_204"

private const val USERNAME_XPATH = "//input[@type='text']"
private const val PASSWORD_XPATH = "//input[@type='password']"
private const val LOGIN_BUTTON_XPATH = "/html/body/div[1]/form/div[3]/button"
private const val INVALID_CREDENTIALS_TEXT = "Note: Please enter your valid credentials."
private const val WIFI_NOT_PRIMARY_MSG = "But you may not enjoy it because your internet is on!"

