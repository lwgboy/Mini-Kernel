@echo off
setlocal enabledelayedexpansion


set logPath="c:/Program Files/qemu-ga/setnulllog.txt"

echo "set null ip begin" > %logPath% 2>&1
echo %date% %time% >> %logPath% 2>&1
echo param1="%1" >> %logPath% 2>&1

if %1 == "" (
   exit /b 198
)

set deleteMac=%1

echo %deleteMac%

set macSp=:
set macLen=0
set objLen=0
set lastN=0
set n=0
:maclenloop
set u=!deleteMac:~%n%,1!

if not "!u!" == "" (
  
  if "!u!" == "!macSp!" (
     set /a subLen=%n%-%lastN%
     set /a lastN=%n%+1
     set obj[%objLen%]=!subLen!
     set /a objLen=%objLen%+1
  )
  
  set/a n+=1
  goto maclenloop
) else (
  set macLen=%n%
) 

set k=0
set lastIdx=0

:delmacloop

set sLen=!obj[%k%]!
set /a lastLen=%macLen%-%lastIdx%
set /a netIdx=%k%+1

if %k% GTR %objLen% (
 
 goto delmacloopend

) 

if %k% == %objLen% (
  set tMac=!deleteMac:~%lastIdx%,%lastLen%!
) else (
  set tMac=!deleteMac:~%lastIdx%,%sLen%!
  set /a lastIdx=%lastIdx%+%sLen%+1
)
echo !tMac! >> %logPath% 2>&1
getmac /v /nh /fo csv >> %logPath% 2>&1
for /f "delims=," %%i in ('getmac /v /nh /fo csv ^| findstr !tMac!') do (

   echo "netsh interface ip set address name=%%i source=dhcp" >> %logPath% 2>&1
   netsh interface ip set address name=%%i source=dhcp

   if errorlevel 1 (
     echo "error"
     exit /b 121
   ) else (
     echo "ip succ"  
   )

   echo "netsh interface ip set dns name=%%i source=dhcp" >> %logPath% 2>&1
   netsh interface ip set dns name=%%i source=dhcp

   if errorlevel 1 (
     echo "error"
     exit /b 122
   ) else (
     echo "dns succ"
   )

) 

if %k% == %objLen% (
  goto delmacloopend
)

set /a k+=1

goto delmacloop

:delmacloopend