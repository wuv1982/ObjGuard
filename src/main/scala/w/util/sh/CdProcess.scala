package w.util.sh

case class CdProcess(dir: String = "/tmp") {

	import sys.process.Process
	import sys.process.ProcessBuilder._

	case class CurDir(cwd: java.io.File)

	implicit def stringToCurDir(d: String) = CurDir(new java.io.File(d))

	implicit def stringToProcess(cmd: String)(implicit curDir: CurDir) =
		Process(cmd, curDir.cwd)

	implicit var cwd: CurDir = dir

	def cd(dir: String = util.Properties.userDir) = cwd = dir
}