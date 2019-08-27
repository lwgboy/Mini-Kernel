#!/usr/bin/python
import route_factory
import sys


if (len(sys.argv)!=5):
    print "input params:\n"
    print "interface name,ip, netmask,gateway"
    sys.exit(2)

routeOperate=route_factory.RouteFactory.create_router_instance()
interface_info={}
interface_info["name"]=sys.argv[1] #eth0
ip_info={}
interface_info["ip"]=ip_info['ip']   =sys.argv[2]
ip_info['netmask']= str(sys.argv[3])
if "None"==sys.argv[4]:
    interface_info["gateway"]=None
else:
    interface_info["gateway"]=ip_info['gateway']=sys.argv[4]
if routeOperate.check_ip(interface_info["name"]) is False:
    sys.exit(3)
interface_info["network"]=routeOperate.get_network_by_ipinfo(ip_info)
interface_info["mode"]="static"
routeOperate.add_route_interfaceinfo(interface_info)
sys.exit(0)
