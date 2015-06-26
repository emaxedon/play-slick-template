name := """play-slick-template"""

version := "0.1.0"

scalaVersion := "2.11.6"

scalacOptions ++= Seq( "-deprecation", "-feature", "-language:postfixOps", "-language:implicitConversions", "-language:existentials" )

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
	jdbc,
	anorm,
	cache,
	ws,
	"com.typesafe.play" %% "play-slick" % "0.8.1",
	"com.typesafe.play" %% "play-mailer" % "2.4.1",
	"com.github.tototoshi" %% "scala-csv" % "1.2.1",
	"org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
	"org.mindrot" % "jbcrypt" % "0.3m",
	"org.webjars" %% "webjars-play" % "2.3.0",
	"org.webjars" % "jquery" % "2.1.4",
	"org.webjars" % "angularjs" % "1.4.0",
	"org.webjars" % "angular-ui-bootstrap" % "0.13.0",
	"org.webjars" % "bootstrap" % "3.3.4",
	"org.webjars" % "requirejs" % "2.1.17"
)

includeFilter in (Assets, LessKeys.less) := "*.less"