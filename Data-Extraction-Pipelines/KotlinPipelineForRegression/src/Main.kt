import java.io.File
import java.util.zip.ZipFile

data class Triple(var first: Int, var second: Int, var third: Int)

val permissionsOS = ArrayList<String>()
val intentsOS = ArrayList<String>()
val applicationProperties = ArrayList<String>()
val features = ArrayList<String>()

val listAllAdded = ArrayList<Pair<String, Int>>()

//first is number of inputs, second is the sum of the values
val mapAverageCase = HashMap<String, Pair<Int, Int>>()
val testDataList = ArrayList<Pair<String, Int>>()

fun saveToAll(detection: Int, input: String) {
    listAllAdded.add(Pair(input, detection))
}

fun saveToMapAverage(detection: Int, input: String) {
    if (mapAverageCase.contains(input)) {
        val auxSize = mapAverageCase[input]!!.first + 1
        val auxSum = mapAverageCase[input]!!.second + detection
        mapAverageCase[input] = Pair(auxSize, auxSum)
    } else {
        mapAverageCase[input] = Pair(1, detection)
    }
}

fun saveToTestDataList(detection: Int, input: String) {
    testDataList.add(Pair(input, detection))
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


fun preprocessingData(manifest: String, filepathDirectory: String, packageName: String): ArrayList<Long> {
    val permissionsApplication = getListOfPermissionsFromManifest(manifest)
    val intentsApplication = getListOfIntentsFromManifest(manifest)
    val applicationPropertiesApplication = getApplicationTagProperties(manifest)
    val featuresApplication = usesFeaturesList(manifest)

    val inputByteArray = ArrayList<Long>()
    val metadataCSV = File("$filepathDirectory\\metadata.csv")

    val inputMetadata = metadataCSV.inputStream()
    val reader = inputMetadata.bufferedReader()
    reader.readLine()
    var vt = 0;
    val virus_total = reader.lineSequence()
        .filter {
            it.isNotBlank() && it.split(',')[1] == packageName
        }
        .map {
            val (_, _, _, _, vt_detection) = it.split(',', ignoreCase = false, limit = 6)
            vt_detection
        }.first()

    if (virus_total.toIntOrNull() == null) {
        if (filepathDirectory.matches(Regex("LowRisk$")))
            vt = 0
        if (filepathDirectory.matches(Regex("MediumRisk$")))
            vt = 3;
        if (filepathDirectory.matches(Regex("HighRisk$")))
            vt = 7;
    } else
        vt = virus_total.toInt()

    inputByteArray.add(vt.toLong())


    permissionsOS.forEach {
        if (permissionsApplication.first.contains(it))
            inputByteArray.add(1)
        else
            inputByteArray.add(0)
    }

    inputByteArray.add(permissionsApplication.second.toLong())

    intentsOS.forEach {
        if (intentsApplication.contains(it))
            inputByteArray.add(intentsApplication[it]!!.toLong())
        else
            inputByteArray.add(0)
    }
    intentsOS.forEach {
        if (intentsApplication.contains(it))
            inputByteArray.add(1)
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
            inputByteArray.add(applicationPropertiesApplication[index].toLong())
        }
    }

    features.forEach {
        if (featuresApplication.contains(it))
            inputByteArray.add(featuresApplication[it]!!.toLong())
        else
            inputByteArray.add(0)
    }


    return inputByteArray
}


fun headerFile(outputFile: File) {
    var header = "CATEGORY"
    permissionsOS.forEach {
        header += ",$it"
    }
    header += ",CUSTOMPERMISSIONS"

    intentsOS.forEach {
        header += ",$it"
    }

//    applicationProperties.forEach {
//        header += ",$it"
//    }

//    features.forEach {
//        header += ",$it"
//    }

    outputFile.writeText(header + "\n")
}


fun main() {
    println("Hello World!")

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
    val outputFileAll = File("$startingDirectory\\regression\\test2_perm_int_nr\\inputDataAll.csv")
    val outputFileAverage = File("$startingDirectory\\regression\\test2_perm_int_nr\\inputDataAverage.csv")
    val testData = File("$startingDirectory\\regression\\test2_perm_int_nr\\testData.csv")

    val types = ArrayList<String>()
    types.add("LowRisk")
    types.add("HighRisk")
    types.add("MediumRisk")

    headerFile(outputFileAll)
    headerFile(outputFileAverage)

    for (entry in types) {
        File(startingDirectory + "\\" + entry).walkTopDown().forEach { file ->
            run {
                if (file.name.matches(".*\\.apk$".toRegex())) {
                    val manifest = getManifest(file.absolutePath)
                    val packageName = file.nameWithoutExtension
                    println(file.name)
                    val inputByteArray = preprocessingData(manifest, startingDirectory + "\\" + entry, packageName)
                    var stringToWrite = ""
//                    inputByteArray.add(file.length())
                    for (i in 0 until inputByteArray.size) {
                        stringToWrite += inputByteArray[i].toString()

                        if (i < inputByteArray.size - 1)
                            stringToWrite += ","
                    }
//                    inputByteArray.add(file.length())
                    stringToWrite += "\n";
                    saveToAll(inputByteArray[0].toInt(), stringToWrite.substringAfter(','))
                    saveToMapAverage(inputByteArray[0].toInt(), stringToWrite.substringAfter(','))
//                    saveToMapInputAllCases(inputByteArray[0], stringToWrite.substring(2))
//                    outputFile.appendText(stringToWrite)
                }
            }
        }
    }

    listAllAdded.forEach {
        val currentLine = "${it.second.toString()},${it.first}"
        outputFileAll.appendText(currentLine)
    }


    mapAverageCase.forEach { (key, value) ->
        val average: Double = (value.second.toDouble() / value.first)
        val currentLine = "$average,$key"
        outputFileAverage.appendText(currentLine)
    }


    //test data
    headerFile(testData)
    File("$startingDirectory\\TestRisk").walkTopDown().forEach { file ->
        run {
            if (file.name.matches(".*\\.apk$".toRegex())) {
                val manifest = getManifest(file.absolutePath)
                val packageName = file.nameWithoutExtension
                println(file.name)
                val inputByteArray = preprocessingData(manifest, "$startingDirectory\\TestRisk", packageName)
                var stringToWrite = ""
                for (i in 0 until inputByteArray.size) {
                    stringToWrite += inputByteArray[i].toString()

                    if (i < inputByteArray.size - 1)
                        stringToWrite += ","
                }
                stringToWrite += "\n";
                saveToTestDataList(inputByteArray[0].toInt(), stringToWrite.substringAfter(','))
            }
        }
    }

    testDataList.forEach { pair ->
        val currentLine = "${pair.second.toString()},${pair.first}"
        testData.appendText(currentLine)
    }


    println("Finished parsing")

}