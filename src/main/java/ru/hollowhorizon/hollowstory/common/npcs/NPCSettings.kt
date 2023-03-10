package ru.hollowhorizon.hollowstory.common.npcs

import kotlinx.serialization.Serializable
import net.minecraft.util.SoundEvent
import ru.hollowhorizon.hc.client.utils.nbt.*
import java.io.File

fun main() {
    val settings = NPCSettings().apply {
        name = "Андрей"

    }

    val nbt = NBTFormat.serialize(settings)
    val file = File("npc.nbt")

    nbt.save(file.outputStream())

}

@Serializable
data class NPCSettings(
    var name: String = "NPC",
    var puppetEntity: String = "minecraft:zombie",
    var data: NPCData = NPCData(),
)

fun makeSettings(builder: NPCSettings.() -> Unit): NPCSettings = NPCSettings().apply(builder)

@Serializable
data class NPCData(
    var health: Float = 20f,
    var damage: Float = 1f,
    var armor: Float = 0f,
    var ignoreLighting: Boolean = false,
    var isUndead: Boolean = false,
    var sound: @Serializable(ForSoundEvent::class) SoundEvent? = null,
)