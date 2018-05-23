package main.kotlin.io.samspel.asg.feedback

import com.intellij.openapi.ui.Messages


fun showException(e: Throwable) {
    Messages.showErrorDialog("I am sorry, Angular Schematic Generator have occurred a RuntimeException.\nYou could try again later or recover to an older version,\nor you could post an issue here:\nhttps://github.com/Dasone/angularschematicgenerator\nWe will fixed it soon!", "Fatal error")
}