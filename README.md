# osgi-reflection-test
The project has been created to reproduce CPU spikes with felix framework when using reflection in the OSGi bundle.

For more details refer to https://issues.apache.org/jira/browse/FELIX-5665.

## Build
You need gradle to build the project.

* Build command

`gradle clean build`

When the build is successful, the OSGi bundle is available under reflection/buildDirectory/libs/ as `reflection-1.0-SNAPSHOT.jar` which can be installed on Apache Karaf.

## Deploying
You can install this bundle by placing it in $KARAF_HOME/deploy/. This should also start it automatically when Karaf boots up.

* Setting up jvisualvm

Add the following lines to `$KARAF_HOME/bin/setenv` before starting Karaf:
```shell
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=19000"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
```

Now you should be able to launch jvisualvm and connect to JVM instance on localhost on port 19000

## Logging
The default log file is `/var/log/reflection-test.log`
You can change the name/path here: `reflection/src/main/resources/log4j2.xml`

## Running load test
* Start

`reflection/src/main/resources/load_start.sh <Concurrency>`

where Concurrency = Number of parallel requests to execute, Ex: 100

* Stop

`reflection/src/main/resources/load_stop.sh`
