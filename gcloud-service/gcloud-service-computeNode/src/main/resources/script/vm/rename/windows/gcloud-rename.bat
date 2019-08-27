@echo off&color 2E&MODE con: COLS=60 LINES=15&title rename(czar8)
set number=%1
echo REGEDIT4 >c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\ComputerName] >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\ComputerName\ComputerName] >> c:\windows\reg.reg
echo "ComputerName"="%number%" >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\ComputerName\ActiveComputerName] >> c:\windows\reg.reg
echo "ComputerName"="%number%" >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\ControlSet002\Control\ComputerName\ComputerName] >> c:\windows\reg.reg
echo "ComputerName"="%number%" >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Tcpip\Parameters] >> c:\windows\reg.reg
echo "NV Hostname"="%number%" >> c:\windows\reg.reg
echo "Hostname"="%number%" >> c:\windows\reg.reg
echo [HKEY_USERS\S-1-5-18\Software\Microsoft\Windows\ShellNoRoam] >> c:\windows\reg.reg
echo @="A%number%" >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\Control\ComputerName\ActiveComputerName] >> c:\windows\reg.reg
echo "ComputerName"="%number%" >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\Services\Tcpip\Parameters] >> c:\windows\reg.reg
echo "NV Hostname"="%number%" >> c:\windows\reg.reg
echo "Hostname"="%number%" >> c:\windows\reg.reg
echo [HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Winlogon] >> c:\windows\reg.reg
echo "DefaultDomainName"="%number%" >> c:\windows\reg.reg
echo "AltDefaultDomainName"="%number%" >> c:\windows\reg.reg
regedit /s c:\windows\reg.reg