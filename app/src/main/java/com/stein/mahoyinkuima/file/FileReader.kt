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
import com.stein.mahoyinkuima.common.Resource

const val UpdateTimeFile = "/proc/uptime"
const val CpuInfo = "/proc/cpuinfo"
const val MemInfo = "/proc/meminfo"
const val Felica = "/etc/felica/mfs.cfg"

data class Memory(val freeMemory: String, val totalMemory: String)

data class CpuDataInfo(val hardware: String, val coreNumber: Int)

data class FelicaData(val trouble: String, val region: String, val market: String)

data class PhoneInfo(val memory: Memory, val cpuInfo: CpuDataInfo, val felicaData: FelicaData?)

suspend fun readFile(fileName: String): String? {
    return withContext(Dispatchers.IO) {
        val file = File(fileName)
        if (!file.exists()) {
            null
        } else {
            val fis = FileInputStream(File(fileName)) // 2nd line
            fis.bufferedReader().use { it.readText() }
        }
    }
}

suspend fun readFelica(): FelicaData? {
    val data = readFile(Felica)
    if (data.isNullOrEmpty()) {
        return null
    }
    var trouble = ""
    var region = ""
    var market = ""
    for (line in data.lines()) {
        if (line.startsWith("0202030A,")) {
            trouble = line.removePrefix("0202030A,")
        } else if (line.startsWith("0202030C,")) {
            region = line.removePrefix("0202030C,")
        } else if (line.startsWith("02020A04,")) {
            market = line.removePrefix("02020A04,")
        }
    }
    return FelicaData(trouble, region, market)
}

suspend fun readCpu(): CpuDataInfo {
    val data = readFile(CpuInfo)

    val cpuinfos = data!!.trim().split("\n\n")
    if (cpuinfos.isEmpty()) {
        return CpuDataInfo("Unknown", 1)
    }
    val coreNumber = cpuinfos.size - 1
    var hardware = ""
    val line1 = cpuinfos[coreNumber]
    for (line in line1.lines()) {
        if (line.startsWith("Hardware")) {
            hardware = line.removePrefix("Hardware").replace(":", "").trim()
        }
    }

    return CpuDataInfo(hardware, coreNumber)
}

suspend fun readMem(): Memory {
    val data = readFile(MemInfo)
    var freeMem = ""
    var total = ""
    for (lin in data!!.lines()) {
        if (freeMem.isNotEmpty() && total.isNotEmpty()) {
            break
        }
        if (lin.startsWith("MemTotal:")) {
            val tmptotal =
                    lin.removePrefix("MemTotal:").removeSuffix("kB").trimStart().trimEnd().toFloat()
            total = "${"%.2f".format(tmptotal / 1024.0)} Mib"
        } else if (lin.startsWith("MemFree:")) {
            val tmpFree =
                    lin.removePrefix("MemFree:").removeSuffix("kB").trimStart().trimEnd().toFloat()
            freeMem = "${"%.2f".format(tmpFree / 1024.0)} Mib"
        }
    }

    return Memory(freeMem, total)
}

class PhoneInfoModel : ViewModel() {
    val state = mutableStateOf<Resource<PhoneInfo>>(Resource.Begin)
    fun load() {
        val theState by state
        if (theState is Resource.Success || theState is Resource.Loading) return
        viewModelScope.launch {
            state.value = Resource.Loading
            val cpuInfo = readCpu()
            val memory = readMem()
            val felicaData = readFelica()
            state.value = Resource.Success(PhoneInfo(memory, cpuInfo, felicaData))
        }
    }
}
