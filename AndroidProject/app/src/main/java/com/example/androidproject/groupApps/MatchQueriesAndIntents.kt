package com.example.androidproject.groupApps

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

fun extractQueriedPackages(packageInfo: PackageInfo, xpp : XmlPullParser, manifest : String): HashSet<String> {
    val arrayOfQueryPackages = HashSet<String>()
    try{
        xpp.setInput(StringReader(manifest))
        var eventType = xpp.eventType
        var insideQueries = false
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val tagName = xpp.name
                    if (tagName == "queries") {
                        insideQueries = true
                    } else if (insideQueries && tagName == "package") {
                        arrayOfQueryPackages.add(xpp.getAttributeValue(null, "name"))
                    }
                }
                XmlPullParser.END_TAG -> {
                    val tagName = xpp.name
                    if (tagName == "queries") {
                        insideQueries = false
                    }
                }
            }
            eventType = xpp.next()
        }
    }catch(ex : Exception){
        Log.d("Error QueryIntentsMatch", ex.toString())
    }
    Log.d("QueryIntentsMatch",
        "package %s queries %d packages".format
            (packageInfo.packageName, arrayOfQueryPackages.size)
    )
    return arrayOfQueryPackages
}


fun depthFirstSearch(initialStart: PackageInfo, xpp: XmlPullParser, packageManager: PackageManager){
    val stack = ArrayDeque<String>()
    val visited = HashSet<PackageInfo>()


    visited.add(initialStart)
    val initialManifest = getManifestFromPackageInfo(initialStart)
    val firstLegaturi = extractQueriedPackages(initialStart, xpp, manifest = initialManifest)
    firstLegaturi.forEach { aux -> stack.add(aux)}

    while (stack.isNotEmpty()){
        val nextPackageName = stack.removeFirst()
        val nextPackage = packageManager.getPackageInfo(nextPackageName, 0)
        val nextPackageManifest = getManifestFromPackageInfo(nextPackage)

        val nextLegaturi = extractQueriedPackages(nextPackage, xpp, nextPackageManifest)
        visited.add(nextPackage)
        nextLegaturi.forEach { aux -> if (!visited.map { value -> value.packageName }.contains(aux)) stack.addFirst(aux) }
    }

    Log.d("DFS", "%d are in this group".format(visited.size))
}