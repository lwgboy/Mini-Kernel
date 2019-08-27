#!/usr/bin/python
import route_factory
import sys
if len(sys.argv)!=2:
    print "input param: interfance name"
    sys.exit(1)
route_operate=route_factory.RouteFactory.create_router_instance()
if route_operate is None:
        print "can not find router factory"
        sys.exit(1)
route_operate.del_interface_route(sys.argv[1])
sys.exit(0)