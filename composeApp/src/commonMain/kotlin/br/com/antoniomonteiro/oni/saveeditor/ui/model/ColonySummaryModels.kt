package br.com.antoniomonteiro.oni.saveeditor.ui.model

// Data models based on the React component's interfaces
data class DLC(val name: String, val enabled: Boolean)
data class BooleanSetting(val name: String, var enabled: Boolean)
data class GameSettingEnum(
    val name: String,
    val options: List<String>,
    var currentIndex: Int
)

// Mock data to populate the screen for now
object ColonySummaryMockData {
    const val fileName = "MyColony_Cycle500.sav"
    const val baseName = "New Haven"
    const val cycle = 500
    const val totalDuplicants = 12
    const val worldgenSeed = "SNDST-A-123456789-0"
    const val startingAsteroid = "Terra Asteroide"

    val dlcs = listOf(
        DLC(name = "Spaced Out!", enabled = true),
        DLC(name = "The Frosty Planet Pack", enabled = true),
        DLC(name = "The Bionic Booster Pack", enabled = false),
        DLC(name = "The Prehistoric Planet Pack", enabled = false),
    )

    val worldTraits = listOf("Geodes", "Metal Rich", "Frozen Core", "Subsurface Ocean")

    fun initialBooleanSettings() = listOf(
        BooleanSetting("Stress Reactions", enabled = true),
        BooleanSetting("Sandbox Mode", enabled = false),
        BooleanSetting("Teleporters", enabled = true),
        BooleanSetting("Care Packages", enabled = true),
        BooleanSetting("Save To Cloud", enabled = false),
    )

    fun initialGameSettings() = listOf(
        GameSettingEnum("Hunger", listOf("Tummyless", "Fasting", "Default", "Rumbly Tummies", "Ravenous"), 2),
        GameSettingEnum("Durability", listOf("Indestructible", "Reinforced", "Default", "Flimsy", "Threadbare"), 2),
        GameSettingEnum("Radiation", listOf("Nuke-Proof", "Healthy Glow", "Default", "Toxic Positivity", "Critical Mass"), 2),
        GameSettingEnum("Disease", listOf("Total Immunity", "Germ Resistant", "Default", "Germ Susceptible", "Outbreak Prone"), 2),
        GameSettingEnum("Morale", listOf("Totally Blasé", "Chill", "Default", "A Bit Persnickety", "Draconian"), 2),
        GameSettingEnum("Meteor Showers", listOf("Clear Skies", "Spring Showers", "Default", "Cosmic Storm", "Doomsday"), 2),
        GameSettingEnum("Stress", listOf("Cloud Nine", "Chipper", "Default", "Glum", "Frankly Depressing"), 2),
        GameSettingEnum("Bionic Wattage", listOf("Analog", "Energy Efficient", "Default", "Power Hungry", "Energy Vampire"), 2),
        GameSettingEnum("Demolior Impact", listOf("Disabled", "Far-Off Forecast", "Slightly Delayed", "Default", "Early Arrival", "Imminent Extinction"), 3),
    )
}
