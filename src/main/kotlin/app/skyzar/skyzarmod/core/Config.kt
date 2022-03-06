package app.skyzar.skyzarmod.core

import app.skyzar.skyzarmod.Skyzar
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType

import java.io.File

class Config : Vigilant(File(Skyzar.configLocation)) {

    @Property(
        type = PropertyType.TEXT,
        name = "API Key",
        description = "Enter your Skyzar API key here",
        category = "General"
    )
    var apiKey = ""

    init {
        addDependency("swagText", "toggleSwag")

        initialize()
    }
}
