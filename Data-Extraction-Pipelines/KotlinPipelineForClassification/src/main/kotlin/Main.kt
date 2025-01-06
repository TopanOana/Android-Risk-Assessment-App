import java.io.File
import java.util.zip.ZipFile

data class Triple(var first: Int, var second: Int, var third: Int)

val permissionsOS = ArrayList<String>()
val intentsOS = ArrayList<String>()
val applicationProperties = ArrayList<String>()
val features = ArrayList<String>()

val mapInputWorstCaseScenario = HashMap<String, Int>()
val mapInputMajorityScenario = HashMap<String, Triple>()
val listAllAdded = ArrayList<Pair<String, Int>>()
val testDataList = ArrayList<Pair<String, Int>>()

fun saveToTestDataList(risk: Int, input: String) {
    testDataList.add(Pair(input, risk))
}


fun saveToMapInputWithWorstCaseScenario(risk: Int, input: String) {
    if (mapInputWorstCaseScenario.contains(input)) {
        if (risk > mapInputWorstCaseScenario[input]!!) {
            mapInputWorstCaseScenario[input] = risk
        }
    } else {
        mapInputWorstCaseScenario[input] = risk
    }
}

fun saveToMapInputAllCases(risk: Int, input: String) {
    listAllAdded.add(Pair(input, risk))
}

fun saveToMapInputMajorityScenario(risk: Int, input: String) {
    if (!mapInputMajorityScenario.contains(input)) {
        mapInputMajorityScenario[input] = Triple(0, 0, 0)
    }

    when (risk) {
        0 -> mapInputMajorityScenario[input]?.first = (mapInputMajorityScenario[input]?.first ?: 0) + 1
        1 -> mapInputMajorityScenario[input]?.second = (mapInputMajorityScenario[input]?.second ?: 0) + 1
        2 -> mapInputMajorityScenario[input]?.third = (mapInputMajorityScenario[input]?.third ?: 0) + 1
    }
}

fun getManifest(filePath: String): String {
    var zip: ZipFile? = null
    var decompressXML: String = ""
    try {
        zip =
            ZipFile(File(filePath))
        val inputStream = zip.getInputStream(zip.getEntry("AndroidManifest.xml"))
        val inputData = Inputs.readAllAndClose(inputStream)
        val androidXMLDecompress = AndroidXMLDecompress()
        decompressXML = androidXMLDecompress.decompressXML(inputData)
    } catch (exception: Exception) {
        println(exception.toString())
    } finally {
        zip?.close()
    }
    return decompressXML
}

fun preprocessingData(manifest: String, filepathDirectory: String, packageName: String): ArrayList<Int> {
    val permissionsApplication = getListOfPermissionsFromManifest(manifest)
    val intentsApplication = getListOfIntentsFromManifest(manifest)
    val applicationPropertiesApplication = getApplicationTagProperties(manifest)
    val featuresApplication = usesFeaturesList(manifest)

    val inputByteArray = ArrayList<Int>()
    val metadataCSV = File("$filepathDirectory\\metadata.csv")

    val inputMetadata = metadataCSV.inputStream()
    val reader = inputMetadata.bufferedReader()
    reader.readLine()
    var vt: Int = 0;
    val virus_total = reader.lineSequence()
        .filter {
            it.isNotBlank() && it.split(',')[1] == packageName
        }
        .map {
            val (_, _, _, _, vt_detection) = it.split(',', ignoreCase = false, limit = 6)
            vt_detection
        }.first()

    if (virus_total.equals("None", true)) {
        if (filepathDirectory.matches(Regex("LowRisk$")))
            vt = 0
        if (filepathDirectory.matches(Regex("MediumRisk$")))
            vt = 1;
        if (filepathDirectory.matches(Regex("HighRisk$")))
            vt = 2;
    } else
        if (virus_total.toInt() < 4) {
            vt = 0
        } else if (virus_total.toInt() >= 4 && virus_total.toInt() <= 7) {
            vt = 1
        } else {
            vt = 2
        }
    inputByteArray.add(vt)


    permissionsOS.forEach {
        if (permissionsApplication.first.contains(it))
            inputByteArray.add(1)
        else
            inputByteArray.add(0)
    }

    inputByteArray.add(permissionsApplication.second)

    intentsOS.forEach {
        if (intentsApplication.contains(it))
            inputByteArray.add(intentsApplication[it]!!)
        else
            inputByteArray.add(0)
    }


    if (applicationPropertiesApplication.size == 0) {
        inputByteArray.add(0)
        inputByteArray.add(1)
        inputByteArray.add(1)
        inputByteArray.add(1)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(1)
        inputByteArray.add(0)
        inputByteArray.add(1)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(1)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(0)
        inputByteArray.add(1)
        inputByteArray.add(1)
    } else {
        applicationProperties.forEachIndexed { index, i ->
            inputByteArray.add(applicationPropertiesApplication[index])
        }
    }

    features.forEach {
        if (featuresApplication.contains(it))
            inputByteArray.add(featuresApplication[it]!!)
        else
            inputByteArray.add(0)
    }

    return inputByteArray
}

