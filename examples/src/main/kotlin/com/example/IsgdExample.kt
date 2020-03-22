package com.example

import net.thauvin.erik.isgd.Isgd
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        args.forEach {
            if (it.contains("is.gd"))
                println(it + " <-- " + Isgd.lookup(it))
            else
                println(it + " --> " + Isgd.shorten(it))
        }
    } else {
        println("Try specifying one or more URLs as arguments.")
    }
    exitProcess(0)
}
