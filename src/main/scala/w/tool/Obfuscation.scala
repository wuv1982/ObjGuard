package w.tool

import scala.io.Source
import scala.util.Random
import java.lang.Boolean
import java.io.FileOutputStream
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.IOException
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import w.util.str.RandomStr

case class Obfuscation(
	symbolFile: String,
	macroFile: String,
	mapFile: String) {

	private def getSymbolList(): Seq[String] = {
		println("get symbol list")
		Source.fromFile(symbolFile, "utf-8").getLines.toList.filter { line =>
			println(s"read line $line")
			line != "" && !line.startsWith("#")
		}
	}

	private def hasDuplicateElement[T](list: Seq[T]): Boolean = {
		list.headOption.map { elem =>
			for (otherElem <- list.tail) {
				if (elem == otherElem) {
					println("duplicated element! : ${elem)")
					return true
				}
			}
			hasDuplicateElement(list.tail)
		}.getOrElse {
			println("has no duplicated element")
			false
		}
	}

	def makeMacroFile: Seq[(String, String)] = {
		val symbolMap = getSymbolList()
			.map { symbol =>

				val randomName = RandomStr.nextStr(5)
				println(s"randomName = ${randomName}")

				(symbol, randomName)
			}

		val randomNameList = symbolMap.map { case (symbol, randomName) => randomName }

		if (!hasDuplicateElement[String](randomNameList)) {
			val symbolMapText = symbolMap.foldLeft("") {
				case (acc, (symbol, randomName)) =>
					acc + s"${symbol} \t ${randomName} \n"
			}

			Future {
				var buf: BufferedWriter = null
				try {
					buf = new BufferedWriter(new FileWriter(mapFile))
					buf.write(symbolMapText)
				} finally {
					buf.close()
				}
			}

			val symbolMacroText = symbolMap
				.foldLeft("") {
					case (acc, (symbol, randomName)) =>
						val macroStr = s"""
#ifndef ${symbol}
#define ${symbol} ${randomName}
#endif
"""
						acc + macroStr
				}

			Files.delete(Paths.get(macroFile))

			Future {
				var buf: BufferedWriter = null
				try {
					buf = new BufferedWriter(new FileWriter(macroFile))
					buf.write(symbolMacroText)
				} finally {
					buf.close()
				}
			}
		}

		symbolMap.flatMap {
			case (symbol, randomName) =>
				Seq((symbol + ".h", randomName + ".h"), (symbol + ".m", randomName + ".m"))
		}
	}
}