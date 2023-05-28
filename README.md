快速创建一个带有EasyLib 的 Minecraft项目

## 菜单

> 需要在插件主类使用菜单模块

```kotlin
class EasyTemplate : EasyPlugin() {
    init {
        // 代表使用ui根据模块 不使用不会监听ui点击
        useUIModule()
    }
}
```

```kotlin

object Menu {

    fun test(player: Player) {
        // "测试菜单 %player_name%".replacePlaceholder(player) 代表进行Papi变量替换
        val basic = Basic(player, "测试菜单 %player_name%".replacePlaceholder(player))
        // 代表这个菜单有多少行
        basic.rows(6)

        // 设置10号位置的物品
        basic.set(10, buildItem(XMaterial.STONE) {
            name = "我是石头"
            lore.add("我是大石头")
        })
        // 设置点击10号位的反应
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

```

## 命令

### 第一种写法 需要执行注册
```kotlin
override fun enable() {
    registerCommand(CommandPort::class.java)
}
```

```kotlin

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
```

### 第二种写法 需要执行注册

```kotlin

override fun enable() {
    rootCommand.register()
}

```

```kotlin


// 第二种写法
private val test21 = command<CommandSender>("a") {
    exec {
        sender.sendMessage("你执行了 /test2 a")
    }
}

private val test22 = command<Player>("b") {
    exec {
        player.sendMessage("你执行了 /test2 b")
    }
}

val rootCommand = command<CommandSender>("test2") {
    description = "描述"
    permission = "执行权限"
    sub(test21)
    sub(test22)
}
```