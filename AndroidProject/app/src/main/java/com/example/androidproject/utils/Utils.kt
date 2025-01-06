package com.example.androidproject.utils

import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.ScanViewModel
import com.example.androidproject.extractFeatures.applicationInputByteArray
import com.example.androidproject.extractFeatures.groupApplicationsInputByteArray
import com.example.androidproject.groupApps.createGroupsOfApplications
import com.example.androidproject.groupApps.getManifestFromPackageInfo
import com.example.androidproject.groupApps.extractQueriedPackages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader

val permissionsOS = ArrayList<String>()
val intentsOS = ArrayList<String>()
val applicationProperties = ArrayList<String>()
val features = ArrayList<String>()

fun MainActivity.populateInitialValues() {
    //open file for permissions
    val permissionsFile: InputStreamReader = resources.openRawResource(R.raw.permissions).reader()
    permissionsFile.forEachLine {
        permissionsOS.add(it.trim())
    }

    //open file for intents
    val intentsFile = resources.openRawResource(R.raw.intents).reader()
    intentsFile.forEachLine {
        intentsOS.add(it.trim())
    }

    val applicationFile = resources.openRawResource(R.raw.applicationproperties).reader()
    applicationFile.forEachLine {
        applicationProperties.add(it.trim())
    }

    val featuresFile = resources.openRawResource(R.raw.features).reader()
    featuresFile.forEachLine {
        features.add(it.trim())
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun MainActivity.scanClicked(viewModel: ScanViewModel, system: Boolean) {
    if (!isAccessGranted()) {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    } else {

        val packageManager = packageManager

        val installedApps = packageManager
            .getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0))

        Log.d(
            MainActivity::class.qualifiedName,
            "Total applications detected: %d".format(installedApps.size)
        )

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.setInProgress()
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            val mapOfQueries = HashMap<String, HashSet<String>>()
            val mapOfInputArrays = HashMap<String, ArrayList<Long>>()

            installedApps.forEach {
                try {
                        val pkgInfo = packageManager.getPackageInfo(
                            it.packageName,
                            PackageManager.PackageInfoFlags.of(0)
                        )
                        val manifest = getManifestFromPackageInfo(packageInfo = pkgInfo)

                        val queriesPackages = extractQueriedPackages(pkgInfo, xpp, manifest)

                        mapOfQueries[pkgInfo.packageName] = queriesPackages

                        mapOfInputArrays[pkgInfo.packageName] = applicationInputByteArray(manifest)

                } catch (ex: Exception) {
                    Log.e(MainActivity::class.qualifiedName, ex.toString())
                    ex.printStackTrace()
                }


            }


            val setOfGroups = createGroupsOfApplications(mapOfQueries)

            val setOfFinalGroups = HashSet<Pair<String, HashSet<String>>>()
            val setOfGroupInputArrays = ArrayList<ArrayList<Long>>()

            for (group in setOfGroups) {
                try {
                    val groupArrays = ArrayList<ArrayList<Long>>()

                    groupArrays.add(mapOfInputArrays[group.first]!!)


                    group.second.forEach {
                        if (mapOfInputArrays[it] != null)
                            groupArrays.add(mapOfInputArrays[it]!!)
                        else
                            group.second.remove(it)
                    }

                    setOfGroupInputArrays.add(groupApplicationsInputByteArray(groupArrays))
                    setOfFinalGroups.add(group)
                    Log.d(MainActivity::class.java.simpleName, "group: ${group.first}")
                } catch (ex: Exception) {
                    Log.e(MainActivity::class.qualifiedName + "ERRORGROUP", ex.toString())
                    ex.printStackTrace()
                }

            }

            Log.d(
                MainActivity::class.java.simpleName,
                "AFTER COLLECTION OF INTENTS AND PERMISSIONS!!!"
            )

            val ortEnvironment = OrtEnvironment.getEnvironment()

            viewModel.testApps(
                groups = setOfFinalGroups,
                groupsInput = setOfGroupInputArrays,
                ortSession = createORTSession(ortEnvironment),
                ortEnvironment = ortEnvironment,
                withSystemApps = system
            )
        }


    }
}

private fun MainActivity.createORTSession(ortEnvironment: OrtEnvironment): OrtSession {
    val modelBytes = resources.openRawResource(R.raw.algorithm).readBytes()
    return ortEnvironment.createSession(modelBytes)
}

