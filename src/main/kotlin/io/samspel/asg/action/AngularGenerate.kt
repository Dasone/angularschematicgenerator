package main.kotlin.io.samspel.asg.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import main.kotlin.io.samspel.asg.command.runCommand
import main.kotlin.io.samspel.asg.feedback.showException
import main.kotlin.io.samspel.asg.state.StateStorage
import main.kotlin.io.samspel.asg.ui.GenerateAngularDialog
import java.io.File

class AngularGenerate : AnAction("AngularGenerate") {
    override fun actionPerformed(event: AnActionEvent) {
        try {
            val project = event.project
            project?.let {

                val state = StateStorage.getInstance(project)
                if (!state.hasAngularCli) {
                    if (!"cmd /c ng --version".runCommand(File(event.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.path))!!.contains("angular")) {
                        Messages.showErrorDialog("No Angular CLI where found on your machine.\nMake sure that you have installed Angular and all it dependencies first.", "No Angular CLI found")
                        state.hasAngularCli = false
                        return
                    }
                }

                state.hasAngularCli = true

                val inputDialog = GenerateAngularDialog(project)
                inputDialog.pack()
                inputDialog.isVisible = true

                if (!inputDialog.isPressedOk) {
                    return
                }

                val result = inputDialog.result

                val path = event.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)

                val type = result.schematicType.toLowerCase()
                val name = result.schematicName
                var argsString = ""
                for (arg in result.args) {
                    argsString += " " + if (arg.value) arg.flagTrue else arg.flagFalse
                }

                val cmd = "cmd /c ng g $type $name $argsString"
                cmd.runCommand(File(path?.path))

                project.baseDir.refresh(true, true)

            }
        } catch (e: Throwable) {
            showException(e)
            throw e
        }
    }
}