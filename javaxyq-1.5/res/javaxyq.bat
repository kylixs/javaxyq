@echo off
set myclasspath=lib/javaxyq-common.jar;lib/JavaXYQ.jar;lib/jl1.0.jar;lib/derby.jar;lib/commons-dbutils-1.3.jar;lib/commons-jexl-2.0.1.jar;lib/groovy-all-1.6.5.jar;lib/commons-logging-1.1.1.jar;lib/commons-beanutils-core-1.8.2.jar

java -Xmx128m -classpath %myclasspath%  com.javaxyq.core.DesktopApplication
pause