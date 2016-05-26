import AssemblyKeys._

name := "scalastyle"

organization := "org.jetbrains"

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-deprecation", "-feature")

crossScalaVersions := Seq("2.10.5", "2.11.6", "2.12.0-M4")

description := "Scalastyle style checker for Scala"

libraryDependencies ++= Seq(
                        "org.scalariform" %% "scalariform" % "0.2.0",
                        "com.typesafe" % "config" % "1.2.0",
                        "junit" % "junit" % "4.11" % "test",
                        "com.novocode" % "junit-interface" % "0.10" % "test",
                        "com.google.guava" % "guava" % "17.0" % "test")

libraryDependencies ++= scala212Deps.value

fork in Test := true

javaOptions in Test += "-Dfile.encoding=UTF-8"

//coverageHighlighting := {
  //if (scalaBinaryVersion.value == "2.10") false
  //else true
//}

def scala212Deps = Def.setting {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2,12)) => Seq("org.scalatest" %% "scalatest" % "2.2.6" )
    case Some((2,_)) => Seq("org.scalatest" %% "scalatest" % "2.2.4")
  }
}


licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

assemblySettings

artifact in (Compile, assembly) ~= { art =>
  art.copy(`classifier` = Some("batch"))
}

addArtifact(artifact in (Compile, assembly), assembly)

mainClass in assembly := Some("org.scalastyle.Main")

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](organization, name, version, scalaVersion, sbtVersion)

buildInfoPackage := "org.scalastyle"

seq(filterSettings: _*)

aether.Aether.aetherLocalRepo := Path.userHome / "dev" / "repo"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Managed

releaseSettings

ReleaseKeys.crossBuild := true

val dynamicPublish = Def.taskDyn {
  if (version.value.trim.endsWith("SNAPSHOT")) {
    Def.task { publish.value }
  } else {
    Def.task { PgpKeys.publishSigned.value }
  }
}

ReleaseKeys.publishArtifactsAction := dynamicPublish.value

publishArtifact in Test := false

publishMavenStyle := false

bintrayOrganization := Some("jetbrains")

bintrayRepository := "scala-plugin-deps"
