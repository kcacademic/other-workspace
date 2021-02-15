/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Scala library project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/6.8/userguide/building_java_projects.html
 */

plugins {
    // Apply the scala Plugin to add support for Scala.
    scala

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    // Use JCenter for resolving dependencies.
    jcenter()
}

dependencies {
    // Use Scala 2.13 in our library project
    implementation("org.scala-lang:scala-library:2.13.3")

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation("com.google.guava:guava:29.0-jre")

    // Use Scalatest for testing our library
    testImplementation("junit:junit:4.13")
    testImplementation("org.scalatest:scalatest_2.13:3.2.2")
    testImplementation("org.scalatestplus:junit-4-12_2.13:3.2.2.0")

    // Need scala-xml at test runtime
    testRuntimeOnly("org.scala-lang.modules:scala-xml_2.13:1.2.0")

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("org.apache.commons:commons-math3:3.6.1")
}
