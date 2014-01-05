
"%JAVA_HOME%\bin\jarsigner" -keystore E:\javaxyq.key JavaXYQ.jar javaxyq <pwd.txt
rem "%JAVA_HOME%\bin\jarsigner" -keystore E:\javaxyq.key lib\groovy-all-1.6.5.jar javaxyq  <pwd.txt
rem "%JAVA_HOME%\bin\jarsigner" -keystore E:\javaxyq.key lib\jl1.0.jar javaxyq  <pwd.txt
copy JavaXYQ.jar ..\..\javaxyq-app\war\javaxyq
@pause