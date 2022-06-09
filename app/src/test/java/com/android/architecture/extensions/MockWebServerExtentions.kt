package com.android.architecture.extensions

import com.android.architecture.helper.ResourceReader
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

fun MockWebServer.enqueueFromFile(file: String, responseCode: Int) {
    val fileContent = ResourceReader.read(file)
    val response = MockResponse()
        .setResponseCode(responseCode)
        .setBody(fileContent)
    enqueue(response)
}
