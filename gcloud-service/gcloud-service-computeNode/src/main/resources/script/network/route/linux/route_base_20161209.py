#!/usr/bin/python
import subprocess
import re
import os
import platform
interface_info_list=[]



#exchange_mask = lambda mask: sum(_bin(int(i)).count('1') \
                               #  for i in mask.split('.'))



class RouteBase:

    def __init__(self):
        pass

    def _bin(self,x):
        """
        bin(number) -> string

        Stringifies an int or long in base 2.
        """
        if x < 0:
            return '-' + self._bin(-x)
        out = []
        if x == 0:
            out.append('0')
        while x > 0:
            out.append('01'[x & 1])
            x >>= 1
            pass
        try:
            return '0b' + ''.join(reversed(out))
        except NameError, ne2:
            out.reverse()
        return '0b' + ''.join(out)


    def exchange_mask(self,mask):
        if int(platform.python_version().split('.')[1])<6:
             return sum(self._bin(int(i)).count('1') \
                                  for i in mask.split('.'))
        else:
             return sum(bin(int(i)).count('1') \
                                  for i in mask.split('.'))

    def check_ip(self,interface_name):
        grep_str="route -n | grep %s | grep -v grep" %(interface_name)
        subp=subprocess.Popen(grep_str, shell=True,stdout=subprocess.PIPE)
        line=subp.stdout.readline()
        if line :
            return True
        else:
            return False

    def _get_mac_netcards(self,interface_name):
        file_name = '/sys/class/net/%s/address' %(interface_name)
        file_opt=open(file_name,'r')
        f=file_opt.read()
        mac_add=None
        s=f.split('\n')
        lines=len(s)
        mac_add=s[0]
        print mac_add
        file_opt.close()
        return mac_add


    def get_gateway_netcard(self,interface_name):
        mac_add=self._get_mac_netcard(interface_name)
        file_name='/usr/share/gcloud/gcgateway'
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
            if mac_add in s[i]:
            #print s[i]
                gw=s[i].split()[1]
                break
        return gw
    # def _get_dhcp_file(self,interface_name):
    #     file_name = '/var/lib/dhcp/dhclient.%s.leases' % interface_name
    #     print "dhcp file %s"%(file_name)
    #     return file_name

    def _get_dhcp_file(self,interface_name):
        grep_str="ps aux | grep dhclient | grep %s | grep -v grep" %(interface_name)
        subp=subprocess.Popen(grep_str, shell=True,stdout=subprocess.PIPE)
        line=subp.stdout.readline()
        if line:
           return line[line.index("-lf")+4:(line.index(".lease")+7)].strip( )

        return None

    def get_gateway_dhcp(self,interface_name):
        file_name=self._get_dhcp_file(interface_name)
        if file_name is None:
            print "dhcp process not exist"
            return None
        if os.path.exists(file_name)==False:
            print "dhcp file %s is not existed" %(file_name)
            return None
        file_opt=open(file_name,'r')
        f=file_opt.read()
        s=f.split('\n')
        gw=None
        gw_str="option routers"
        lines=len(s)
        for i in range(0,lines)[::-1]:
            if gw_str in s[i]:
            #print s[i]
                gw_1=s[i].split()[2]
            #print "gw=%s" % gw_1
                gw = gw_1.split(";")[0]
                break
        #print 'gw=%s' % gw
        file_opt.close()
        return gw


    def get_network(self,ip_list, netmask_list, prefix):
        network=str()
        for k in range(4):
            network+=str(int(netmask_list[k])&int(ip_list[k]))
            if k!=3:
                network+="."
        network+=prefix
        return network

    def exec_command(self,command):
        print command
        os.system(command)

    def _check_rule(self,interface_name):
        grep_str="ip rule list | grep %s | grep -v grep" %(interface_name)
        subp=subprocess.Popen(grep_str, shell=True,stdout=subprocess.PIPE)
        line=subp.stdout.readline()
        if line>0:
            return True
        else:
            return  False
    def exchange_maskint(self,mask_int):
      bin_arr = ['0' for i in range(32)]
      for i in range(mask_int):
        bin_arr[i] = '1'
      tmpmask = [''.join(bin_arr[i * 8:i * 8 + 8]) for i in range(4)]
      tmpmask = [str(int(tmpstr, 2)) for tmpstr in tmpmask]
      return '.'.join(tmpmask)
    def get_network_by_ipinfo(self,ip_info):
         ip_list=ip_info["ip"].split(".")
         netmask_list=ip_info["netmask"].split(".")
         prefix="/"+str(self.exchange_mask(ip_info["netmask"]))
         return self.get_network(ip_list, netmask_list, prefix)

    def add_route_interfaceinfo(self,interface_info):
        #interface_info['gateway'],interface_info["name"]  interface_info['ip']
        #flush  #ip rule del  from 40.40.40.146  table eth0
        cmds="ip route flush table %s" %interface_info["name"]
        self.exec_command(cmds)

        #exec_command("ip rule del  from %s table %s" %(interface_info['ip'],interface_info["name"]))
        # add ip  route add default  via  40.40.40.1  dev eth0  table  eth0
        if interface_info['gateway']:
            self.exec_command("ip  route add default  via %s dev %s table %s"
                         %(interface_info['gateway'],interface_info["name"],interface_info["name"]))

        #ip  route add  40.40.40.0/24 dev eth0 src 40.40.40.146 table eth0
        #print 1
        cmds="ip route add %s dev %s src %s table %s"\
             %(interface_info['network'],interface_info["name"],interface_info['ip'],interface_info["name"])
        self.exec_command(cmds)

        is_exist=self._check_rule(interface_info["name"])
        if is_exist:
              cmds="ip rule del  table %s" %(interface_info["name"])
              self.exec_command(cmds)

        # ip rule add from 40.40.40.146  table eth0
        cmds="ip rule add from %s table %s" %(interface_info['ip'],interface_info["name"])
        self.exec_command(cmds)

        #if interface_info.get("mode") is not None and interface_info.get("mode")=="static":
            #add default route in interface
        cmds="route del default dev %s"%(interface_info["name"])
        self.exec_command(cmds)

        if interface_info['gateway']:
            cmds="route add default  gw  %s dev %s"\
                         %(interface_info['gateway'],interface_info["name"])
            self.exec_command(cmds)


    def add_all_interface_route(self):
        subp=subprocess.Popen("ip addr show", shell=True,stdout=subprocess.PIPE)
        line=subp.stdout.readline()
        while line:
            interface=re.findall("eth[0-9]{1,3}", line)
            ##print "line"+line
            if len(interface)==0:
                 line=subp.stdout.readline()
                 continue
            else:
                #print interface
                interface_info={"name": interface[0]}
                #print interface[0]
                if  line.find("UP")==-1:
                     line=subp.stdout.readline()
                     continue
                interface_info['gateway']=self.get_gateway_dhcp(interface[0])
                if interface_info['gateway']==None:
                     interface_info['gateway']=self.get_gateway_netcard(interface[0])
                     interface_info['mode']="static"

                nextline=subp.stdout.readline()#link ether
                nextline=subp.stdout.readline()#ip or eth1:up***
                if nextline is not None:
                    if nextline.find("inet")!=-1:
                        print nextline
                        ip_info=nextline[nextline.index("inet")+5:nextline.index("brd")-1]
                        network=ip_info#20.251.225.225/24
                        interface_info['ip']=network.split('/')[0]
                        interface_info['netmask']=self.exchange_maskint(int(network.split('/')[1]))
                        interface_info['network']=self.get_network_by_ipinfo(interface_info)
                        interface_info_list.append(interface_info)
                        line=subp.stdout.readline()

        print interface_info_list
        for  interface_info in interface_info_list:
              self.add_route_interfaceinfo(interface_info)

    # def add_all_interface_route(self):
    #     subp=subprocess.Popen("ifconfig", shell=True,stdout=subprocess.PIPE)
    #     line=subp.stdout.readline()
    #     while line:
    #         interface=re.findall("eth[0-9]{1,3}", line)
    #         if interface:
    #             interface_info={"name": interface[0]}
    #             interface_info['gateway']=self.get_gateway_dhcp(interface[0])
    #             if interface_info['gateway']==None:
    #                  interface_info['gateway']=self.get_gateway_netcard(interface[0])
    #                  interface_info['mode']="static"
    #             ip_info=subp.stdout.readline()
    #             list=ip_info.split()
    #             interface_info['ip'] = list[1].split(":")[1]
    #             prefix=str(self.exchange_mask(list[3].split(":")[1]))
    #             network=self.get_network(list[1].split(":")[1].split("."),list[3].split(":")[1].split("."),"/"+prefix)
    #             interface_info['network']=network
    #             interface_info_list.append(interface_info)
    #         line=subp.stdout.readline()
    #     print interface_info_list
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

    def del_interface_route(self,interface_name):
        is_exist=self._check_rule(interface_name)
        if is_exist:
              self.exec_command("ip rule del  table %s" %(interface_name))
        self.exec_command("ip route flush table %s" %(interface_name))
