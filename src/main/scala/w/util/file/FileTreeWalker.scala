package w.util.file

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

case class FileTreeWalker(
	onFile: Path => FileVisitResult,
	preDir: Path => FileVisitResult = _ => FileVisitResult.CONTINUE,
	postDir: Path => FileVisitResult = _ => FileVisitResult.CONTINUE) {

	private class Visitor extends SimpleFileVisitor[Path] {
		override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
			preDir(dir)
		}

		override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
			onFile(file)
		}

		override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
			postDir(dir)
		}
	}

	def walk(start: Path) {
		Files.walkFileTree(start, new Visitor)
	}

}