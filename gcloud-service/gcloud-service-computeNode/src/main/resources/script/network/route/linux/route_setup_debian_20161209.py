#!/usr/bin/python
import route_base
import subprocess
class Debian(route_base.RouteBase):
    def __init__(self):
        pass
    # def _get_dhcp_file(self,interface_name):
    #     grep_str="ps aux | grep dhclient  | grep %s | grep -v grep" %(interface_name)
    #     subp=subprocess.Popen(grep_str, shell=True,stdout=subprocess.PIPE)
    #     line=subp.stdout.readline()
    #     while line:
    #        return line[line.index("-lf")+4:(line.index("-cf")-1)]
    #        break
    #     return None
#if __name__=='__main__':
#    debianOpertate=Debian()
#    debianOpertate.add_all_interface_route()
    #print debianOpertate._get_dhcp_interface("eth2")
    #print debianOpertate.get_gateway_dhcp("eth2")