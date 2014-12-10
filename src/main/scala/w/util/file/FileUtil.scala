package w.util.file

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import scala.io.Source
import java.io.FileOutputStream
import scala.util.Properties

object FileUtil {

	def cp(source: String, target: String) = {
		val sourcePath = Paths.get(source)
		val targetPath = Paths.get(target)

		val copy: Path => FileVisitResult = file => {
			val targetFile = targetPath.resolve(sourcePath.relativize(file))
			Files.copy(file, targetFile)
			println(s"cp $file to $targetFile")
			FileVisitResult.CONTINUE
		}

		FileTreeWalker(copy, copy).walk(sourcePath)
	}

	def rm(target: String) = {
		val targetPath = Paths.get(target)

		val remove: Path => FileVisitResult = file => {
			Files.delete(file)
			println(s"remove $file")
			FileVisitResult.CONTINUE
		}

		if (Files.exists(targetPath)) {
			FileTreeWalker(onFile = remove, postDir = remove).walk(targetPath)
		}
	}

	def update(target: String)(updateLine: String => Option[String]) {
		val targetPath = Paths.get(target)
		val backup = s"$target.bk"
		val backupPath = Paths.get(backup)
		if (Files.exists(backupPath)) {
			Files.delete(backupPath)
		}
		Files.move(targetPath, backupPath)

		var bufWriter: BufferedWriter = null
		try {
			bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "utf-8"))

			for (readLine <- Source.fromFile(backup, "utf-8").getLines) {
				updateLine(readLine).map { newline =>
					bufWriter.write(newline + "\n")
				}
			}
			Files.delete(backupPath)
		} finally {
			bufWriter.close()
		}
	}
}