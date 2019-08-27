#!/usr/bin/python
import os
import route_setup_ubuntu
import route_setup_debian
import route_setup_centos

CENTOS="centos"
UBUNTU="ubuntu"
DEBIAN="debian"
versions=[CENTOS,UBUNTU,DEBIAN]

def get_system_version():
     file_name = '/etc/redhat-release'
     if os.path.exists(file_name)==False:
         file_name='/etc/issue'
         if os.path.exists(file_name)==False:
             return None
     file_opt=None
     try:
          file_opt=open(file_name,'r')
          f=file_opt.read()
          s=f.split('\n')
          str=s[0]
          for var in versions:
             print var
             print str
             if str.lower().find(var)!=-1:
                 return var
     finally:
            if file_opt is not None:
                 file_opt.close()
     return None

class RouteFactory:
     @staticmethod
     def create_router_instance():
         version=get_system_version()
         if version ==CENTOS:
             return route_setup_centos.Centos()
         elif version == UBUNTU:
             return route_setup_ubuntu.Ubuntu()
         elif version == DEBIAN:
             return route_setup_debian.Debian()
         else:
             return None
