package com.xbaimiao.template

import com.xbaimiao.easylib.module.command.CommandBody
import com.xbaimiao.easylib.module.command.CommandHeader
import com.xbaimiao.easylib.module.command.command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author 小白
 * @date 2023/5/26 19:57
 **/
// 第一种写法
@CommandHeader(command = "test", description = "描述", permission = "命令使用权限")
class CommandPort {

    @CommandBody // command<CommandSender> 所有类型都可以执行
    val a = command<CommandSender>("a") {
        description = "执行a"
        exec {
            sender.sendMessage("你执行了 /test a")
        }
    }

    @CommandBody  // command<Player> 为只能玩家类型执行
    val b = command<Player>("b") {
        description = "执行b"
        exec {
            player.sendMessage("你执行了 /test b")
        }
    }


}

// 第二种写法
private val test21 = command<CommandSender>("a"){
    exec {
        sender.sendMessage("你执行了 /test2 a")
    }
}

private val test22 = command<Player>("b"){
    exec {
        player.sendMessage("你执行了 /test2 b")
    }
}

val rootCommand = command<CommandSender>("test2"){
    description ="描述"
    permission = "执行权限"
    sub(test21)
    sub(test22)
}