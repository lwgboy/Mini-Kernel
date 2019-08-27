package com.gcloud.api;

import com.gcloud.api.region.Region;
import com.gcloud.api.region.Regions;
import com.gcloud.api.security.HttpRequestConstant;
import com.gcloud.header.compute.msg.api.model.DescribeRegionResponse;
import com.gcloud.header.compute.msg.api.model.RegionType;
import com.gcloud.header.compute.msg.api.region.ApiDescribeRegionReplyMsg;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RegionController {

    @Autowired
    private Regions regions;

    @RequestMapping(value = "/ecs/DescribeRegions.do")
    public ApiDescribeRegionReplyMsg describeRegions(HttpServletRequest request){

        List<RegionType> regionTypes = new ArrayList<>();

        if(regions.getRegions() != null){
            for(Map.Entry<String, Region> regionInfo : regions.getRegions().entrySet()){
                Region region = regionInfo.getValue();
                RegionType type = new RegionType();
                type.setRegionId(region.getId());
                regionTypes.add(type);
            }
        }

        DescribeRegionResponse response = new DescribeRegionResponse();
        response.setRegion(regionTypes);

        ApiDescribeRegionReplyMsg reply = new ApiDescribeRegionReplyMsg();
        reply.setRegions(response);

        reply.setRequestId(ObjectUtils.toString(request.getAttribute(HttpRequestConstant.ATTR_REQUEST_ID), null));

        return reply;
    }


}
