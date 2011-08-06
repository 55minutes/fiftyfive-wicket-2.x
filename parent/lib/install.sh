#!/bin/sh

mvn install:install-file \
    -Dfile=wicketstuff-merged-resources-3.0.jar \
    -DgroupId=org.wicketstuff \
    -DartifactId=wicketstuff-merged-resources \
    -Dversion=3.0 \
    -Dpackaging=jar \
    -DgeneratePom=true
