# cacheserver

This project is part of the proof-of-concept for implementing a cacheserver written in Java. This
specific project/subsyststem implements a simple cacheserver that listens on a socket for
clients that wish to store key/value pairs.


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
terminal window/command prompt and cd into the ./cacheserver folder
```
# Builds just the cacheserver if you are in the proper folder
./gradlew build
```

If you made it this far, now we can try a demo of running the cacheserver.
If everything is working, the client should be able to store key/values
in the cacheserver.

Open up a new terminal/command prompt and let this run.
```
# Run the cacheserver. Make sure you see task run and then
# the message "cacheserver started and ready for connections."
# when you are done running the test, press CTRL-C to break.
./cgradlew run
```

Open up a new terminal (tested with a linux telnet client)
```
# connect to the cacheserver on port 5000
telnet localhost 5000

# Server replies with "Cache Server - let's store some stuff"
# means you're connected (woot woot!)
# let's try adding something. Type the following in and press enter
# when done.
+add mykey awesomevalue

# If you got a +OK things are good, a -ERR means something didn't work
# lets try retrieving the value assuming it was successfully added
+get mykey

# The server should reply with "+OK awesomevalue"
# If that happened, we can try removing the key
+remove mykey

# The server should reply with a +OK
# If we try to get the same key again,
# it won't be in the cache and should
# return an -ERRR
+get mykey

# Server should reply with -ERR key not found

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
