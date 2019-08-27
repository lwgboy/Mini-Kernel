@echo off
setlocal enabledelayedexpansion

rem ==================setip begin======================

set logPath="c:/Program Files/qemu-ga/setiplog.txt"

echo "set ip begin" > %logPath% 2>&1
echo %date% %time% >> %logPath% 2>&1
echo param1="%1",param2="%2",param3="%3",param4="%4",param5="%5" >> %logPath% 2>&1

:setipbegin

if "%1" == "" (
   exit /b 101
)

if "%2" == "" (
   exit /b 102
)

if "%3" == "" (
   exit /b 103
)

if "%4" == "" (
   exit /b 104
)

if "%5" == "" (
   exit /b 105
)

set targetMac=%1
set targetIp=%2
set targetGw=%3
  
set targetMask=%4
set targetDns=%5

if "%targetGw%" == "none" (
   set targetGw=
)

if "%targetDns%" == "none" (
   set targetDns=
)

set targetEth=""
set findNum=0

:findTargetEth

for /f "delims=," %%i in ('getmac /v /nh /fo csv ^| findstr "%targetMac%"') do (
 
 set targetEth=%%i

) 
set /a findNum=%findNum%+1

if %targetEth% == "" (
   if %findNum% GEQ 10 (
     echo "targetEth not found, exit" >> %logPath% 2>&1
     exit /b 110
   ) else (
     echo "targetEth not found, sleep, times=%findNum%" >> %logPath% 2>&1
     ping 127.0.0.1 -n 2 >nul
     goto findTargetEth
   )
)

echo "targetEth=%targetEth%" >> %logPath% 2>&1

if "%targetDns%" == "" (
  
  echo "gotodns"
  goto setipbegin
  
)

set dnsSp=:
set dnsLen=0
set objLen=0
set lastN=0
set n=0
:lenloop
set u=!targetDns:~%n%,1!

if not "!u!" == "" (
  
  if "!u!" == "!dnsSp!" (
     set /a subLen=%n%-%lastN%
     set /a lastN=%n%+1
     set obj[%objLen%]=!subLen!
     set /a objLen=%objLen%+1
  )
  
  set/a n+=1
  goto lenloop
) else (
  set dnsLen=%n%
) 


set k=0
set lastIdx=0

:setdnsloop

set sLen=!obj[%k%]!
set /a lastLen=%dnsLen%-%lastIdx%
set /a netIdx=%k%+1

if %k% GTR %objLen% (
 
 goto setdnsloopend

) 

rem 只有一个DNS

if %objLen% == 0 (
  
  set tDns=!targetDns:~%lastIdx%,%lastLen%!
  echo "netsh interface ip set dns name=%targetEth% static addr=!tDns! register=PRIMARY" >> %logPath% 2>&1
  netsh interface ip set dns name=%targetEth% static addr=!tDns! register=PRIMARY >> %logPath% 2>&1

) else (

  if %k% == 0 (
     set tDns=!targetDns:~%lastIdx%,%sLen%!
     set /a lastIdx=%lastIdx%+%sLen%+1
   
     echo "netsh interface ip set dns name=%targetEth% static addr=!tDns! register=PRIMARY" >> %logPath% 2>&1
     netsh interface ip set dns name=%targetEth% static addr=!tDns! register=PRIMARY >> %logPath% 2>&1
   
  ) else (

     if %k% == %objLen% (
       set tDns=!targetDns:~%lastIdx%,%lastLen%!

       echo "netsh interface ip add dns name=%targetEth% addr=!tDns! index=%netIdx%" >> %logPath% 2>&1
       netsh interface ip add dns name=%targetEth% addr=!tDns! index=%netIdx% >> %logPath% 2>&1

     ) else (

       set tDns=!targetDns:~%lastIdx%,%sLen%!
       set /a lastIdx=%lastIdx%+%sLen%+1

       echo "netsh interface ip add dns name=%targetEth% addr=!tDns! index=%netIdx%" >> %logPath% 2>&1
       netsh interface ip add dns name=%targetEth% addr=!tDns! index=%netIdx% >> %logPath% 2>&1


     )

  )


)

if errorlevel 1 (
   echo "set dns error, errorlever=!errorlevel!" >> %logPath% 2>&1
   exit /b 121
) else (
   echo "set dns succ, errorlever=!errorlevel!" >> %logPath% 2>&1
)

set /a k+=1

goto setdnsloop

:setdnsloopend

:setipbegin

set setIpTime=0

:setiploopbegin

set /a setIpTime=%setIpTime%+1

if "%targetGw%" == "" (
   
   echo "gw is none" >> %logPath% 2>&1
   echo "netsh interface ip set address %targetEth% static %targetIp% %targetMask% none" >> %logPath% 2>&1
   netsh interface ip set address %targetEth% static %targetIp% %targetMask% none >> %logPath% 2>&1

) else (

   echo "netsh interface ip set address %targetEth%  static %targetIp%  %targetMask% %targetGw% 1" >> %logPath% 2>&1
   netsh interface ip set address %targetEth%  static %targetIp%  %targetMask% %targetGw% 1 >> %logPath% 2>&1
  
)

rem try again

if errorlevel 1 ( 
   if %setIpTime% GEQ 3 (
     echo "set ip error, errorlever=!errorlevel!, exit" >> %logPath% 2>&1
     exit /b 124
   ) else (
     echo "set ip error, errorlever=!errorlevel!, setIpTime=%setIpTime%" >> %logPath% 2>&1
     ping 127.0.0.1 -n 2 >nul
     goto setiploopbegin
   )
) else (
   echo "set ip succ, errorlever=!errorlevel!" >> %logPath% 2>&1
)

   


