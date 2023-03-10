package ru.hollowhorizon.hollowstory.common.files

import net.minecraftforge.fml.loading.FMLPaths
import java.io.File
import java.io.InputStream
import java.lang.IllegalStateException

object HollowStoryDirHelper {
    private val SCRIPTS_DIR = FMLPaths.GAMEDIR.get().resolve("hollowscript").toFile().apply {
        if(!exists()) mkdirs()
    }
    val CACHE_DIR = SCRIPTS_DIR.resolve(".cache").apply {
        if(!exists()) mkdirs()
    }

    @JvmStatic
    fun init() {

    }

    fun getScripts(): Collection<File> {
        val dir = FMLPaths.GAMEDIR.get().resolve("hollowscript").toFile()

        if(!dir.exists()) dir.mkdirs()

        val list = arrayListOf<File>()

        collectAllFiles(list, dir) { file ->
            return@collectAllFiles file.path.endsWith(".kts")
        }

        return list
    }

    fun getAllDialogues(): Collection<File> {
        return getScripts().filter { it.path.endsWith(".hsd.kts") }
    }

    fun getAllStoryEvents(): Collection<File> {
        return getScripts().filter { it.path.endsWith(".se.kts") }
    }

    fun findMainScript(): File? {
        val scripts = getScripts().filter { it.endsWith(".main.kts") }

        if (scripts.size > 1) throw IllegalStateException("Main Script can be only one!")

        if(scripts.isEmpty()) return null

        return scripts[0]
    }

    fun toReadablePath(file: File): String {
        return file.path.substringAfter(FMLPaths.GAMEDIR.get().resolve("hollowscript").toFile().path+"\\").replace("\\", "/")
    }

    fun fromReadablePath(path: String): File {
        return FMLPaths.GAMEDIR.get().resolve("hollowscript").resolve(path).toFile()
    }

    private fun collectAllFiles(list: MutableList<File>, startDir: File, predicate: (File) -> Boolean) {
        startDir.listFiles()?.forEach { file ->
            if(file.isDirectory) {
                collectAllFiles(list, file, predicate)
            } else if(predicate(file)) {
                list.add(file)
            }
        }
    }
}