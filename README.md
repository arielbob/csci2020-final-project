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

Run main menu using Gradle's run command.

```bash
$ gradle run
```

Play the game by itself using the "tetris" task

```bash
$ gradle tetris
```
