// Scala version used
scalaVersion in Global := "2.12.6"

// Project details
name := "roxana"
description := "Reactive UI component framework for Scala.js applications"
version in Global := "0.1.0-SNAPSHOT"
organization in Global := "com.norcane.roxana"
licenses in Global += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
homepage in Global := Some(url("https://github.com/norcane/roxana"))

// More info for Maven Central
developers in Global := List(
  Developer(
    id = "vaclav.svejcar",
    name = "Vaclav Svejcar",
    email = "vaclav.svejcar@gmail.com",
    url = url("https://github.com/vaclavsvejcar")
  )
)

scmInfo in Global := Some(
  ScmInfo(
    url("https://github.com/norcane/roxana"),
    "scm:git@github.com:norcane/roxana.git"
  )
)

// Bintray configuration
bintrayOrganization := Some("norcane")
bintrayRepository := "roxana"

lazy val roxana = (project in file("."))
  .dependsOn(roxanaCore).aggregate(roxanaCore)

lazy val roxanaCore = (project in file("roxana-core"))
  .settings(
    name := "roxana-core",
    libraryDependencies ++= Seq(
      /// runtime dependencies
      "com.lihaoyi" %%% "scalatags" % "0.6.7",
      "com.lihaoyi" %%% "scalarx" % "0.4.0",

      /// test dependencies
      "com.lihaoyi" %%% "utest" % "0.6.4" % "test"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv
  )
  .enablePlugins(ScalaJSPlugin)

lazy val examples = (project in file("roxana-examples"))
  .settings(
    name := "roxana-examples",
    scalaJSUseMainModuleInitializer := true
  )
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(WorkbenchPlugin)
  .dependsOn(roxana).aggregate(roxana)

scalacOptions in Global := Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-Ypartial-unification"
)