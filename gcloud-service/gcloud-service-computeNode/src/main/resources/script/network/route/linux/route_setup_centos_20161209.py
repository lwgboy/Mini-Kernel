#!/usr/bin/python
import route_base
import os
import subprocess
import re
import os
interface_info_list=[]

class Centos(route_base.RouteBase):
    def __init__(self):
        pass
    def get_gateway_netcard(self,interface_name):
            file_name='/etc/sysconfig/network-scripts/ifcfg-%s'%(interface_name)
            file_opt=None
            try:
                file_opt=open(file_name,'r')
                if os.path.exists(file_name)==False:
                    print "gc gateway file is not existed"
                    return None
                file_opt=open(file_name,'r')
                f=file_opt.read()
                s=f.split('\n')
                lines=len(s)
                gw=None
                for i in range(0,lines)[::-1]:
                    if "GATEWAY" in s[i]:
                    #print s[i]
                        gw=s[i].split("=")[1]
                        break
                return gw
            finally:
                if file_opt is not None:
                    file_opt.close()
            return None

    # def _get_dhcp_file(self,interface_name):
    #     #/var/lib/dhclient/dhclient-eth0.leases
    #     file_name = '/var/lib/dhclient/dhclient-%s.leases' % interface_name
    #     print "dhcp file %s"%(file_name)
    #     return file_name




    # def add_all_interface_route(self):
    #     subp=subprocess.Popen("ip addr show", shell=True,stdout=subprocess.PIPE)
    #     line=subp.stdout.readline()
    #     while line:
    #         interface=re.findall("eth[0-9]{1,3}", line)
    #         if interface:
    #             interface_info={"name": interface[0]}
    #             if  line.find("UP")==-1:
    #                  continue
    #             interface_info['gateway']=self.get_gateway_dhcp(interface[0])
    #             if interface_info['gateway']==None:
    #                  interface_info['gateway']=self.get_gateway_netcard(interface[0])
    #                  interface_info['mode']="static"
    #
    #             nextline=subp.stdout.readline()#link ether
    #             nextline=subp.stdout.readline()#ip or eth1:up***
    #             if nextline is not None:
    #                 if nextline.find("inet")!=-1:
    #                     ip_info=nextline[len("inet")+1:line.index("brd")-1]
    #                     # list=ip_info.split("/")
    #                     # interface_info['ip'] = list[1]
    #                     # prefix=str(self.exchange_mask(list[3]))
    #                     #network=self.get_network(list[1].split("."),list[3].split("."),"/"+prefix)
    #                     network=ip_info
    #                     interface_info['network']=network
    #                     interface_info_list.append(interface_info)
    #                     line=subp.stdout.readline()
    #     print interface_info_list
    #
    #     for  interface_info in interface_info_list:
    #         """
    #         #flush  #ip rule del  from 40.40.40.146  table eth0
    #         exec_command("ip route flush table %s" %interface_info["name"])
    #         #exec_command("ip rule del  from %s table %s" %(interface_info['ip'],interface_info["name"]))
    #         # add ip  route add default  via  40.40.40.1  dev eth0  table  eth0
    #         if interface_info['gateway']:
    #             exec_command("ip  route add default  via %s dev %s table %s"
    #                          %(interface_info['gateway'],interface_info["name"],interface_info["name"]))
    #
    #         #ip  route add  40.40.40.0/24 dev eth0 src 40.40.40.146 table eth0
    #         exec_command("ip route add %s dev %s src %s table %s"
    #                      %(interface_info['network'],interface_info["name"],interface_info['ip'],interface_info["name"]))
    #
    #         is_exist=_check_rule(interface_info["name"])
    #         if is_exist:
    #               exec_command("ip rule del  table %s" %(interface_info["name"]))
    #         # ip rule add from 40.40.40.146  table eth0
    #         exec_command("ip rule add from %s table %s" %(interface_info['ip'],interface_info["name"]))
    #         """
    #         self.add_route_interfaceinfo(interface_info)



