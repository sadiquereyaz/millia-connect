package com.reyaz.feature.notice.data.remote

import android.util.Log
import com.reyaz.feature.notice.data.model.NoticeDto
import com.reyaz.feature.notice.data.model.NoticeType
import org.htmlunit.html.HtmlAnchor
import org.htmlunit.html.HtmlDivision
import org.htmlunit.html.HtmlListItem
import org.htmlunit.html.HtmlPage
import org.htmlunit.html.HtmlUnorderedList
import java.net.URL

private const val TAG = "NOTICE_PARSER"

class NoticeParser {

    private var currentTime = System.currentTimeMillis()

    private fun getTime(): Long {
        currentTime -= 50L
        return currentTime
    }

    /*fun parseAcademicCalendar(page: HtmlPage): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing academic calendar..")
            val ul = page.getFirstByXPath<HtmlUnorderedList>(
                "//div[contains(@class, 'bg_gray')]//ul[contains(@class, 'unorder-list')]"
            )

            if (ul != null) {
                val listItems = ul.getByXPath<HtmlListItem>(".//li")
                val acadList: List<NoticeDto> = listItems.take(3).mapNotNull { item ->
                    val anchor = item.getFirstByXPath<HtmlAnchor>(".//a")
                    if (anchor != null) {
                        val title = anchor.textContent.trim()
                        val href = anchor.hrefAttribute.trim()
                        val fullUrl = URL(page.baseURL, href).toString()
                        Log.d(TAG, "$title — $fullUrl")
                        NoticeDto(
                            title = title,
                            url = fullUrl,
                            type = NoticeType.AcademicCalendar,
                            createdOn = getTime()
                        )
                    } else {
                        null
                    }
                }
                return Result.success(acadList)
            } else {
                throw Exception("Error while parsing")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing notice")
            return Result.failure(e)
        }
    }*/

    fun parseHoliday(page: HtmlPage): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing holidays..")
            val div = page.getFirstByXPath<HtmlDivision>(
                "//div[contains(@class, 'bg_gray')]"
            )

            val notices = mutableListOf<NoticeDto>()

            if (div != null) {
                val anchors = div.getByXPath<HtmlAnchor>(".//a")

                anchors.take(9).forEach { anchor ->
                    val title = anchor.textContent.trim()
                    val href = anchor.hrefAttribute.trim()
                    val fullUrl = URL(page.baseURL, href).toString()
                    //delay(500)
                    notices.add(
                        NoticeDto(
                            title = title,
                            type = NoticeType.Holiday,
                            url = fullUrl,
                            createdOn = getTime()
                        )
                    )
                    Log.d(TAG, "$title — $fullUrl")
                }
                notices.reverse()
                return Result.success(notices)
            } else {
                throw Exception("Error while parsing")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing notice")
            return Result.failure(e)
        }
    }

    fun parseAdmissionNotices(page: HtmlPage, noticeType: NoticeType): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing ${noticeType.typeId}..")
//            Log.d(TAG, "Page to be Parsed: ${page.asNormalizedText()}..")
            val anchors = page.getByXPath<HtmlAnchor>("//span[@id='datatable1']//a")
//            Log.d(TAG, "Anchor Size: ${anchors.size}..")
            /*anchors.forEach{
                Log.d(TAG, "Anchor: ${it}..")
            }*/
            if (anchors != null) {
                val admissionNotices: List<NoticeDto> = anchors.mapNotNull { anchor ->
                    val title = anchor.textContent.trim()
                    val href = anchor.hrefAttribute.trim()
                    if (href.isNotBlank()) {
                        val fullUrl = URL(page.baseURL, href).toString()
                        NoticeDto(
                            title = title,
                            url = fullUrl,
                            type = noticeType,
                            createdOn = getTime()
                        )
                    } else null
                }
//                Log.d(TAG, "Admission notices size: ${admissionNotices.size}")
                return Result.success(admissionNotices)
            } else {
                throw Exception("Error while parsing")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            return Result.failure(e)
        }
    }

    fun parseUrgentNotices(page: HtmlPage): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing urgent Notices..")
            val anchors = page.getByXPath<HtmlAnchor>("//marquee//a")
            if (anchors != null) {
                val admissionNotices: List<NoticeDto> = anchors.mapNotNull { anchor ->
                    val title = anchor.textContent.trim()   // Also captures <p> inside
                    val href = anchor.hrefAttribute.trim().replace(" ", "%20")
                    if (href.isNotBlank()) {
                        val fullUrl = URL(page.baseURL, href).toString()
                        NoticeDto(
                            title = title,
                            url = fullUrl,
                            type = NoticeType.Urgent,
                            createdOn = getTime()
                        )
                    } else null
                }
                return Result.success(admissionNotices)
            } else {
                throw Exception("Error while parsing")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error while parsing notice")
            return Result.failure(e)
        }
    }

    fun parseAnchorsByPath1(
        page: HtmlPage,
        noticeType: NoticeType,
        xPath: String,
        limit: Int = 5,
    ): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing ${noticeType.typeId}")

            //val anchorsContainer = page.getFirstByXPath<HtmlDivision>(xPath)
            // Find all <a> tags inside it
            val anchors = page?.getByXPath<HtmlAnchor>(xPath)

            if (anchors != null) {
                val notices = getNoticeFromAnchors(anchors = anchors, limit = limit, noticeType = noticeType, baseUrl = page.baseURL)
                Log.d(TAG, "Notices size: ${notices.size}")
                return Result.success(notices)
            } else {
                throw Exception("Error while parsing")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            return Result.failure(e)
        }
    }

    fun parseAnchorsByPath(
        page: HtmlPage,
        noticeType: NoticeType,
        xPath: String,
        limit: Int = 5,
    ): Result<List<NoticeDto>> {
        try {
            Log.d(TAG, "Parsing ${noticeType.typeId}")

            val anchorsContainer = page.getFirstByXPath<HtmlDivision>(xPath)
            // Find all <a> tags inside it
            val anchors = anchorsContainer?.getByXPath<HtmlAnchor>(".//a")

            if (anchors != null) {
                val notices = getNoticeFromAnchors(anchors = anchors, limit = limit, noticeType = noticeType, baseUrl = page.baseURL)
                Log.d(TAG, "Notices size: ${notices.size}")
                return Result.success(notices)
            } else {
                throw Exception("Error while parsing")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            return Result.failure(e)
        }
    }

    private fun getNoticeFromAnchors(anchors: List<HtmlAnchor?>, limit: Int, noticeType: NoticeType, baseUrl: URL): List<NoticeDto> {
        val notices = anchors.take(limit).mapNotNull { anchor ->
            if (anchor != null) {
                val title = anchor.textContent.trim()
                val href = anchor.hrefAttribute.trim()
                val fullUrl = URL(baseUrl, href).toString()
                NoticeDto(title = title, url = fullUrl, type = noticeType, createdOn = getTime())
            } else null
        }
        return notices
    }
}