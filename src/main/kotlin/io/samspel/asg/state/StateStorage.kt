package main.kotlin.io.samspel.asg.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
        name = "StateStorage"
)
class StateStorage : PersistentStateComponent<StateStorage> {

    var hasAngularCli: Boolean = false
    var argHtml: Boolean = false
    var argSpec: Boolean = true
    var argStyle: Boolean = true
    var argRouting: Boolean = false

    override fun getState(): StateStorage? {
        return this
    }

    override fun loadState(state: StateStorage) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): StateStorage {
            return ServiceManager.getService(project, StateStorage::class.java)
        }
    }
}
