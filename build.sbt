name := "git-branch-splitter"

organization := "com.sudohalt"

version := "0.2"

scalaVersion := "2.13.1"

libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"
libraryDependencies +=  "org.yaml" % "snakeyaml" % "1.25"

mainClass in Compile := Some("com.sudohalt.Cli")

assemblyOption in assembly := (assemblyOption in assembly).value

addArtifact(artifact in (Compile, assembly), assembly).settings