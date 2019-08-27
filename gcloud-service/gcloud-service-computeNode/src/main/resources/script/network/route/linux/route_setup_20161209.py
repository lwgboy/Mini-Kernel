import route_factory
import  sys
if __name__=='__main__':
    route_operate=route_factory.RouteFactory.create_router_instance()
    if route_operate is None:
        print "can not find router factory"
        sys.exit(1)
    route_operate.add_all_interface_route()
