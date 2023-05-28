package com.xbaimiao.template

import com.xbaimiao.easylib.EasyPlugin
import com.xbaimiao.easylib.module.command.registerCommand

@Suppress("unused")
class EasyTemplate : EasyPlugin() {

    init {
        // 代表使用ui根据模块 不使用不会监听ui点击
        useUIModule()
    }

    override fun load() {

    }

    override fun enable() {
        registerCommand(CommandPort::class.java)
        rootCommand.register()
    }

    override fun active() {

    }

    override fun disable() {

    }

}