fun headerFile(outputFile: File): Unit {
    var header = "CATEGORY"
    permissionsOS.forEach {
        header += ",$it"
    }
    header += ",CUSTOMPERMISSIONS"

    intentsOS.forEach {
        header += ",$it"
    }

    applicationProperties.forEach {
        header += ",$it"
    }

    features.forEach {
        header += ",$it"
    }

    outputFile.writeText(header + "\n")
}


fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")


    //open file for permissions
    val permissionsFile =
        File("D:\\Desktop\\licenta_research\\testing_datasets\\antimalwareapp\\allpermissions.txt")
    permissionsFile.forEachLine {
        permissionsOS.add(it.trim())
    }


    //open file for intents
    val intentsFile = File("D:\\Desktop\\licenta_research\\testing_datasets\\antimalwareapp\\intents.txt")
    intentsFile.forEachLine {
        intentsOS.add(it.trim())
    }

    val applicationFile = File("D:\\Desktop\\licenta_research\\testing_datasets\\antimalwareapp\\applicationProps.txt")
    applicationFile.forEachLine {
        applicationProperties.add(it.trim())
    }

    val featuresFile = File("D:\\Desktop\\licenta_research\\testing_datasets\\antimalwareapp\\features.txt")
    featuresFile.forEachLine {
        features.add(it.trim())
    }

    val startingDirectory = "D:\\Desktop\\licenta_research\\testing_datasets\\androzoo"
    val outputFileWorstCase =
        File("$startingDirectory\\classification\\test4_appprop_perm_int_feat\\inputDataWorstCaseScenario.csv")
    val outputFileMajority =
        File("$startingDirectory\\classification\\test4_appprop_perm_int_feat\\inputDataMajorityScenario.csv")
    val outputFileAll = File("$startingDirectory\\classification\\test4_appprop_perm_int_feat\\inputDataAll.csv")
    val testData = File("$startingDirectory\\classification\\test4_appprop_perm_int_feat\\testData.csv")

    val types = ArrayList<String>()
    types.add("LowRisk")
    types.add("HighRisk")
    types.add("MediumRisk")

    headerFile(outputFileWorstCase)
    headerFile(outputFileMajority)
    headerFile(outputFileAll)

    for (entry in types) {
        File(startingDirectory + "\\" + entry).walkTopDown().forEach { file ->
            run {
                if (file.name.matches(".*\\.apk$".toRegex())) {
                    val manifest = getManifest(file.absolutePath)
                    val packageName = file.nameWithoutExtension
                    println(file.name)
                    val inputByteArray = preprocessingData(manifest, startingDirectory + "\\" + entry, packageName)
                    var stringToWrite = "";
                    for (i in 0 until inputByteArray.size) {
                        stringToWrite += inputByteArray[i].toString()

                        if (i < inputByteArray.size - 1)
                            stringToWrite += ","
                    }
                    stringToWrite += "\n";
                    saveToMapInputWithWorstCaseScenario(inputByteArray[0], stringToWrite.substringAfter(','))
                    saveToMapInputMajorityScenario(inputByteArray[0], stringToWrite.substringAfter(','))
                    saveToMapInputAllCases(inputByteArray[0], stringToWrite.substringAfter(','))
//                    outputFile.appendText(stringToWrite)
                }
            }
        }
    }


    listAllAdded.forEachIndexed { index, pair ->
        val currentLine = pair.second.toString() + "," + pair.first
        outputFileAll.appendText(currentLine)
    }

    mapInputWorstCaseScenario.toList().forEachIndexed { index, pair ->
        val currentLine = pair.second.toString() + "," + pair.first
        outputFileWorstCase.appendText(currentLine)
    }

    mapInputMajorityScenario.toList().forEachIndexed { index, pair ->
        val finalVal: Int = if (pair.second.first > pair.second.second) {
            if (pair.second.first > pair.second.third)
                0
            else
                1
        } else {
            if (pair.second.second > pair.second.third) {
                1
            } else {
                2
            }
        }
        val currentLine = finalVal.toString() + "," + pair.first
        outputFileMajority.appendText(currentLine)
    }


    ///test data
    headerFile(testData);
    File("$startingDirectory\\TestRisk").walkTopDown().forEach { file ->
        run {
            if (file.name.matches(".*\\.apk$".toRegex())) {
                val manifest = getManifest(file.absolutePath)
                val packageName = file.nameWithoutExtension
                println(file.name)
                val inputByteArray = preprocessingData(manifest, "$startingDirectory\\TestRisk", packageName)
                var stringToWrite = "";
                for (i in 0 until inputByteArray.size) {
                    stringToWrite += inputByteArray[i].toString()

                    if (i < inputByteArray.size - 1)
                        stringToWrite += ","
                }
                stringToWrite += "\n";
                saveToTestDataList(inputByteArray[0], stringToWrite.substringAfter(','))
            }
        }
    }

    testDataList.forEach { pair ->
        val currentLine = pair.second.toString() + "," + pair.first
        testData.appendText(currentLine)
    }

    println("finished parsing it all")
}
