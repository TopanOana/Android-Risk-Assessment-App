package com.example.androidproject.groupApps

import android.app.usage.UsageStats
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import com.example.androidproject.MainActivity
import com.example.androidproject.extractFeatures.AndroidXMLDecompress
import com.example.androidproject.extractFeatures.Inputs
import java.io.File
import java.util.zip.ZipFile

fun getPackageInfoFromUsageStats(
    usageStats: UsageStats,
    packageManager: PackageManager
): PackageInfo {
    return packageManager.getPackageInfo(
        usageStats.packageName,
        PackageManager.GET_META_DATA
    )
}


fun getManifestFromPackageInfo(packageInfo: PackageInfo) : String {
    var zip: ZipFile? = null
    var decompressXML : String = ""
    try {
        Log.d(
            MainActivity::class.qualifiedName,
            "application package %s ".format(packageInfo.packageName)
        )
        zip = ZipFile(File(packageInfo.applicationInfo.sourceDir))
        val inputStream = zip.getInputStream(zip.getEntry("AndroidManifest.xml"))
        val inputData = Inputs.readAllAndClose(inputStream)
        val androidXMLDecompress = AndroidXMLDecompress()
        decompressXML = androidXMLDecompress.decompressXML(inputData)
    } catch (ex: Exception) {
        Log.e(MainActivity::class.qualifiedName, ex.toString())
        ex.printStackTrace()
    } finally {
        zip?.close()
    }
    return decompressXML
}