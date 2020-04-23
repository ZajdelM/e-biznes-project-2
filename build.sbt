name := "sklep"
 
version := "1.0" 
      
lazy val `sklep` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.8"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies ++= Seq( "com.typesafe.play" %% "play-slick" % "4.0.0" )
libraryDependencies ++= Seq( "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0" )
libraryDependencies ++= Seq( "org.xerial" % "sqlite-jdbc" % "3.30.1" )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      