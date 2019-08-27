set readWrite=%1
set write=%2
reg add "HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\RemovableStorageDevices" /v Deny_All /t reg_dword /d %readWrite% /f 

reg add "HKEY_LOCAL_MACHINE\SOFTWARE\Policies\Microsoft\Windows\RemovableStorageDevices\{53f5630d-b6bf-11d0-94f2-00a0c91efb8b}" /v Deny_Write /t reg_dword /d %write% /f 
