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
lazy val paymentservice = (project in file("paymentservice")).
  settings(
    name := "paymentservice",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.mytaxi.data.test.paymentservice.Paymentservice"),
    assemblyJarName in assembly := "producer.jar",
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      scalaLogging,
      logbackClassic,
      scallop),
    assemblyMergeStrategy in assembly := {
      case PathList("org", "slf4j", xs@_*) => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

lazy val MyTaxiPaymentsConsumer = (project in file("MyTaxiPaymentsConsumer")).
  settings(
    name := "MyTaxiPaymentsConsumer",
    sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue,
    mainClass := Some("com.mytaxi.data.test.payments.PaymentsConsumer"),
    assemblyJarName in assembly := "consumer.jar",
    libraryDependencies ++= Seq(
      avroSerializer,
      kafkaClient,
      scalaArm,
      cassandra,
      scalaLogging,
      scallop,
      scalaConfig,
      logbackClassic,
      scalaParserCombinators),
    assemblyMergeStrategy in assembly := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.first
      case PathList("org", "slf4j", xs@_*) => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  ).dependsOn(paymentservice)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case PathList("org", "slf4j", xs@_*) => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
