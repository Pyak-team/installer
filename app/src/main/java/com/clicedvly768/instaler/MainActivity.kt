package com.clicedvly768.instaler

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class ApkInstaller(private val context: Context) {

    companion object {
        private const val VK_PACKAGE = "com.vkontakte.android"
        private const val YANDEX_PACKAGE_PREFIX = "ru.yandex"
        private const val PROVIDER_SUFFIX = ".fileprovider"
    }

    fun installApk(apkFile: File) {
        try {
            checkForRestrictedApps()

            val intent = Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val authority = context.packageName + PROVIDER_SUFFIX
                    FileProvider.getUriForFile(context, authority, apkFile)
                } else {
                    Uri.fromFile(apkFile)
                }

                setDataAndType(uri, "application/vnd.android.package-archive")
            }

            context.startActivity(intent)
        } catch (e: SecurityException) {
            throw ApkInstallException("Security error installing APK", e)
        } catch (e: IllegalArgumentException) {
            throw ApkInstallException("Invalid arguments for installing APK", e)
        } catch (e: Exception) {
            throw ApkInstallException("Unknown error while installing APK", e)

        }
    }

    private fun checkForRestrictedApps() {
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        for (packageInfo in installedPackages) {
            val packageName = packageInfo.packageName

            if (packageName == VK_PACKAGE) {
                throw RestrictedAppException("VK application detected. APK installation is not possible.")
            }

            if (packageName.startsWith(YANDEX_PACKAGE_PREFIX)) {
                throw RestrictedAppException("Yandex application detected. APK installation is not possible.")
            }
        }
    }
}

class ApkInstallException(message: String, cause: Throwable? = null) : Exception(message, cause)

class RestrictedAppException(message: String) : Exception(message)