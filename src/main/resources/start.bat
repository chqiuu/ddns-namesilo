@echo off
echo set app name
set appName=ddns-namesilo
set appVersion=1.0.0
echo set jarFile 
title %appName%-%appVersion%-%date%-%time%-%cd%
set jarFile=%appName%-%appVersion%.jar
echo find %appName% to kill before start process.
for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %appName%`) do (
	set pid=%%a
	set image_name=%%b
)
if not defined pid (echo process %appName% does not exists) else (
	echo prepare to kill %image_name%
	echo start kill %pid% ...
	taskkill /f /pid %pid%
)

echo start java -jar %jarFile%
java -Duser.timezone=GMT+08 -jar %jarFile%
for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr %jarFile%`) do (
	set pid=%%a
	set image_name=%%b
	echo %%a >%appName%.pid
)
exit
