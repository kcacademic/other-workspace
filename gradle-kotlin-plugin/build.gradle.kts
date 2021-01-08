apply<MyPlugin>()

configure<MyPluginExtension> {
    message = "Hi"
    greeter = "Gradle"
}

tasks.register<GreetingToFileTask>("greet") {
    destination = { project.extra["greetingFile"]!! }
}

tasks.register("sayGreeting") {
    dependsOn("greet")
    doLast {
        println(file(project.extra["greetingFile"]!!).readText())
    }
}

extra["greetingFile"] = "$buildDir/hello.txt"