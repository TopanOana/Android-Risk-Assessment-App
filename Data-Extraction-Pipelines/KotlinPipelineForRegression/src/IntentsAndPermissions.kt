import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory


//fun getListOfPermissionsFromManifest(
//    manifest: String
//): Pair<HashSet<String>, Int> {
//    val arrayOfPermissions = HashSet<String>()
//    val factory = DocumentBuilderFactory.newInstance()
//    var nrCustomPermissions = 0
//
//    try {
//        val builder = factory.newDocumentBuilder()
//        val inputStream = ByteArrayInputStream(manifest.toByteArray())
//        val document = builder.parse(inputStream)
//        val permissionNodes = document.getElementsByTagName("uses-permission")
//        for (i in 0 until permissionNodes.length) {
//            if (permissionNodes.item(i) is Element) {
//                val element = permissionNodes.item(i) as Element
//                val permissionName = element.getAttribute("name")
//                if (permissionName.matches(Regex("android.permission.*"))) {
//                    val newPermissionName = permissionName.substring(permissionName.lastIndexOf('.') + 1)
//                    arrayOfPermissions.add(newPermissionName)
//                } else {
//                    nrCustomPermissions++
//                }
////                arrayOfPermissions.add(permissionName)
//            }
//        }
//    } catch (exception: Exception) {
////        Log.d(MainActivity::class.qualifiedName, exception.toString())
//        println(exception.toString())
//    }
//
//    return Pair(arrayOfPermissions, nrCustomPermissions)
//}
//
//fun getListOfIntentsFromManifest(
//    manifest: String
//): HashMap<String, Int> {
//    val arrayOfIntents = HashMap<String, Int>()
//    val factory = DocumentBuilderFactory.newInstance()
//    try {
//        val builder = factory.newDocumentBuilder()
//        val inputStream = ByteArrayInputStream(manifest.toByteArray())
//        val document = builder.parse(inputStream)
//        val actionNodes = document.getElementsByTagName("action")
//        for (i in 0 until actionNodes.length) {
//            if (actionNodes.item(i) is Element) {
//                val element = actionNodes.item(i) as Element
//                val intentaction = element.getAttribute("name")
//                if (intentaction.matches(Regex("android.intent.action.*"))) {
//                    val newIntentAction = intentaction.substring(intentaction.lastIndexOf('.') + 1)
//                    if (arrayOfIntents.contains(newIntentAction))
//                        arrayOfIntents[newIntentAction] = arrayOfIntents[newIntentAction]!! + 1
//                    else
//                        arrayOfIntents[newIntentAction] = 1
////                    arrayOfIntents.add(newIntentAction)
//                }
////                arrayOfIntents.add(intentaction)
//            }
//        }
//
//        val categoryNodes = document.getElementsByTagName("category")
//        for (i in 0 until categoryNodes.length) {
//            if (categoryNodes.item(i) is Element) {
//                val element = categoryNodes.item(i) as Element
//                val intentaction = element.getAttribute("name")
//                if (intentaction.matches(Regex("android.intent.category.*"))) {
//                    val newIntentAction = intentaction.substring(intentaction.lastIndexOf('.') + 1)
//                    if (arrayOfIntents.contains(newIntentAction))
//                        arrayOfIntents[newIntentAction] = arrayOfIntents[newIntentAction]!! + 1
//                    else
//                        arrayOfIntents[newIntentAction] = 1
//                }
//            }
//        }
//
//        val dataNodes = document.getElementsByTagName("data")
//        for (i in 0 until dataNodes.length) {
//            if (dataNodes.item(i) is Element) {
//                val element = dataNodes.item(i) as Element
////                var intentData = element.getAttribute("android:data")
////                arrayOfIntents.add(intentData)
////                intentData = element.getAttribute("android:host")
////                arrayOfIntents.add(intentData)
////                intentData = element.getAttribute("android:port")
////                arrayOfIntents.add(intentData)
////                intentData = element.getAttribute("android:path")
////                arrayOfIntents.add(intentData)
//                val intentData = element.getAttribute("android:mimeType")
//                if (intentData != "") {
//                    if (arrayOfIntents.contains(intentData))
//                        arrayOfIntents[intentData] = arrayOfIntents[intentData]!! + 1
//                    else
//                        arrayOfIntents[intentData] = 1
//                }
//                val intData = element.getAttribute("android:scheme")
//                if (intData.matches(Regex("android.intent.data.*"))) {
//                    val newIntentAction = intData.substring(intData.lastIndexOf('.') + 1)
//                    if (arrayOfIntents.contains(newIntentAction))
//                        arrayOfIntents[newIntentAction] = arrayOfIntents[newIntentAction]!! + 1
//                    else
//                        arrayOfIntents[newIntentAction] = 1
//                }
//            }
//        }
//
//    } catch (exception: Exception) {
//        exception.printStackTrace()
//    }
//    return arrayOfIntents
//}

