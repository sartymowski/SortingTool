package sorting

import java.io.File
import kotlin.math.roundToInt

abstract class DataType(private val sortingType: String) {
    protected abstract fun doProcessDataNatural(data: List<String>): List<Any>
    protected abstract fun doProcessByCount(data: List<String>): List<Pair<Any, Int>>

    protected open fun doReadData(inputFile: String): List<String> = File(inputFile).readLines()

    protected abstract fun doCollectData(): List<String>

    protected abstract fun printByCountData(data: List<Pair<Any, Int>>, outputFile: String?)
    protected abstract fun printNaturalData(data: List<Any>, outputFile: String?)

    fun processData(inputFile: String? = null, outputFile: String? = null) {
        val data = inputFile?.let { doReadData(inputFile) } ?: doCollectData()

        if (sortingType == "natural") {
            printNaturalData(doProcessDataNatural(data), outputFile)
        } else {
            printByCountData(doProcessByCount(data), outputFile)
        }
    }
}

class LineDataType(sortingType: String) : DataType(sortingType) {
    override fun doCollectData(): List<String> {
        var lines = mutableListOf<String>()
        while (true) {
            val line: String = readlnOrNull() ?: break
            lines.add(line)
        }

        return lines
    }

    override fun printByCountData(data: List<Pair<Any, Int>>, outputFile: String?) {
        val size = data.sumOf { it.second }
        var result = "Total lines: ${size}.\n"
        data.forEach { elem -> result += "${elem.first}: ${elem.second} time(s), ${((elem.second.toFloat() / size.toFloat()) * 100.0).roundToInt()}%\n" }
        outputFile?.let { File(it).writeText(result) } ?: run { println(result) }
    }

    override fun printNaturalData(data: List<Any>, outputFile: String?) {
        var result = "Total lines: ${data.size}.\n" + "Sorted data: ${data.joinToString(separator = " ")}"
        outputFile?.let { File(it).writeText(result) } ?: run { println(result) }
    }

    override fun doProcessByCount(data: List<String>): List<Pair<String, Int>> {
        return data.groupingBy { it }.eachCount().toList().sortedWith(
            compareBy({ it.second }, { it.first })
        )
    }

    override fun doProcessDataNatural(data: List<String>): List<Any> {
        return data.sorted()
    }
}

class LongDataType(sortingType: String) : DataType(sortingType) {
    override fun doCollectData(): List<String> {
        var numbers = mutableListOf<String>()
        while (true) {
            val line: String = readlnOrNull() ?: break
            numbers.addAll(line.split(' ').filter { it.isNotEmpty() }
                .mapNotNull {
                    if (it.toIntOrNull() != null) it else {
                        println("\"${it}\" is not a long. It will be skipped.")
                        null
                    }
                })
        }

        return numbers
    }
    override fun printByCountData(data: List<Pair<Any, Int>>, outputFile: String?) {
        val size = data.sumOf { it.second }
        var result = "Total words: ${size}.\n"
        data.forEach { elem -> result += "${elem.first}: ${elem.second} time(s), ${((elem.second.toFloat() / size.toFloat()) * 100.0).roundToInt()}%\n" }
        outputFile?.let { File(it).writeText(result) } ?: run { println(result) }
    }

    override fun printNaturalData(data: List<Any>, outputFile: String?) {
        var result = "Total words: ${data.size}.\n" + "Sorted data: ${data.joinToString(separator = " ")}"
        outputFile?.let { File(it).writeText(result) } ?: run { println(result) }
    }

    override fun doProcessByCount(data: List<String>): List<Pair<Any, Int>> {
        return data.groupingBy { it.toInt() }.eachCount().toList().sortedWith(
            compareBy({ it.second }, { it.first })
        )
    }

    override fun doProcessDataNatural(data: List<String>): List<Any> {
        return data.map { it.toInt() }.sorted()
    }
}

class WordDataType(sortingType: String) : DataType(sortingType) {

    override fun doReadData(inputFile: String): List<String> {
        var words = mutableListOf<String>()
        for (line in File(inputFile).readLines()) {
            words.addAll(line.split(' ').filter { it.isNotEmpty() })
        }

        return words
    }

    override fun doCollectData(): List<String> {
        var words = mutableListOf<String>()
        while (true) {
            val line: String = readlnOrNull() ?: break
            words.addAll(line.split(' ').filter { it.isNotEmpty() })
        }

        return words
    }

    override fun printByCountData(data: List<Pair<Any, Int>>, outputFile: String?) {
        val size = data.sumOf { it.second }
        var result = "Total lines: ${size}.\n"
        data.forEach { elem -> result += "${elem.first}: ${elem.second} time(s), ${((elem.second.toFloat() / size.toFloat()) * 100.0).roundToInt()}%\n" }
        outputFile?.let { File(it).writeText(result) } ?: run { println(result) }
    }

    override fun printNaturalData(data: List<Any>, outputFile: String?) {
        var result = "Total lines: ${data.size}." + "Sorted data: ${data.joinToString(separator = " ")}"
        outputFile?.let { File(it).writeText(result) } ?: run { println(result) }
    }

    override fun doProcessByCount(data: List<String>): List<Pair<Any, Int>> {
        return data.groupingBy { it }.eachCount().toList().sortedWith(
            compareBy({ it.second }, { it.first })
        )
    }

    override fun doProcessDataNatural(data: List<String>): List<Any> {
        return data.sorted()
    }
}

class DataTypeFactory {
    companion object {
        fun getDataTypeObj(type: String, sortingType: String): DataType? =
            when (type) {
                "long" -> LongDataType(sortingType)
                "line" -> LineDataType(sortingType)
                "word" -> WordDataType(sortingType)
                else -> {
                    assert(false)
                    null
                }
            }

    }
}

fun invalidateArgs(args: Array<String>): Map<String, String> {
    val newArgs = mutableMapOf<String, String>()
    val knownArguments = listOf("-sortingType", "-dataType", "-inputFile", "-outputFile")

    if (args.size == 1) {
        if (args.contains("-sortingType")) {
            throw Exception("No sorting type defined!")
        } else if (args.contains("-dataType")) {
            throw Exception("No data type defined!")
        }
    }

    var it = 0
    while (it < args.size) {
        if (knownArguments.contains(args[it])) {
            newArgs[args[it]] = args[it + 1]
            it += 2
        } else {
            println("\"${args[it]}\" is not a valid parameter. It will be skipped.")
            it++
        }
    }

    if (!newArgs.containsKey("-sortingType")) {
        newArgs["-sortingType"] = "natural"
    }

    if (!newArgs.containsKey("-dataType")) {
        newArgs["-dataType"] = "word"
    }

    return newArgs
}

fun main(args: Array<String>) {
    try {
        val newArgs = invalidateArgs(args)
        DataTypeFactory.getDataTypeObj(newArgs["-dataType"]!!, newArgs["-sortingType"]!!)!!.processData(inputFile = newArgs["-inputFile"], outputFile = newArgs["-outputFile"])
    } catch (ex: Exception) {
        println(ex.message)
    }
}