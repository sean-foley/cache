# Cache

This project is a proof-of-concept for a distributed cache implemented in java. The design goals
are to minimize dependencies and not over-abstract the design so it can serve as a learning
aide to someone new to programming.


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

Getting up and running should be as simple as cloning this project and then building
the source tree.

Create a folder to hold the project.

```
# On windows use a powershell window.
mkdir ./projects
cd ./projects
```

Clone the project.  This will create a ./cache folder

```
git clone https://github.com/sean-foley/cache
```

Build the source tree to make sure everything is working.  There is a master
build.gradle setup in the root that will handle building all of the sub-projects.

```
# go to the root of the project
cd ./cache

# This will build all of the projects. This will also
# dynamically resolve any dependencies, including downloading
# the proper version of gradle if needed.
./gradlew build

```

If you made it this far, now we can try a demo of running the cacheserver
and the cacheserver-client-example. If everything is working, the client
should be able to store key/values in the cacheserver.

Open up a new terminal/command prompt and let this run.
```
# Run the cacheserver. Make sure you see task run and then
# the message "cacheserver started and ready for connections."
# when you are done running the test, press CTRL-C to break.
cd ./projects/cache
./cacheserver/gradlew run
```

Open up another terminal/command prompt to run the example.
```
# Run the example. Make sure you see task run. This
# will try to add/get/remove values from the cache
# and display the results to the console.
cd ./projects/cache/cacheserver-client-example/
./gradlew run
```

## Project Organization/Subsystems

The tree is organized as follows:

./cache - root

./cache/cacheserver - the cacheserver that stores key/values

./cache/cacheserver-client-java - produces a client lib jar to make it easy for clients to use the server

./cache/cacheserver-client-example - example that shows how to use the client lib

Each one of these subsystems has its own Gradle build setup to facilitate working independently
across projects.

## Running the tests

Each subsystem has corresponding unit tests setup under the ./src/test folder. All of
the test framework dependencies are managed via Gradle in the corresponding build.gradle
file.

The project root (./cache) has a build.gradle file setup that you can use to orchestrate
a master build.

### Project Root Tests

This will run all of the tests configured for each project.  A corresponding build/reports/tests/test/index.html
is produced under each subsystem that has tests.

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
