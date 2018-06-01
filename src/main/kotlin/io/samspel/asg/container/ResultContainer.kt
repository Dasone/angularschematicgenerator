package main.kotlin.io.samspel.asg.container

class ResultContainer(val schematicName: String, val schematicType: String, vararg params: ArgumentContainer){
    val args = params
}