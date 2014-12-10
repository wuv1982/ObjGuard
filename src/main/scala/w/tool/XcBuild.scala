package w.tool

import java.io.File
import java.io.FileReader
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.BufferedWriter
import java.io.FileWriter
import scala.io.Source
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import w.util.file.FileTreeWalker
import w.util.file.FileUtil

case class XcBuild(
	sourceDir: String,
	xcProjFile: String) {

	def renameFiles(map: Map[String, String]) = {

		FileTreeWalker(file => {
			val absPath = file.toFile().getAbsolutePath()
			val nameSuffix = file.toFile.getName
			println(s"visited $nameSuffix")

			if (!nameSuffix.startsWith(".")) {
				// get new file path
				val newAbsPath = map.get(nameSuffix).map { randomName =>
					println(s"rename ${nameSuffix} to $randomName")
					absPath.replaceAll(nameSuffix, randomName)
				}.getOrElse(absPath)

				// rename file
				Files.move(file, Paths.get(newAbsPath))

				// update file
				FileUtil.update(newAbsPath) { readLine =>
					var writeLine = readLine
					if (readLine.contains("#import")) {
						var break = false
						for (
							(name, randomName) <- map if !break
						) {
							if (readLine.contains(name)) {
								println(s"update line $readLine")
								writeLine = readLine.replaceAll(name, randomName)
								break = true
							}
						}
					}
					Some(writeLine)
				}
			} else {
				println(s"ignore $nameSuffix")
			}

			FileVisitResult.CONTINUE
		}).walk(Paths.get(sourceDir))

	}

	def updateXcProj(map: Map[String, String]) = {
		println(s"update $xcProjFile")
		FileUtil.update(xcProjFile) { readLine =>
			var writeLine = readLine
			var break = false
			for ((name, randomName) <- map if !break) {
				if (readLine.contains(name)) {
					println(s"update line $name -> $randomName")
					break = true
					writeLine = readLine.replaceAll(name, randomName)
				}
			}
			Some(writeLine)
		}
	}

}