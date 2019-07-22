import Deps._
import sbt.Keys.scalaVersion

lazy val root = (project in file(".")).
  aggregate(paymentservice, MyTaxiPaymentsConsumer).
  settings(
    inThisBuild(List(
      organization := "com.mytaxi.data.test.paymentservice",
      scalaVersion := "2.12.4",
      resolvers += "io.confluent" at "http://packages.confluent.io/maven/"
    )),
    name := "MyTaxiApplication"
  )
lazy val paymentservice = (project in file ("paymentservice")).
  settings(
    name := "paymentservice",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.mytaxi.data.test.paymentservice.Paymentservice"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaLogging,
      scallop)
  )

lazy val MyTaxiPaymentsConsumer = (project in file ("MyTaxiPaymentsConsumer")).
  settings(
    name := "MyTaxiPaymentsConsumer",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.example.examplesub.Main"),
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      cassandra,
      scalaLogging,
      scallop,
      scalaConfig,
      scalaParserCombinators)
  ).dependsOn(paymentservice)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case PathList("META-INF/io.netty.versions.properties") => MergeStrategy.discard
  case x => MergeStrategy.first
}

//excludedFiles in Assembly := { (base: Seq[File]) =>
//  (((base / "META-INF" ** "*") ---
//      (base / "META-INF" / "services" ** "*") ---
//      (base / "META-INF" / "maven" ** "*"))).get }

//mappings in (Compile, packageSrc) += {
//  ((resourceManaged in Compile).value / "User.scala") -> "paymentservice/src/main/scala/com/mytaxi/data/test/paymentservice/User.scala"
//}