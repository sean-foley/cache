# cacheserver-client-java

This project is part of the proof-of-concept for implementing a cacheserver written in Java. This
specific project/subsystem implements a client library for interacting with the cacheserver.
This library provides support to distribute the key/values over a cluster of cacheservers.
For instance, if you have 50 cacheservers, this client library can "shard" the keys across all 50 servers.


## Getting Started

Make sure you have a Java JDK (not JRE) installed on your target machine. That is the only requirement.
However, if you plan on viewing/editing the code, I recommend using your favorite Integrated
Development Environment (IDE).  IntelliJ IDEA was used for this project.

### Prerequisites

The project is written in Java.  You will need to have a Java Development Kit (JDK) setup
on the target/host machine. I tested on a debian based machine and will show setup for that.
Please see http://openjdk.java.net/install/ for setup instructions for different host OS's.

```
# Debian/Ubuntu based hosts
sudo apt-get install -y openjdk-8-jdk
```

You will also need a git client if you plan to contribute or do not want to download the zip.
You can get a git client from https://git-scm.com/downloads
```
# Debian/Ubuntu based hosts
sudo apt-get install -y git
```
Gradle is used for the build automation. Gradle wrappers are provided (and preferred). Installing
Gradle is optional and only required if you need to contribute to the build automation by
regenerating the wrappers for a different Gradle version, etc. You can get Gradle from
https://docs.gradle.org/current/userguide/installation.html
```
# Debian/Ubuntu based hosts
sudo apt-get install -y gradle
```

### Installing

Please see the [project root](../readme.md) for instructions.

After following the project root install instructions, open a
terminal window/command prompt and cd into the ./cacheserver-client-java folder
```
# Builds just the cacheserver-client-java if you are in the proper folder
./gradlew build
```

## Running the tests

Tests are stored under the ./src/test folder.  Build output test artifacts can be found
under ./build/reports/tests for nice web-based reports, and ./build/test-results for xml results.

```
# Run the tests
./gradlew test
```

#### Misc Gradle Tasks

```
# Clean the build
./gradlew clean

# Building
./gradlew build

# Showing other configured tasks
./gradlew tasks
```

## Built With

* [OpenJdk](http://openjdk.java.net/) - The Java JDK used
* [Gradle](https://gradle.org/) - Dependency Management
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - Integrated Development Environment

## Authors

* **Sean Foley** - *Initial work*

## License

You are free to use this code anyway you see fit.

## Acknowledgments

Big thanks for the readme.md template
https://gist.github.com/PurpleBooth/109311bb0361f32d87a2
