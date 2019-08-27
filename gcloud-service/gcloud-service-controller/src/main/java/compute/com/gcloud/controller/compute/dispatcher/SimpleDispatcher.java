package com.gcloud.controller.compute.dispatcher;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by yaowj on 2019/1/7.
 * 每次都选第一个节点，第一个节点不满足选下一个
 */
@Component
@Slf4j
public class SimpleDispatcher extends Dispatcher {


    public Node assignNode(Integer core, Integer memory){

        Map<String, Node> nodes = RedisNodesUtil.getComputeNodes();
        if(nodes == null || nodes.size() == 0){
            throw new GCloudException("::没有合适的节点");
        }

        List<String> hostnames = new ArrayList<>();
        hostnames.addAll(nodes.keySet());
        Collections.sort(hostnames);

        Node node = null;
        for(String host : hostnames){

            boolean succ = occupyResource(core, memory, host);
            if(succ){
                node = nodes.get(host);
                break;
            }

        }

        return node;

    }

    @Override
    public Node assignNodeInZone(String zoneId, Integer core, Integer memory) {
        Map<String, Node> nodes = RedisNodesUtil.getComputeNodes();
        if (nodes == null) {
            return null;
        }
        for (Node node : nodes.values()) {
            if (StringUtils.equals(zoneId, node.getZoneId())) {
                if (occupyResource(core, memory, node.getHostName())) {
                    return node;
                }
            }
        }
        return null;
    }

    public void assignNode(String hostname, Integer core, Integer memory){
        allocateCompute(core, memory, hostname);
    }


}
