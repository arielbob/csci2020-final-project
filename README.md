# CSCI2020U Winter 2019 Final Project

## About

This is a two-player networked Tetris game for our final project for CSCI2020U (Software Systems Development and Integration).

### Contributors

- Ariel Bobadilla ([arielbob](https://github.com/arielbob)): client/server mechanics, socket I/O, multi-threading, user interface

- Luke Tran ([Luke-Tran](https://github.com/Luke-Tran)): gameplay mechanics, user key inputs, user interface, file I/O

- Mitchell Theriault ([Mitchtee22](https://github.com/Mitchtee22)): main menu user interface

### Repository URL

- https://github.com/arielbob/csci2020-final-project

# How to use

## Dependencies

- [Java Development Kit 11 (JDK 11)](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)

- [JavaFX SDK 11](https://gluonhq.com/products/javafx/)

- [Gradle](https://gradle.org/install/)

## Installation

Clone the repository:

```bash
$ git clone https://github.com/arielbob/csci2020-final-project.git
```

Change to the cloned directory:

```bash
$ cd csci2020-final-project/
```

## Compiling

Compile using Gradle's build command:

```bash
$ gradle build
```

## Running

Run main menu using Gradle's run command

```bash
$ gradle run
```

Can also run the main menu using the "runUI" task

```bash
$ gradle runUI
```

Play the game by itself using the "tetris" task

```bash
$ gradle tetris
```

## Playing

**To play single player:**

1. When the program starts, press the "SOLO" button.

**To view player statistics:**

1. When the program starts, press the "VIEW STATS" button.

**To host a game:**

1. When the program starts, go to "MULTIPLAYER" -> "CREATE GAME"

2. Enter a username and a port number between 0 and 65535 (inclusive)

3. Press "Create Server"

4. Once you can see your opponent's username under the right board, press "Start Game"

**To join a game:**

1. When the program starts, go to "MULTIPLAYER" -> "JOIN GAME"

2. Enter in your username, the server address in dot-decimal notation, and the same port number that the server is using.
If playing against yourself, you can simply enter in "localhost" for the server address.

3. Press "Join Server" then wait for the host to start the game
