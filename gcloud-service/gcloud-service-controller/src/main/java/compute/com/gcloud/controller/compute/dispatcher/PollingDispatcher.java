package com.gcloud.controller.compute.dispatcher;

import com.gcloud.controller.compute.model.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2019/1/7.
 */
//@Component
@Slf4j
public class PollingDispatcher extends Dispatcher {


    @Override
    public Node assignNode(Integer core, Integer memory) {
        return null;
    }

    @Override
    public Node assignNodeInZone(String zoneId, Integer core, Integer memory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void assignNode(String hostname, Integer core, Integer memory) {

    }
}
