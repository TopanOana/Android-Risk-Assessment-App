package com.example.androidproject

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import java.nio.IntBuffer
import java.nio.LongBuffer

class ScanViewModel() : ViewModel() {

    private var _uiState = MutableStateFlow(UIState(inProgress = false))
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()
    fun testApps(
        groups: HashSet<Pair<String, HashSet<String>>>,
        groupsInput: ArrayList<ArrayList<Long>>,
        ortSession: OrtSession,
        ortEnvironment: OrtEnvironment,
        withSystemApps: Boolean,
    ) {
        _uiState.update {
            it.copy(
                inProgress = true
            )
        }

        ///call the ai module
        aiModuleCall(groups, groupsInput, ortSession, ortEnvironment, withSystemApps)

        _uiState.update {
            it.copy(
                inProgress = false
            )
        }
    }

    fun setInProgress() {
        Log.d("ScanViewModel", "setInProgress: HAPPEN")
        _uiState.update {
            it.copy(
                inProgress = true
            )
        }
    }

    fun setNotInProgress() {
        Log.d("ScanViewModel", "setNotInProgress: Happen")
        _uiState.update {
            it.copy(
                inProgress = false
            )
        }
    }

    private fun aiModuleCall(
        groups: HashSet<Pair<String, HashSet<String>>>,
        groupsInput: ArrayList<ArrayList<Long>>,
        ortSession: OrtSession,
        ortEnvironment: OrtEnvironment,
        withSystemApps: Boolean,
    ) {
        val outputList = mutableListOf<Pair<List<String>, Int>>()
//        val outputList = mutableListOf<Pair<List<String>, String>>()
        for ((index, group) in groups.withIndex()) {
            //getting a list of all the package names
            val currentGroupNames: MutableList<String> = mutableListOf<String>(group.first)
            group.second.forEach {
                currentGroupNames.add(it)
            }

            if (withSystemApps || currentGroupNames.filter { str -> str.contains(Regex("^(com\\.(google|android)\\.)|(android\\.?)")) }
                    .isEmpty()) {
                val predictedValue = runClassifier(groupsInput[index], ortSession, ortEnvironment)
                var predictionString: String
//                when (predictedValue) {
//                    0L -> predictionString = "low"
//                    1L -> predictionString = "medium"
//                    2L -> predictionString = "high"
//                    else -> {
//                        predictionString = "error"
//                        Log.e(
//                            "ScanViewModel::AI",
//                            "aiModuleCall: error from ai module! not [0,1,2]"
//                        )
//                    }
//                }
//                outputList.add(Pair(currentGroupNames, predictionString))
                outputList.add(Pair(currentGroupNames, predictedValue.toInt()))
            }


        }


//        sorting output list by risk
        outputList.sortByDescending { it.second }

        val finalOutputList = ArrayList<Pair<List<String>, String>>()
        outputList.forEach {
            var predictionString: String
            when (it.second) {
                0 -> predictionString = "low"
                1 -> predictionString = "medium"
                2 -> predictionString = "high"
                else -> {
                    predictionString = "error"
                    Log.e("ScanViewModel::AI", "aiModuleCall: error from ai module! not [0,1,2]")
                }
            }

            finalOutputList.add(Pair(it.first, predictionString))
        }

        _uiState.update {
            it.copy(
                listRiskApps = finalOutputList
            )
        }
    }


    private fun runClassifier(
        input: ArrayList<Long>,
        ortSession: OrtSession,
        ortEnvironment: OrtEnvironment,
    ): Long {
        //get name of input node
        val inputName = ortSession.inputNames?.iterator()?.next()

        //buffer for inputs
        val bufferInputs = LongBuffer.wrap(input.toLongArray())

        val inputTensor =
            OnnxTensor.createTensor(
                ortEnvironment,
                bufferInputs,
                longArrayOf(1, input.size.toLong())
            )

        val results = ortSession.run(mapOf(inputName to inputTensor))

        val output = results[0].value as LongArray
        return output[0]
    }

}

data class UIState(
    val listRiskApps: List<Pair<List<String>, String>> = emptyList(),
    val inProgress: Boolean = false,
)

class ScanViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScanViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}