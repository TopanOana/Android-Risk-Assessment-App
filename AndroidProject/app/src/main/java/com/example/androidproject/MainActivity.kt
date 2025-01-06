package com.example.androidproject

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.androidproject.composables.MainScreen
import com.example.androidproject.utils.populateInitialValues
import com.example.androidproject.utils.scanClicked


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidProjectTheme {
                populateInitialValues()
                val viewModel: ScanViewModel by viewModels {
                    ScanViewModelFactory()
                }
                MainScreen(viewModel, onScanClick = {
                    viewModel.setInProgress()
                    scanClicked(viewModel, it)
                    viewModel.setNotInProgress()
                })
            }
        }
    }
//
//    @Composable
//    fun But() {
//        Button(
//            onClick = {
//                if (!isAccessGranted()) {
//                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//                    startActivity(intent)
//                } else {
//
//                    getAllPermissions()
//
//                    val usageStatsManager =
//                        getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//
//                    val cal: Calendar = Calendar.getInstance()
//                    val endTime = cal.timeInMillis
//                    cal.add(Calendar.DAY_OF_MONTH, -1)
//                    val beginTime = cal.timeInMillis
//
//                    val queryUsageStats: List<UsageStats> = usageStatsManager.queryUsageStats(
//                        UsageStatsManager.INTERVAL_MONTHLY, beginTime, endTime
//                    )
//
//                    Log.d(
//                        MainActivity::class.qualifiedName,
//                        "Total applications detected: %d".format(queryUsageStats.size)
//                    )
//
//                    val packageManager = packageManager
//                    val factory = XmlPullParserFactory.newInstance()
//                    factory.isNamespaceAware = true
//                    val xpp = factory.newPullParser()
//                    val mapOfQueries = HashMap<String, HashSet<String>>()
//                    val mapOfPermissions = HashMap<String, HashSet<String>>()
//                    val mapOfIntents = HashMap<String, HashSet<String>>()
//
//                    for (usageStats in queryUsageStats) {
//                        try {
//                            val pkgInfo = getPackageInfoFromUsageStats(usageStats, packageManager)
//                            val manifest = getManifestFromPackageInfo(packageInfo = pkgInfo)
//                            val queriesPackages = matchQueriesToIntents(pkgInfo, xpp, manifest)
//                            mapOfQueries[pkgInfo.packageName] = queriesPackages
//
//                        } catch (ex: Exception) {
//                            Log.e(MainActivity::class.qualifiedName, ex.toString())
//                            ex.printStackTrace()
//                        }
//
//                    }
//
//                    val setOfGroups = createGroupsOfApplications(mapOfQueries)
//
//                    val tripleGroupsIntentsPermissions =
//                        HashSet<Triple<Pair<String, HashSet<String>>, HashSet<String>, HashSet<String>>>()
//
//                    for (group in setOfGroups) {
//                        val totalIntentsPerGroup = HashSet<String>()
//                        val totalPermissionsPerGroup = HashSet<String>()
//
//                        mapOfIntents[group.first]?.forEach { totalIntentsPerGroup.add(it) }
//
//                        mapOfPermissions[group.first]?.forEach { totalPermissionsPerGroup.add(it) }
//
//
//                        for (app in group.second) {
//                            mapOfIntents[app]?.forEach { totalIntentsPerGroup.add(it) }
//                            mapOfPermissions[app]?.forEach { totalPermissionsPerGroup.add(it) }
//                        }
//
//
//                        val addTriple =
//                            Triple(
//                                first = group,
//                                second = totalIntentsPerGroup,
//                                third = totalPermissionsPerGroup
//                            )
//                        tripleGroupsIntentsPermissions.add(addTriple)
//                    }
//
//                    Log.d(
//                        MainActivity::class.java.simpleName,
//                        "AFTER COLLECTION OF INTENTS AND PERMISSIONS!!!"
//                    )
//
//                }
//            },
//        ) {
//            Text("X")
//        }
//    }

    internal fun isAccessGranted(): Boolean {
        return try {
            val packageManager: PackageManager = packageManager
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?
            var mode = 0
            mode = appOpsManager!!.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


}