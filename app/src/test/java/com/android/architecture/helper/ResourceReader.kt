package com.android.architecture.helper

import java.io.IOException
import java.io.InputStream

object ResourceReader {

    @Throws(IOException::class)
    private fun openFile(filename: String): InputStream? {
        return javaClass.classLoader.getResourceAsStream(filename)
    }

    fun read(fileName: String): String {
        val inputStream = openFile(fileName)
        return inputStream?.bufferedReader().use { bufferReader ->
            bufferReader?.readText()
        }!!
    }
}
