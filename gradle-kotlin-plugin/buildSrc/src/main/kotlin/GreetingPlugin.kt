import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

open class MyPluginExtension {
    var message: String? = null
    var greeter: String? = null
}

class MyPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Add the 'greeting' extension object
        val extension = project.extensions.create<MyPluginExtension>("greeting")
        // Add a task that uses configuration from the extension object
        project.task("hello") {
            doLast {
                println("${extension.message} from ${extension.greeter}")
            }
        }
    }

}