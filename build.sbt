name := "ObjGuard"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

fork := true
javaOptions := Seq(
	"-Dfile.encoding=UTF-8",
	"-Dconfig.file=conf/ObjGuard.conf"
)
