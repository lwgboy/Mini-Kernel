package com.gcloud.compute.util;

import com.gcloud.common.util.FileUtil;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComputeNetworkUtil {

    private static final String CHECK_NET_SH = "network/checkNet.sh";
    private static final String CHECK_OVS_SH = "network/checkOvs.sh";

    public static boolean isNetExist(String netName) {

        boolean isExist = false;
        ComputeNodeProp prop = SpringUtil.getBean(ComputeNodeProp.class);
        String scriptPath = prop.getConfigurePath();
        String script = FileUtil.getDirectoryString(scriptPath) + CHECK_NET_SH;

        try {

            String[] cmd = new String[] { script, netName };
            int ret = SystemUtil.runAndGetCode(cmd);
            isExist = ret == 0;

        } catch (Exception e) {
            log.error("运行检查网络是否存在脚本失败", e);
            isExist = false;
        }

        return isExist;

    }

    public static boolean isOvsExist(String ovsName) {

        boolean isExist = false;
        ComputeNodeProp prop = SpringUtil.getBean(ComputeNodeProp.class);
        String scriptPath = prop.getConfigurePath();
        String script = FileUtil.getDirectoryString(scriptPath) + CHECK_OVS_SH;

        try {

            int ret = 0;
            String[] cmd = new String[] { script, ovsName };
            ret = SystemUtil.runAndGetCode(cmd);
            isExist = ret == 0;

        } catch (Exception e) {
            log.error("运行检查ovs是否存在脚本失败", e);
            isExist = false;
        }

        return isExist;

    }

}