fun getListOfPermissionsFromManifest(
    manifest: String
): Pair<HashSet<String>, Int> {
    val arrayOfPermissions = HashSet<String>()
    val factory = DocumentBuilderFactory.newInstance()
    var nrCustomPermissions = 0

    try {
        val builder = factory.newDocumentBuilder()
        val inputStream = ByteArrayInputStream(manifest.toByteArray())
        val document = builder.parse(inputStream)
        val permissionNodes = document.getElementsByTagName("uses-permission")
        for (i in 0 until permissionNodes.length) {
            if (permissionNodes.item(i) is Element) {
                val element = permissionNodes.item(i) as Element
                val permissionName = element.getAttribute("name")
                if (permissionName.matches(Regex("android.permission.*"))) {
                    val newPermissionName = permissionName.substring(permissionName.lastIndexOf('.') + 1)
                    arrayOfPermissions.add(newPermissionName)
                } else {
                    nrCustomPermissions++
                }
//                arrayOfPermissions.add(permissionName)
            }
        }
    } catch (exception: Exception) {
//        Log.d(MainActivity::class.qualifiedName, exception.toString())
        println(exception.toString())
    }

    return Pair(arrayOfPermissions, nrCustomPermissions)
}

fun getListOfIntentsFromManifest(
    manifest: String
): HashMap<String, Int> {
    val arrayOfIntents = HashMap<String, Int>()
    val factory = DocumentBuilderFactory.newInstance()
    try {
        val builder = factory.newDocumentBuilder()
        val inputStream = ByteArrayInputStream(manifest.toByteArray())
        val document = builder.parse(inputStream)
        val actionNodes = document.getElementsByTagName("action")
        for (i in 0 until actionNodes.length) {
            if (actionNodes.item(i) is Element) {
                val element = actionNodes.item(i) as Element
                val intentaction = element.getAttribute("name")
                if (intentaction.matches(Regex("android.intent.action.*"))) {
                    val newIntentAction = intentaction.substring(intentaction.lastIndexOf('.') + 1)
                    if (arrayOfIntents.contains(newIntentAction))
                        arrayOfIntents[newIntentAction] = arrayOfIntents[newIntentAction]!! + 1
                    else
                        arrayOfIntents[newIntentAction] = 1
//                    arrayOfIntents.add(newIntentAction)
                }
//                arrayOfIntents.add(intentaction)
            }
        }

        val categoryNodes = document.getElementsByTagName("category")
        for (i in 0 until categoryNodes.length) {
            if (categoryNodes.item(i) is Element) {
                val element = categoryNodes.item(i) as Element
                val intentaction = element.getAttribute("name")
                if (intentaction.matches(Regex("android.intent.category.*"))) {
                    val newIntentAction = intentaction.substring(intentaction.lastIndexOf('.') + 1)
                    if (arrayOfIntents.contains(newIntentAction))
                        arrayOfIntents[newIntentAction] = arrayOfIntents[newIntentAction]!! + 1
                    else
                        arrayOfIntents[newIntentAction] = 1
                }
            }
        }

        val dataNodes = document.getElementsByTagName("data")
        for (i in 0 until dataNodes.length) {
            if (dataNodes.item(i) is Element) {
                val element = dataNodes.item(i) as Element
//                var intentData = element.getAttribute("android:data")
//                arrayOfIntents.add(intentData)
//                intentData = element.getAttribute("android:host")
//                arrayOfIntents.add(intentData)
//                intentData = element.getAttribute("android:port")
//                arrayOfIntents.add(intentData)
//                intentData = element.getAttribute("android:path")
//                arrayOfIntents.add(intentData)
                val intentData = element.getAttribute("android:mimeType")
                if (intentData != "") {
                    if (arrayOfIntents.contains(intentData))
                        arrayOfIntents[intentData] = arrayOfIntents[intentData]!! + 1
                    else
                        arrayOfIntents[intentData] = 1
                }
                val intData = element.getAttribute("android:scheme")
                if (intData.matches(Regex("android.intent.data.*"))) {
                    val newIntentAction = intData.substring(intData.lastIndexOf('.') + 1)
                    if (arrayOfIntents.contains(newIntentAction))
                        arrayOfIntents[newIntentAction] = arrayOfIntents[newIntentAction]!! + 1
                    else
                        arrayOfIntents[newIntentAction] = 1
                }
            }
        }

    } catch (exception: Exception) {
        exception.printStackTrace()
    }
    return arrayOfIntents
}

