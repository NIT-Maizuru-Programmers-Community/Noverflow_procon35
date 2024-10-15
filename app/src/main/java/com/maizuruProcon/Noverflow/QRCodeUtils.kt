package com.maizuruProcon.Noverflow

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.random.Random

object QRCodeUtils {
    fun generateRandomFourDigitNumber(): String {
        return String.format("%04d", Random.nextInt(0, 10000))
    }

    fun createBitMatrix(data: String): BitMatrix? {
        val multiFormatWriter = MultiFormatWriter()
        val hints = mapOf(
            EncodeHintType.MARGIN to 0,
            EncodeHintType.ERROR_CORRECTION to com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L
        )

        return multiFormatWriter.encode(
            data,
            BarcodeFormat.QR_CODE,
            170,
            200,
            hints
        )
    }

    fun createBitmap(bitMatrix: BitMatrix): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }

    fun createQrCode(data: String): Bitmap? {
        return try {
            val bitMatrix = createBitMatrix(data)
            bitMatrix?.let { createBitmap(it) }
        } catch (e: Exception) {
            null
        }
    }
}