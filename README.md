
![Logo](/app/src/main/resources/Logos/logo_large.png)

# Chat system

A local network decentralized lightweight chat system in Java

## 1. Features

- Detection of all connected users on local network
- Two-sided conversation opening
- Sending messages to other users
- History of previous messages

## 2. Demo

![Demo](/app/src/main/resources/demo_chat_system_cropped.gif)

## 3. Installation and execution

### 3.1 Using installers

Install for :

- [Windows (.exe)](https://github.com/MonsieurMK/chat_system/releases/download/v0.1.3/chatsystem-0.1.3.exe)
- [Linux (.deb)](https://github.com/MonsieurMK/chat_system/releases/download/v0.1.3/chatsystem-0.1.3.deb)
- MacOS (.dmg) *no pre-releases on MacOS*

And simply launch the installed executable application

### 3.2 Using jar archive

The ```.jar``` archive can be downloaded [here](https://github.com/MonsieurMK/chat_system/releases/download/v0.1.3/chatsystem-0.1.3.jar).

And it can be run with the following program arguments :

| Argument        | Shortname | Type      | Default value | Description                    |
|-----------------|-----------|-----------|---------------|--------------------------------|
| `title`         | `t`       | `string`  | chat_system   | Title of the window            |
| `tcpServerPort` | `ts`      | `integer` | 9000          | TCP server port                |
| `tcpClientPort` | `tc`      | `integer` | 9000          | TCP client port                |
| `udpServerPort` | `us`      | `integer` | 9001          | UDP server port                |
| `udpClientPort` | `uc`      | `integer` | 9001          | UDP client port                |
| `username`      | `n`       | `string`  | noUsername    | Username                       |
| `isFullscreen`  | `fs`      | `boolean` | true          | Execution in fullscreen or not |

## 4. Documentation

[Full javadoc](https://monsieurmk.github.io/chat_system/)


## 5. Author

[Morgan H. Pelloux](https://www.github.com/MonsieurMK)


## 6. Feedback

If you have any feedback, please reach out to us at morganhpelloux@gmail.com