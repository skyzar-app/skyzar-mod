package app.skyzar.skyzarmod

import app.skyzar.skyzarmod.commands.SkyzarCommand
import app.skyzar.skyzarmod.core.Config
import app.skyzar.skyzarmod.events.packet.PacketListener
import app.skyzar.skyzarmod.util.HttpUtils
import com.google.common.collect.Maps
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
    modid = Skyzar.MOD_ID,
    name = Skyzar.MOD_NAME,
    version = Skyzar.VERSION
)
class Skyzar {

    companion object {
        const val MOD_ID = "skyzarmod"
        const val MOD_NAME = "Skyzar"//try that
        const val VERSION = "1.0"
        const val configLocation = "./config/SkyzarMod.toml"

        val mc: Minecraft = Minecraft.getMinecraft()
        var config: Config? = null
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        config = Config()
        config?.preload()

        SkyzarCommand().register()

        MinecraftForge.EVENT_BUS.register(PacketListener())
    }
}
