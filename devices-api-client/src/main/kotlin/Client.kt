package com.exabyte

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*


val gson = Gson()

fun main() {

    val deviceId = "5f22c316fa436340bc553c14"

    //getDevices()

    //getSpecificDevice(deviceId)

    // deleteDevice(deviceId)

    postDevice(createDevice())

}

fun getDevices() {
    val deviceCollectionType: Type = object : TypeToken<Collection<Device>>() {}.type
    val url = URL("http://localhost:3000/devices")
    with(url.openConnection() as HttpURLConnection) {
        println("\nSending 'GET' request to URL : $url; Response Code : $responseCode")
        inputStream.bufferedReader().use { bufferReader ->
            bufferReader.lines().forEach { line ->
                try {
                    val devices: Collection<Device> = gson.fromJson(line, deviceCollectionType)
                    println("Number of devices: ${devices.size}")
                } catch (ex: Exception) {
                    println(ex.message)
                }
            }
        }
    }
}

fun getSpecificDevice(deviceId: String) {
    val url = URL("http://localhost:3000/devices/$deviceId")
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"
        inputStream.bufferedReader().use { bufferReader ->
            bufferReader.lines().forEach { line ->
                try {
                    val device = gson.fromJson(line, Device::class.java)
                    println("Device retrieved: ${device.make}")
                } catch (ex: Exception) {
                    println(ex.message)
                }
            }
        }
    }
}

fun deleteDevice(deviceId: String) {
    val url = URL("http://localhost:3000/devices/$deviceId")
    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "DELETE"
        inputStream.bufferedReader().use { bufferReader ->
            bufferReader.lines().forEach { line ->
                println(line)
            }
        }
    }
}

fun createDevice(): Device {
    val qrCode = UUID.randomUUID().toString()
    val make = UUID.randomUUID().toString()
    return Device(qrCode, make)
}

fun postDevice(device: Device) {
    val url = URL("http://localhost:3000/devices")
    val json = gson.toJson(device)
    val postData: ByteArray = json.toByteArray(UTF_8)
    with(url.openConnection() as HttpURLConnection) {
        doOutput = true
        useCaches = false
        instanceFollowRedirects = false
        requestMethod = "POST"
        setRequestProperty("Content-Type", "application/json")
        setRequestProperty("charset", "utf-8")
        setRequestProperty("Content-Length", postData.size.toString())
        outputStream.use { wr ->
            wr.write(postData)
        }
        inputStream.bufferedReader().use { bufferReader ->
            bufferReader.lines().forEach { line ->
                println(line)
            }
        }
    }
}

