package w.tool

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import w.util.sh.CdProcess
import w.util.str.RandomStr

object ObjGuard extends App {

	val conf: Config = ConfigFactory.load

	val FILE_ROOT = conf.getString("input.workspace")
	val FILE_ROOT_WORK = conf.getString("input.backup")
	val FILE_PROJECT = conf.getString("input.project")
	val PROJECT_NAME = conf.getString("projectName")

	val FILE_INPUT_SYMBOL_LIST = conf.getString("input.symbolList")
	val FILE_OUTPUT_MAP = conf.getString("output.map")
	val FILE_OUTPUT_MACRO = conf.getString("output.macro")
	val FILE_OUTPUT_BUILD = conf.getString("output.build")
	val FILE_OUTPUT_LIB = conf.getString("output.lib")

	val FILE_INPUT_SOURCE = conf.getString("input.source")

	val FILE_XBUILD_PROJECT = s"${FILE_PROJECT}/${PROJECT_NAME}.xcodeproj/project.pbxproj"

	val cdProcess = CdProcess()
	import cdProcess._

	s"rm -r $FILE_ROOT_WORK".!
	s"cp -fR $FILE_ROOT $FILE_ROOT_WORK".!

	val map = Obfuscation(FILE_INPUT_SYMBOL_LIST, FILE_OUTPUT_MACRO, FILE_OUTPUT_MAP).makeMacroFile.toMap

	val xcBuild = XcBuild(FILE_INPUT_SOURCE, FILE_XBUILD_PROJECT)
	xcBuild.renameFiles(map)
	xcBuild.updateXcProj(map)

	cd(s"$FILE_PROJECT")
	"pwd".!
	"bash build.sh".!

	s"mv ${FILE_OUTPUT_BUILD}/${FILE_OUTPUT_LIB} ${FILE_OUTPUT_BUILD}/${RandomStr.nextStr(5)}.a".!
}