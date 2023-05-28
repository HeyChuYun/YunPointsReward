package com.xbaimiao.template

import com.xbaimiao.easylib.bridge.replacePlaceholder
import com.xbaimiao.easylib.module.chat.Lang.sendLang
import com.xbaimiao.easylib.module.item.buildItem
import com.xbaimiao.easylib.module.sound.parseToESound
import com.xbaimiao.easylib.module.ui.Basic
import com.xbaimiao.easylib.module.utils.parseToXMaterial
import com.xbaimiao.easylib.xseries.XMaterial
import org.bukkit.entity.Player

/**
 * @author 小白
 * @date 2023/5/26 19:51
 **/
object Menu {

    fun test(player: Player) {
        // "测试菜单 %player_name%".replacePlaceholder(player) 代表进行Papi变量替换
        val basic = Basic(player, "测试菜单 %player_name%".replacePlaceholder(player))
        // 代表这个菜单有多少行
        basic.rows(6)

        basic.slots.addAll(
            arrayListOf(
                "   B    B".toCharArray().toList(),
                "   B    B".toCharArray().toList(),
                "   B    B".toCharArray().toList(),
                "   B    B".toCharArray().toList(),
                "   B    B".toCharArray().toList(),
                "   B    B".toCharArray().toList()
            )
        )

        "坤之歌".parseToESound().playSound(player)

        basic.set('B', buildItem(XMaterial.ACACIA_BOAT))

        basic.set(10, buildItem(XMaterial.STONE) {
            name = "我是石头"
            lore.add("我是大石头")
        })
        basic.onClick(10) {
            player.sendLang("你点击了10号位置")
        }

        basic.onClick {
            // 所有点击都会传入这里
        }

        basic.onDrag {
            // 所有拖动都会传入这里
        }


        basic.openAsync()
    }

}