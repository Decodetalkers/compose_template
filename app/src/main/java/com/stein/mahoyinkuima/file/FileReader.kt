package com.stein.mahoyinkuima.file

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import java.io.File
import java.io.FileInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val UpdateTimeFile = "/proc/uptime"
const val CpuInfo = "/proc/cpuinfo"
const val MemInfo = "/proc/meminfo"

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    object Begin : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Failure(val message: String) : Resource<Nothing>()
}

data class UpdateTime(val minuet: String, val second: String)

data class Memory(val freeMemory: String, val totalMemory: String)

data class CpuDataInfo(val name: String, val coreNumber: Int)

data class PhoneInfo(val memory: Memory, val cpuInfo: CpuDataInfo)

suspend fun readFile(fileName: String): String {
    return withContext(Dispatchers.IO) {
        val fis = FileInputStream(File(fileName)) // 2nd line
        fis.bufferedReader().use { it.readText() }
    }
}

suspend fun readCpu(): CpuDataInfo {
    val data = readFile(CpuInfo)

    val cpuinfos = data.trim().split("\n\n")
    if (cpuinfos.isEmpty()) {
        return CpuDataInfo("Unknown", 1)
    }
    val coreNumber = cpuinfos.size
    var name = ""
    val line_1 = cpuinfos[0]
    for (line in line_1.lines()) {
        if (line.startsWith("model name:")) {
            name = line.removePrefix("model name").trim()
        }
    }

    return CpuDataInfo(name, coreNumber)
}

suspend fun readMem(): Memory {
    val data = readFile(MemInfo)
    var free_mem = ""
    var total = ""
    for (lin in data.lines()) {
        if (!free_mem.isEmpty() && !total.isEmpty()) {
            break
        }
        if (lin.startsWith("MemTotal:")) {
            total = lin.removePrefix("MemTotal:").removeSuffix("kB").trim()
        } else if (lin.startsWith("MemFree:")) {
            free_mem = lin.removePrefix("MemFree::").removeSuffix("kB").trim()
        }
    }

    return Memory(free_mem, total)
}

suspend fun readUpdateTime(): UpdateTime {
    val data = readFile(UpdateTimeFile)
    val m_and_s = data.split(" ")
    if (m_and_s.size != 2) {
        return UpdateTime("", "")
    }

    val m = m_and_s[0]
    val s = m_and_s[1]
    val minitue_f = m.toFloatOrNull()
    var minitue = ""
    if (minitue_f != null) {
        minitue = (minitue_f / 60.0).toUInt().toString()
    }
    val second_f = s.toFloatOrNull()
    var second = ""
    if (second_f != null) {
        second = (second_f % 60.0).toUInt().toString()
    }
    return UpdateTime(minitue, second)
}

class PhoneInfoModel : ViewModel() {
    val state = mutableStateOf<Resource<PhoneInfo>>(Resource.Begin)
    fun load() {
        val thestate by state
        if (thestate is Resource.Success || thestate is Resource.Loading) return
        viewModelScope.launch {
            state.value = Resource.Loading
            // val updateTime = readUpdateTime()

            val cpuInfo = readCpu()
            val memory = readMem()
            state.value = Resource.Success(PhoneInfo(memory, cpuInfo))
        }
    }
}
