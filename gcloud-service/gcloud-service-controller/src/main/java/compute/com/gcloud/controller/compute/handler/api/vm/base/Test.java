package com.gcloud.controller.compute.handler.api.vm.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.controller.compute.handler.api.model.DescribeInstancesParams;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.compute.msg.api.vm.base.ApiDescribeInstancesMsg;

public class Test {

    public static void main(String[] args) {
        ApiDescribeInstancesMsg msg = new ApiDescribeInstancesMsg();
//        msg.setPageNumber(2);
//
//        CurrentUser currentUser = new CurrentUser();
//        currentUser.setUserTenants(Arrays.asList("a", "b"));
//        msg.setCurrentUser(currentUser);

        DescribeInstancesParams params = BeanUtil.copyProperties(msg, DescribeInstancesParams.class);

        System.out.println(JSON.toJSONString(params, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
    }
}
