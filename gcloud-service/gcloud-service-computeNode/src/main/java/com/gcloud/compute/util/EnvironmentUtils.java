package com.gcloud.compute.util;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;

import lombok.extern.slf4j.Slf4j;

/*
 * @Description 获取环境参数
 */
@Slf4j
public class EnvironmentUtils {

    /**
     * @param errorCode
     * @throws GCloudException
     * @Title: checkCephFileSystem
     * @Description: 检查是否挂上ceph文件系统
     * @date 2015-6-29 下午3:24:39
     */
    public static void checkCephFileSystem() throws GCloudException {
        ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean("computeNodeProp");
        if (computeNodeProp.isCephFileSystem()) {
            int rc = SystemUtil.runAndGetCode(new String[]{computeNodeProp.getConfigurePath() + "/environment/checkCephFileSystem.sh", computeNodeProp.getCephFileSystemPath()});
            if (rc != 0) {
                log.error("checkCephFileSystem error: ceph file system not connected!  path:" + computeNodeProp.getCephFileSystemPath() + "       return=" + rc);
                throw new GCloudException("1010501::ceph file system is unmount or error");
            }
        }
    }

    public static String getKernelVersion() {
        String cmd[] = new String[]{"uname", "-r"};
        String res = SystemUtil.run(cmd);
        return res.replace("\n", "");
    }

    //获取 cpu信息
    public static String getCpuInfo() {
        ComputeNodeProp prop = SpringUtil.getBean(ComputeNodeProp.class);
        String cmd[] = new String[]{prop.getConfigurePath() + "/environment/getCpuInfo.sh"};
        return SystemUtil.run(cmd);
    }

    public static Integer getPhysicalCpu() {
        ComputeNodeProp prop = SpringUtil.getBean(ComputeNodeProp.class);
        String cmd[] = new String[]{prop.getConfigurePath() + "/environment/getPhysicalCpuNum.sh"};

        Integer num = null;
        try{
            num = Integer.valueOf(SystemUtil.run(cmd).trim());
        }catch (Exception ex){
            log.error("getPhysicalCpu error", ex);
        }

        return num;
    }

    private static double hmbForStr(String sizeStr) {
        if ("0".equals(sizeStr)) {
            return 0;
        }

        String s = sizeStr.substring(sizeStr.length() - 1, sizeStr.length());
        if (s.equalsIgnoreCase("m")) {
            return Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 1)) / 1024d;
        } else if (s.equalsIgnoreCase("g")) {
            return Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 1));
        } else if (s.equalsIgnoreCase("t")) {
            return Double.parseDouble(sizeStr.substring(0, sizeStr.length() - 1)) * 1024d;
        } else {
            return 0;
        }
    }

}
