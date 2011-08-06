Please note that as of June 5, 2011 the wicketstuff-merged-resources JAR is unavailable in the maven
central repository. This JAR is needed for compiling fiftyfive-wicket itself, and by default it is
also required for projects created with the fiftyfive-wicket-archetype.

The necessary JAR has been included in this lib directory so that this project can build
successfully. Its inclusion is licensed under Apache Software License, Version 2.0. Copyright 2010
Stefan Fußenegger.
(http://wicketstuff.org/confluence/display/STUFFWIKI/wicketstuff-merged-resources)

This missing dependency affects fiftyfive-wicket 2.x, but it will not affect 3.x when it is
released.

INSTALLATION

From within this "lib" directory, run the following shell command:

  sh install.sh

This will install the wicketstuff-merged-resources-3.0.jar into your local maven repository.
