@echo off
setlocal & pushd
set APP_ENTRY=com.act.admin.AppEntry
set BASE=%~dp0
set CP=%BASE%\classes;%BASE%\lib\*
title Running act_admin-1.0.0 powered by actframework-1.8.7
javaw -server -Xms256M -Xmx2G -XX:MaxPermSize=128M  -Dapp.mode=prod -Dprofile=%PROFILE% -cp "%CP%" %APP_ENTRY%
endlocal & popd
