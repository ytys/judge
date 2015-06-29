@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION
TITLE %~0
%~d0
CD %~p0
IF "%~1" == "" GOTO noarg

:start
IF "%~x1" == ".js" GOTO js
ECHO File '%~1' is not a js file.
PAUSE
GOTO :next

:js
PUSHD %~dp1
CALL uglifyjs -cm --comments "/license|warranty/i" --source-map "%~n1.min.map" -o "%~n1.min%~x1" -- "%~nx1"
POPD
:next
SHIFT
IF "%~1" == "" GOTO :EOF
GOTO start

:noarg
SET file=
SET /p file="%cd%>"
IF NOT DEFINED file GOTO noarg
IF "!file!" == "" GOTO :noarg
IF EXIST "!file!" CALL :start !file!
GOTO :noarg
PAUSE
