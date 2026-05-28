package com.example.fitbody.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class CheckInActivity : AppCompatActivity() {

    private val cameraRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                cameraRequestCode
            )
        } else {
            startQRScanner()
        }
    }

    private fun startQRScanner() {
        val integrator =
            IntentIntegrator(this)

        integrator.setPrompt("Quét mã QR phòng gym")
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)

        integrator.initiateScan()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (
            requestCode == cameraRequestCode &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startQRScanner()
        } else {
            Toast.makeText(
                this,
                "Bạn cần cấp quyền camera để check-in",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: android.content.Intent?
    ) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(
                requestCode,
                resultCode,
                data
            )

        if (result != null) {

            if (result.contents == null) {
                Toast.makeText(
                    this,
                    "Đã hủy check-in",
                    Toast.LENGTH_SHORT
                ).show()

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Check-in thành công: ${result.contents}",
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }

        } else {
            super.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
    }
}