fun getApplicationTagProperties(manifest: String): ArrayList<Int> {
    val arrayList = ArrayList<Int>()
    val factory = DocumentBuilderFactory.newInstance()
    try {
        val builder = factory.newDocumentBuilder()
        val inputStream = ByteArrayInputStream(manifest.toByteArray())
        val document = builder.parse(inputStream)
        val applicationNodes = document.getElementsByTagName("application")
        //first tag is the relevent one
        if (applicationNodes.length != 0) {
            val element = applicationNodes.item(0) as Element

            //allowTaskReparenting
            val allowTaskReparenting = element.getAttribute("allowTaskReparenting")
            if (allowTaskReparenting == "" || allowTaskReparenting == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //allowBackup
            val allowBackup = element.getAttribute("allowBackup")
            if (allowBackup == "" || allowBackup == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //allowClearUserData
            val allowClearUserData = element.getAttribute("allowClearUserData")
            if (allowClearUserData == "" || allowClearUserData == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //allowNativeHeapPointerTagging
            val allowNativeHeapPointerTagging = element.getAttribute("allowNativeHeapPointerTagging")
            if (allowNativeHeapPointerTagging == "" || allowNativeHeapPointerTagging == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //appCategory
            val appCategory = element.getAttribute("appCategory")
            var check = false
            if (appCategory == "accessibility") {
                arrayList.add(1)
                check = true
            }
            if (appCategory == "audio") {
                arrayList.add(2)
                check = true
            }
            if (appCategory == "game") {
                arrayList.add(3)
                check = true
            }
            if (appCategory == "image") {
                arrayList.add(4)
                check = true
            }
            if (appCategory == "maps") {
                arrayList.add(5)
                check = true
            }
            if (appCategory == "news") {
                arrayList.add(6)
                check = true
            }
            if (appCategory == "productivity") {
                arrayList.add(7)
                check = true
            }
            if (appCategory == "social") {
                arrayList.add(8)
                check = true
            }
            if (appCategory == "video") {
                arrayList.add(9)
                check = true
            }
            if (appCategory == "" || !check)
                arrayList.add(0)


            //backupAgent -> string so it cant be uniform


            //backupInForeground
            val backupInForeground = element.getAttribute("backupInForeground")
            if (backupInForeground == "" || backupInForeground == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //banner -> resource so not helpful

            //dataExtractionRules -> string resource ->not helpful

            //debuggable
            val debuggable = element.getAttribute("debuggable")
            if (debuggable == "" || debuggable == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //description -> string not helpful

            //enabled
            val enabled = element.getAttribute("enabled")
            if (enabled == "" || enabled == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //extractNativeLibs -> default behaviour depends so unsure

            //fullBackupContent -> pointer to an xml file

            //fullBackupOnly
            val fullBackupOnly = element.getAttribute("fullBackupOnly")
            if (fullBackupOnly == "" || fullBackupOnly == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //gwpAsanMode -> honestly not going to test for this because it doesn't seem useful

            //hasCode
            val hasCode = element.getAttribute("hasCode")
            if (hasCode == "" || hasCode == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //hasFragileUserData
            val hasFragileUserData = element.getAttribute("hasFragileUserData")
            if (hasFragileUserData == "" || hasFragileUserData == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //icon -> not helpful

            //isGame
            val isGame = element.getAttribute("isGame")
            if (isGame == "" || isGame == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //isMonitoringTool
            val isMonitoringTool = element.getAttribute("isMonitoringTool")
            check = false
            if (isMonitoringTool == "parental_control") {
                arrayList.add(1)
                check = true
            }
            if (isMonitoringTool == "enterprise_management") {
                arrayList.add(2)
                check = true
            }
            if (isMonitoringTool == "other") {
                arrayList.add(3)
                check = true
            }
            if (isMonitoringTool == "" || !check)
                arrayList.add(0)

            //killAfterRestore
            val killAfterRestore = element.getAttribute("killAfterRestore")
            if (killAfterRestore == "" || killAfterRestore == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //largeHeap -> unsure how to check this

            //label -> reference to resource

            //logo -> reference to resource

            //manageSpaceActivity -> no default and unsure how to use

            //name -> optional and unnecessary

            //networkSecurityConfig -> xml resource file

            //permission -> ??? TODO

            //persistent
            val persistent = element.getAttribute("persistent")
            if (persistent == "" || persistent == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //process -> i don't know man

            //restoreAnyVersion
            val restoreAnyVersion = element.getAttribute("restoreAnyVersion")
            if (restoreAnyVersion == "" || restoreAnyVersion == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //requestLegacyExternalStorage ->nope!

            //requiredAccountType -> nope!

            //restrictedAccountType --> nope

            //supportsRtl
            val supportsRtl = element.getAttribute("supportsRtl")
            if (supportsRtl == "" || supportsRtl == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //taskAffinity -> unhelpful

            //testOnly -> not on playstore

            //theme unhelpful

            //uiOptions
            val uiOptions = element.getAttribute("uiOptions")
            if (uiOptions == "" || uiOptions == "none")
                arrayList.add(0)
            else
                arrayList.add(1)

            //usesCleartextTraffic -> checks for api TODO
            val usesCleartextTraffic = element.getAttribute("usesCleartextTraffic")
            if (usesCleartextTraffic == "" || usesCleartextTraffic == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //vmSafeMode
            val vmSafeMode = element.getAttribute("vmSafeMode")
            if (vmSafeMode == "" || vmSafeMode == "false")
                arrayList.add(0)
            else
                arrayList.add(1)

            //hardwareAccelerated
            val hardwareAccelerated = element.getAttribute("hardwareAccelerated")
            if (hardwareAccelerated == "" || hardwareAccelerated == "true")
                arrayList.add(1)
            else
                arrayList.add(0)

            //resizeableActivity
            val resizeableActivity = element.getAttribute("resizeableActivity")
            if (resizeableActivity == "" || resizeableActivity == "true")
                arrayList.add(1)
            else
                arrayList.add(0)


        } else {
            arrayList.add(0)
            arrayList.add(1)
            arrayList.add(1)
            arrayList.add(1)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(1)
            arrayList.add(0)
            arrayList.add(1)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(1)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(0)
            arrayList.add(1)
            arrayList.add(1)
        }
    } catch (exception: Exception) {
//        Log.d(MainActivity::class.qualifiedName, exception.toString())
        println(exception.toString())
    }
    return arrayList
}


fun usesFeaturesList(manifest: String): HashMap<String, Int> {
    val arrayOfFeatures = HashMap<String, Int>()
    val factory = DocumentBuilderFactory.newInstance()
    try {
        val builder = factory.newDocumentBuilder()
        val inputStream = ByteArrayInputStream(manifest.toByteArray())
        val document = builder.parse(inputStream)
        val featuresNodes = document.getElementsByTagName("uses-feature")
        for (i in 0 until featuresNodes.length) {
            if (featuresNodes.item(i) is Element) {
                val element = featuresNodes.item(i) as Element
                val featureName = element.getAttribute("name")
                if (featureName.matches(Regex("android.*"))) {
//                    val featureName = permissionName.substring(permissionName.lastIndexOf('.') + 1)

                    val featureRequired = element.getAttribute("required")
                    if (featureRequired == "" || featureRequired == "true")
                        arrayOfFeatures[featureName] = 2
                    else
                        arrayOfFeatures[featureName] = 1

                }
            }
        }
    } catch (exception: Exception) {
//        Log.d(MainActivity::class.qualifiedName, exception.toString())
        println(exception.toString())
    }
    return arrayOfFeatures
}