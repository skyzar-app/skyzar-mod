package app.skyzar.skyzarmod.commands

import com.google.common.collect.Maps
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import app.skyzar.skyzarmod.util.HttpUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import net.minecraft.util.ChatStyle
import net.minecraft.util.EnumChatFormatting
import scala.collection.parallel.ParIterableLike.Min

class SkyzarCommand : Command("skyzar") {
    @DefaultHandler
    fun handle() {
        Thread(Runnable {
            val headers = Maps.newHashMap<String, String>()
            headers["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36"
            val res = HttpUtils().sendRequest(HttpUtils.HttpRequest("", "GET", headers, ""))
//            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(res.body))
            val data = JsonParser().parse(res.body).asJsonObject.get("data").asJsonObject.get("buy_price").asDouble
            Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText("The buy price for enchanted oak wood is: $data coins").setChatStyle(
                ChatStyle().setColor(EnumChatFormatting.AQUA)
            ))
        }).start()
    }
}
