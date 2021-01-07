import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import java.io.File

class CodeLinesCounterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("codeLines") {
            doLast {
                println("Hello from the CodeLinesCounterPlugin")
				printCodeLinesCount(project)
            }
        }
    }
	
	private fun printCodeLinesCount(project: Project) {
       var totalCount = 0
       println("Total lines: $totalCount")
   }
}