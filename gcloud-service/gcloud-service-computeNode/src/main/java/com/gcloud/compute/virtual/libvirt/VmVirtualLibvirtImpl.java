package com.gcloud.compute.virtual.libvirt;

import com.gcloud.common.util.Base64Util;
import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.compute.lock.VmStartLock;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.util.ParamUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.compute.enums.FsfreezeType;
import com.gcloud.header.compute.enums.FtState;
import com.gcloud.header.compute.enums.PlatformType;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.enums.VmStateLibvirt;
import com.gcloud.service.common.compute.model.DomainBlk;
import com.gcloud.service.common.compute.model.DomainDetail;
import com.gcloud.service.common.compute.model.DomainDisk;
import com.gcloud.service.common.compute.model.DomainInterface;
import com.gcloud.service.common.compute.model.DomainListInfo;
import com.gcloud.service.common.compute.model.FtInfo;
import com.gcloud.service.common.compute.model.NodeInfo;
import com.gcloud.service.common.compute.uitls.VmUtil;
import com.gcloud.service.common.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yaowj on 2018/9/7.
 */
@Service
@Primary
@Slf4j
public class VmVirtualLibvirtImpl implements IVmVirtual {
	
	@Autowired
	private ComputeNodeProp computeNodeProp;
	
    @Override
    public boolean agentGuestInfo(String instanceId) {
        try {
            int ret = 0;
            String[] cmd = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-info\"}'", computeNodeProp.getNodeIp(), instanceId)};
            ret = SystemUtil.runAndGetCode(cmd);
            LogUtil.handleLog(cmd, ret, "::agentGuestInfo error");
            return ret == 0;
        } catch (GCloudException e) {
            log.error("::agentGuestInfo error", e);
            return false;
        }
    }

    @Override
    public boolean agentGuestExec(String instanceId, String[] cmd, int timeout) {
        try {
            int exitcode = agentGuestExecCode(instanceId, cmd, timeout);
            return exitcode == 0;
        } catch (Exception e) {
            log.error("命令执行失败", e);
            return false;
        }
    }

    @Override
    public int agentGuestExecCode(String instanceId, String[] cmd, int timeout) {
        String res1 = "";
        String[] command1 = null;
        if (cmd.length == 0) {
            return -1;
        } else if (cmd.length == 1) {
            command1 = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-exec\", \"arguments\": {\"path\":\"%s\"}}'", computeNodeProp.getNodeIp(), instanceId, cmd[0])};
        } else {
            String arg = "";
            for (int i = 1; i < cmd.length; i++) {
                arg += "\"" + cmd[i] + "\"";
                if (i != (cmd.length - 1)) {
                    arg += ",";
                }
            }
            command1 = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-exec\", \"arguments\": {\"path\":\"%s\", \"arg\":[%s]}}'", computeNodeProp.getNodeIp(), instanceId, cmd[0], arg)};
        }
        res1 = SystemUtil.run(command1);

        String commandStr1 = printCommand(command1, res1);
        try {
            Map<String, Object> map1 = ParamUtil.stringToMap(res1);
            Map<String, Object> ret1 = (Map<String, Object>) map1.get("return");
            Integer pid = (Integer) ret1.get("pid");

            String[] command2 = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-exec-status\", \"arguments\": {\"pid\":%s}}'", computeNodeProp.getNodeIp(), instanceId, pid)};
            String res2 = SystemUtil.run(command2);
            printCommand(command2, res2);

            Map<String, Object> map2 = ParamUtil.stringToMap(res2);
            Map<String, Object> ret2 = (Map<String, Object>) map2.get("return");

            boolean exited = ParamUtil.getSingleBoolean("exited", ret2);
            long startTime = System.currentTimeMillis();
            boolean isTimeout = false;
            while (!exited && !isTimeout) {
                Thread.sleep(1 * 1000L);

                res2 = SystemUtil.run(command2);
                printCommand(command2, res2);
                map2 = ParamUtil.stringToMap(res2);
                ret2 = (Map<String, Object>) map2.get("return");

                exited = ParamUtil.getSingleBoolean("exited", ret2);
                isTimeout = System.currentTimeMillis() - startTime > timeout * 1000L;
            }

            if (exited) {
                int exitcode = Integer.parseInt(String.valueOf(ret2.get("exitcode")));// exited
                if (exitcode != 0) {
                    log.error("command:" + commandStr1 + "--exitcode:" + exitcode);
                }
                return exitcode;
            } else {
                log.error("command:" + commandStr1 + "--check is exited timeout");
                throw new GCloudException("check is exited timeout");
            }
        } catch (Exception e) {
            log.error("::exist code error", e);
            return -1;
        }
    }

    @Override
    public String agentGuestExecReturn(String instanceId, String[] cmd, int timeout, PlatformType type) {
        String res1 = "";
        String[] command1 = null;
        String tempReturnFile = null;
        if (type == PlatformType.LINUX) {
            tempReturnFile = String.format("/home/gcloud_%s", UUID.randomUUID().toString());
        } else if (type == PlatformType.WINDOWS) {
            tempReturnFile = String.format("c:/Windows/gcloud_%s", UUID.randomUUID().toString());
        }

        if (cmd.length == 0) {
            return null;
        } else {
            String arg = "";
            for (int i = 0; i < cmd.length; i++) {
                arg += cmd[i] + " ";
            }
            arg += "> " + tempReturnFile;

            if (type == PlatformType.LINUX) {
                command1 = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-exec\", \"arguments\": {\"path\":\"bash\", \"arg\":[\"-c\", \"%s\"]}}'", computeNodeProp.getNodeIp(), instanceId, arg)};
            } else if (type == PlatformType.WINDOWS) {
                command1 = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-exec\", \"arguments\": {\"path\":\"cmd.exe\", \"arg\":[\"/c\", \"%s\"]}}'", computeNodeProp.getNodeIp(), instanceId, arg)};
            }
        }
        res1 = SystemUtil.run(command1);

        String commandStr1 = printCommand(command1, res1);

        try {
            Map<String, Object> map1 = ParamUtil.stringToMap(res1);
            Map<String, Object> ret1 = (Map<String, Object>) map1.get("return");
            Integer pid = (Integer) ret1.get("pid");

            String[] command2 = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-exec-status\", \"arguments\": {\"pid\":%s}}'", computeNodeProp.getNodeIp(), instanceId, pid)};
            String res2 = SystemUtil.run(command2);
            printCommand(command2, res2);

            Map<String, Object> map2 = ParamUtil.stringToMap(res2);
            Map<String, Object> ret2 = (Map<String, Object>) map2.get("return");

            boolean exited = ParamUtil.getSingleBoolean("exited", ret2);
            int i = 0;
            while (!exited && i < timeout) {
                Thread.sleep(1 * 1000L);

                res2 = SystemUtil.run(command2);
                printCommand(command2, res2);
                map2 = ParamUtil.stringToMap(res2);
                ret2 = (Map<String, Object>) map2.get("return");

                exited = ParamUtil.getSingleBoolean("exited", ret2);
                i++;
            }

            if (exited) {
                int exitcode = Integer.parseInt(String.valueOf(ret2.get("exitcode")));// exited
                if (exitcode != 0) {
                    log.error("command:" + commandStr1 + "--exitcode:" + exitcode);
                }

                String res = agentGuestReadFile(instanceId, tempReturnFile);

                if (StringUtils.isNotBlank(res) && type == PlatformType.WINDOWS) {
                    if (res.endsWith("\r\n")) {
                        res = res.substring(0, res.length() - 2);
                    }
                }

                // 删除文件
                String[] commandDelete = new String[]{"rm", "-rf", tempReturnFile};
                agentGuestExec(instanceId, commandDelete, timeout);

                return res;
            } else {
                log.error("command:" + commandStr1 + "--check is exited timeout");
                throw new GCloudException("check is exited timeout");
            }
        } catch (Exception e) {
            log.error("::agentGuestExecReturn error", e);
            return null;
        }
    }

    @Override
    public Boolean agentGuestIsExistFile(String instanceId, String path) {
        int handle = -1;
        try {
            // 打开文件
            String[] commandOpen = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-open\", \"arguments\": {\"path\":\"%s\",\"mode\":\"r\"}}'", computeNodeProp.getNodeIp(), instanceId, path)};
            String resOpen = SystemUtil.run(commandOpen);
            printCommand(commandOpen, resOpen);
            Map<String, Object> mapOpen = ParamUtil.stringToMap(resOpen);
            handle = Integer.parseInt(String.valueOf(mapOpen.get("return")));// handle
        } catch (Exception e) {
            log.debug("path:" + path + " is not found");
            return false;
        } finally {
            try {
                if (handle != -1) {
                    // 关闭文件
                    String[] commandClose = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-close\", \"arguments\": {\"handle\":%s}}'", computeNodeProp.getNodeIp(), instanceId, handle)};
                    String resClose = SystemUtil.run(commandClose);
                    printCommand(commandClose, resClose);
                }
            } catch (Exception e) {
                log.debug("path:" + path + " close is error");
            }
        }
        return true;
    }

    @Override
    public String agentGuestReadFile(String instanceId, String tempReturnFile) {
        // 打开文件
        String[] commandOpen = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-open\", \"arguments\": {\"path\":\"%s\",\"mode\":\"r\"}}'", computeNodeProp.getNodeIp(), instanceId, tempReturnFile)};
        String resOpen = SystemUtil.run(commandOpen);
        printCommand(commandOpen, resOpen);
        Map<String, Object> mapOpen = ParamUtil.stringToMap(resOpen);
        int handle = Integer.parseInt(String.valueOf(mapOpen.get("return")));// handle

        // 读取文件
        String[] commandRead = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-read\", \"arguments\": {\"handle\":%s}}'", computeNodeProp.getNodeIp(), instanceId, handle)};
        String resRead = SystemUtil.run(commandRead);
        printCommand(commandRead, resRead);
        Map<String, Object> mapRead = ParamUtil.stringToMap(resRead);
        Map<String, Object> retRead = (Map<String, Object>) mapRead.get("return");

        // 关闭文件
        String[] commandClose = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-close\", \"arguments\": {\"handle\":%s}}'", computeNodeProp.getNodeIp(), instanceId, handle)};
        String resClose = SystemUtil.run(commandClose);
        printCommand(commandClose, resClose);
        Map<String, Object> mapClose = ParamUtil.stringToMap(resClose);
        Map<String, Object> retClose = (Map<String, Object>) mapClose.get("return");

        String res = ParamUtil.getSingleString("buf-b64", retRead);

        if (StringUtils.isNotBlank(res)) {
            return new String(Base64Util.decode(res));
        } else {
            return null;
        }
    }

    private String printCommand(String[] command, String res) {
        String command2String = "";
        for (String part : command) {
            command2String += part + " ";
        }
        log.debug(String.format("agentGuestExec  cmd:%s  result%s:", command2String, res));
        return command2String;
    }

    @Override
    public boolean agentGuestSetUserPassword(String instanceId, String username, String password, boolean crypted) {
        if (StringUtils.isBlank(password)) {
            throw new GCloudException("密码不能为空");
        }

        int ret = 0;
        String basePwd = Base64Util.encode(password.getBytes());
        String[] cmd = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-set-user-password\", \"arguments\": {\"username\":\"%s\", \"password\":\"%s\", \"crypted\":%s}}'", computeNodeProp.getNodeIp(), instanceId, username, basePwd, crypted)};
        ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::设置密码失败");
        return ret == 0;
    }

    @Override
    public boolean agentGuestSaveFile(String instanceId, String context, String path, String fileName, PlatformType type) {
        int handle = -1;
        try {
            // 创建目录
            String[] cmd = null;
            if (type == PlatformType.LINUX) {
                cmd = new String[]{"mkdir", "-p", path};
            } else if (type == PlatformType.WINDOWS) {
                cmd = new String[]{"cmd.exe", "/c", "md", path};
            }
            boolean isSucc = agentGuestExec(instanceId, cmd, 30);
            if (!isSucc) {
                log.error("目录创建失败，path" + path + "   instanceId:" + instanceId);
            } else {
                log.debug("目录创建成功，path" + path + "   instanceId:" + instanceId);
            }

            // 打开文件
            log.debug("打开文件开始，filePath" + path + fileName + "   instanceId:" + instanceId);
            String[] commandOpen = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-open\", \"arguments\": {\"path\":\"%s\",\"mode\":\"w+\"}}'", computeNodeProp.getNodeIp(), instanceId, path + fileName)};
            String resOpen = SystemUtil.run(commandOpen);
            printCommand(commandOpen, resOpen);
            Map<String, Object> mapOpen = ParamUtil.stringToMap(resOpen);
            handle = Integer.parseInt(String.valueOf(mapOpen.get("return")));// handle
            log.debug("打开文件成功，filePath" + path + fileName + "   instanceId:" + instanceId);

            if (StringUtils.isNotBlank(context)) {
                String base64Context = Base64Util.encode(context.getBytes());

                // 写入文件
                log.debug("写入文件开始，filePath" + path + fileName + "   instanceId:" + instanceId);
                String[] commandWrite = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-write\", \"arguments\": {\"handle\":%s, \"buf-b64\":\"%s\"}}'", computeNodeProp.getNodeIp(), instanceId, handle, base64Context)};
                String resWrite = SystemUtil.run(commandWrite);
                printCommand(commandWrite, resWrite);
                Map<String, Object> mapWrite = ParamUtil.stringToMap(resWrite);
                Map<String, Object> retWrite = (Map<String, Object>) mapWrite.get("return");
                Integer.parseInt(String.valueOf(retWrite.get("count")));// handle
                log.debug("写入文件成功，filePath" + path + fileName + "   instanceId:" + instanceId);
            }
            return true;
        } catch (Exception e) {
            log.error("写入文件出错", e);
            return false;
        } finally {
            if (handle != -1) {
                // 关闭文件
                log.debug("关闭文件开始，filePath" + path + fileName + "   instanceId:" + instanceId);
                String[] commandClose = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-close\", \"arguments\": {\"handle\":%s}}'", computeNodeProp.getNodeIp(), instanceId, handle)};
                String resClose = SystemUtil.run(commandClose);
                printCommand(commandClose, resClose);
                Map<String, Object> mapClose = ParamUtil.stringToMap(resClose);
                Map<String, Object> retClose = (Map<String, Object>) mapClose.get("return");
                log.debug("关闭文件成功，filePath" + path + fileName + "   instanceId:" + instanceId);
            }
        }
    }

    @Override
    public boolean agentGuestSaveFileForMultiString(String instanceId, List<String> contexts, String path, String fileName, PlatformType type) {
        int handle = -1;
        try {
            // 创建目录
            String[] cmd = null;
            if (type == PlatformType.LINUX) {
                cmd = new String[]{"mkdir", "-p", path};
            } else if (type == PlatformType.WINDOWS) {
                cmd = new String[]{"cmd.exe", "/c", "md", path};
            }
            boolean isSucc = agentGuestExec(instanceId, cmd, 30);
            if (!isSucc) {
                log.error("目录创建失败，path" + path + "   instanceId:" + instanceId);
            } else {
                log.debug("目录创建成功，path" + path + "   instanceId:" + instanceId);
            }

            // 打开文件
            log.debug("打开文件开始，filePath" + path + fileName + "   instanceId:" + instanceId);
            String[] commandOpen = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-open\", \"arguments\": {\"path\":\"%s\",\"mode\":\"w+\"}}'", computeNodeProp.getNodeIp(), instanceId, path + fileName)};
            String resOpen = SystemUtil.run(commandOpen);
            printCommand(commandOpen, resOpen);
            Map<String, Object> mapOpen = ParamUtil.stringToMap(resOpen);
            handle = Integer.parseInt(String.valueOf(mapOpen.get("return")));// handle
            log.debug("打开文件成功，filePath" + path + fileName + "   instanceId:" + instanceId);

            if (contexts != null && contexts.size() > 0) {
                for (String context : contexts) {
                    String base64Context = Base64Util.encode(context.getBytes());

                    // 写入文件
                    log.debug("写入文件开始，filePath" + path + fileName + "   instanceId:" + instanceId);
                    String[] commandWrite = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-write\", \"arguments\": {\"handle\":%s, \"buf-b64\":\"%s\"}}'", computeNodeProp.getNodeIp(), instanceId, handle, base64Context)};
                    String resWrite = SystemUtil.run(commandWrite);
                    printCommand(commandWrite, resWrite);
                    Map<String, Object> mapWrite = ParamUtil.stringToMap(resWrite);
                    Map<String, Object> retWrite = (Map<String, Object>) mapWrite.get("return");
                    Integer.parseInt(String.valueOf(retWrite.get("count")));// handle
                    log.debug("写入文件成功，filePath" + path + fileName + "   instanceId:" + instanceId);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("写入文件出错,", e);
            return false;
        } finally {
            if (handle != -1) {
                // 关闭文件
                log.debug("关闭文件开始，filePath" + path + fileName + "   instanceId:" + instanceId);
                String[] commandClose = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-file-close\", \"arguments\": {\"handle\":%s}}'", computeNodeProp.getNodeIp(), instanceId, handle)};
                String resClose = SystemUtil.run(commandClose);
                printCommand(commandClose, resClose);
                Map<String, Object> mapClose = ParamUtil.stringToMap(resClose);
                Map<String, Object> retClose = (Map<String, Object>) mapClose.get("return");
                log.debug("关闭文件成功，filePath" + path + fileName + "   instanceId:" + instanceId);
            }
        }
    }

    @Override
    public void start(String instanceId) {
        try {
            VmStartLock.addStartList(instanceId);
            VmStartLock.checkVmStartLock(instanceId);

            this._start(instanceId);

            // 避免启动风暴，启动成功之后sleep
            startSleep(instanceId);
        } catch (GCloudException e) {
            log.error("::开机失败", e);
            throw new GCloudException("::开机失败");
        } catch (InterruptedException e) {
            log.error("::开机线程等待失败", e);
            throw new GCloudException("::开机线程等待失败");
        } finally {
            removeStartInfo(instanceId);
        }
    }

    @Override
    public void startDesktop(String instanceId) {
        this._start(instanceId);
    }

    private void _start(String instanceId) {
        int ret = 0;
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "start", instanceId};
        ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::开机失败");
    }

    @Override
    public void start(String instanceId, String hostIp) {
        try {
            VmStartLock.addStartList(instanceId);
            VmStartLock.checkVmStartLock(instanceId);

            int ret = 0;
            String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + hostIp + "/system", "start", instanceId};
            ret = SystemUtil.runAndGetCode(cmd);
            LogUtil.handleLog(cmd, ret, "::开机失败");

            // 避免启动风暴，启动成功之后sleep
            startSleep(instanceId);
        } catch (GCloudException e) {
            log.error("::开机失败", e);
            throw new GCloudException("::开机失败");
        } catch (InterruptedException e) {
            log.error("::开机线程等待失败", e);
            throw new GCloudException("::开机线程等待失败");
        } finally {
            removeStartInfo(instanceId);
        }
    }

    @Override
    public void shutdown(String instanceId) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "shutdown", instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::关机失败");
    }

    @Override
    public void destroy(String instanceId) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "destroy", instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::destroy 失败");

    }

    @Override
    public void destroy(String instanceId, String hostIp) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + hostIp + "/system", "destroy", instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::远程destroy失败");
    }

    @Override
    public void reboot(String instanceId) {
        try {
            ComputeNodeProp computeNodeProp = SpringUtil.getBean(ComputeNodeProp.class);
            VmStartLock.addStartList(instanceId);
            VmStartLock.checkVmStartLock(instanceId);

            int ret = 0;
            String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "reboot", instanceId, "--mode", "acpi"};
            ret = SystemUtil.runAndGetCode(cmd);
            LogUtil.handleLog(cmd, ret, "::重启失败");

            // 避免启动风暴，启动成功之后sleep
            Thread.sleep(computeNodeProp.getVmStartSleepTime() * 1000L);
        } catch (GCloudException e) {
            log.error("::重启失败", e);
            throw new GCloudException("::重启失败");
        } catch (InterruptedException e) {
            log.error("::重启等待休眠失败", e);
            throw new GCloudException("::重启等待休眠失败");
        } finally {
            removeStartInfo(instanceId);
        }

    }

    public String domstate(String instanceId) {
        return domstate(instanceId, computeNodeProp.getNodeIp());
    }

    public String domstate(String instanceId, String targetIp) {
        String ret = SystemUtil.run(new String[]{"virsh", "-c", "qemu+tcp://" + targetIp + "/system", "domstate", instanceId});
        return ret.replace("\n\n", "");
    }

    public String queryStateForGcloud(String instanceId) {
        String ret = domstate(instanceId);
        return changeStateForGcloud(ret);

    }

    public String queryStateForGcloud(String instanceId, String targetIp) {
        String ret = domstate(instanceId, targetIp);
        return changeStateForGcloud(ret);

    }

    @Override
    public String changeStateForGcloud(String ret) {

        if (ret.equals(VmStateLibvirt.NOSTATE.getValue())) {
            return VmState.RUNNING.value();
        } else if (ret.equals(VmStateLibvirt.RUNNING.getValue())) {
            return VmState.RUNNING.value();
        } else if (ret.equals(VmStateLibvirt.BLOCKED.getValue())) {
            return VmState.RUNNING.value();
        } else if (ret.equals(VmStateLibvirt.PAUSED.getValue())) {
            return VmState.PAUSED.value();
        } else if (ret.equals(VmStateLibvirt.SHUTDOWN.getValue())) {
            return VmState.STOPPED.value();
        } else if (ret.equals(VmStateLibvirt.SHUTOFF.getValue())) {
            return VmState.STOPPED.value();
        } else if (ret.equals(VmStateLibvirt.CRASHED.getValue())) {
            return VmState.CRASHED.value();
        } else if (ret.equals(VmStateLibvirt.INSHUTDOWN.getValue())) {
            return VmState.RUNNING.value();
        } else {
            return ret;
        }
    }

    public void undefine(String instanceId) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "undefine", instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::undefine 失败");
    }

    public void undefine(String instanceId, String hostIp) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + hostIp + "/system", "undefine", instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::undefine 失败");
    }

    public void define(String libvirtFilePath) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "define", libvirtFilePath};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::define 失败");
    }

    public void define(String instanceId, String hostIp) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + hostIp + "/system", "define", instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::define 失败");
    }

    @Override
    public NodeInfo info() {
        NodeInfo info = new NodeInfo();
        String res = SystemUtil.run(new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "nodeinfo"});
        String[] lines = res.split("\n");
        Map<String, String> map = new HashMap<String, String>();
        for (String line : lines) {
            String[] result = line.split(":");
            map.put(result[0].trim(), result[1].trim());
        }

        info.setCpuModel(map.get("CPU model"));

        if(StringUtils.isNotBlank(map.get("CPU(s)"))){
            info.setCpus(Integer.parseInt(map.get("CPU(s)")));
        }

        if(StringUtils.isNotBlank(map.get("CPU frequency"))){
            info.setCpuFrequency(Integer.parseInt(map.get("CPU frequency").replace(" MHz", "")));
        }

        if(StringUtils.isNotBlank(map.get("CPU socket(s)"))){
            info.setCpuSockets(Integer.parseInt(map.get("CPU socket(s)")));
        }

        if(StringUtils.isNotBlank(map.get("Core(s) per socket"))){
            info.setCoresPerSocket(Integer.parseInt(map.get("Core(s) per socket")));
        }

        if(StringUtils.isNotBlank(map.get("Thread(s) per core"))){
            info.setThreadsPerCore(Integer.parseInt(map.get("Thread(s) per core")));
        }

        if(StringUtils.isNotBlank(map.get("NUMA cell(s)"))){
            info.setNumaCells(Integer.parseInt(map.get("NUMA cell(s)")));
        }

        if(StringUtils.isNotBlank(map.get("Memory size"))){
            info.setMemorys(Integer.parseInt(map.get("Memory size").replace(" KiB", "")));
        }

        return info;
    }

    @Override
    public void autoStart(String instanceId, int autoStart) {
        String[] cmd = null;
        if (autoStart == 0) {
            cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "autostart", instanceId, "--disable"};
        } else {
            cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "autostart", instanceId};
        }
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::auto start 失败");
    }

    @Override
    public List<DomainBlk> domblklist(String instanceId) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "domblklist", instanceId};
        return getDomBlkList(cmd);
    }

    @Override
    public List<DomainListInfo> listVm(String hostIp, boolean isAll, boolean isPersistent) {
        List<DomainListInfo> list = new ArrayList<DomainListInfo>();
        List<String> cmdList = new ArrayList<String>();
        cmdList.add("virsh");
        if (StringUtils.isBlank(hostIp)) {
            hostIp = computeNodeProp.getNodeIp();
        }

        cmdList.add("-c");
        cmdList.add("qemu+tcp://" + hostIp + "/system");

        cmdList.add("list");

        if (isAll) {
            cmdList.add("--all");
        }

        if (isPersistent) {
            cmdList.add("--persistent");
        }

        String[] cmd = new String[cmdList.size()];
        cmd = cmdList.toArray(cmd);
        return getListVm(list, cmd);
    }

    @Override
    public void migrate(String type, String instanceId, String targetIp, Boolean isStorageAll) {
        String[] cmd = null;
        if (isStorageAll) {
            cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "migrate", "--" + type, instanceId, "qemu+tcp://" + targetIp + "/system", "tcp://" + targetIp, "--storage-all"};
        } else {
            cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "migrate", "--" + type, instanceId, "qemu+tcp://" + targetIp + "/system", "tcp://" + targetIp};
        }

        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "::迁移失败");
    }

    public DomainDetail getVmDetail(String instanceId) {

        DomainDetail detail = new DomainDetail();
        InputStream is = null;

        try {

            String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "dumpxml", instanceId};
            String res = SystemUtil.run(cmd);

            // log.debug("xml=" + res);

            try {
                is = new ByteArrayInputStream(res.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.debug("not support UTF-8");
                is = new ByteArrayInputStream(res.getBytes());
            }

            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);

			/*Element graphic = (Element) doc.selectSingleNode("/domain/devices/graphics");
			if (graphic == null) {
				log.error("graphic not found!");
			} else {

				String remoteType = graphic.attributeValue("type");
				String remotePort = graphic.attributeValue("port");
				Boolean autoPort = graphic.attributeValue("autoport").equals("yes");
				String listen = graphic.attributeValue("listen");

				detail.setAutoPort(autoPort);
				detail.setRemoteType(remoteType);
				detail.setListen(listen);
				detail.setRemotePort(remotePort);

			}
			*/

			if(doc.selectSingleNode("/domain/memory") != null){
                detail.setMemory(((Long)(Long.valueOf(doc.selectSingleNode("/domain/memory").getText()) / 1024)).intValue());
            }

            if(doc.selectSingleNode("/domain/currentMemory") != null){
                detail.setCurrentMemory(((Long)(Long.valueOf(doc.selectSingleNode("/domain/currentMemory").getText()) / 1024)).intValue());
            }

            if(doc.selectSingleNode("/domain/vcpu") != null){
                detail.setVcpu(Integer.valueOf(doc.selectSingleNode("/domain/vcpu").getText()));
            }

            List<Element> graphics = (List<Element>) doc.selectNodes("/domain/devices/graphics");
            if (graphics == null || graphics.size() == 0) {
                log.error("graphic not found!");
            } else {
                for (Element graphic : graphics) {
                    String remoteType = graphic.attributeValue("type");
                    String remotePort = graphic.attributeValue("port");
                    Boolean autoPort = graphic.attributeValue("autoport").equals("yes");
                    String listen = graphic.attributeValue("listen");
                    if (remoteType.equals("vnc")) {
                        detail.setAutoPort(autoPort);
                        detail.setRemoteType(remoteType);
                        detail.setListen(listen);
                        detail.setRemotePort(remotePort);
                    } else if (remoteType.equals("spice")) {
                        detail.setSpiceAutoPort(autoPort);
                        detail.setSpiceType(remoteType);
                        detail.setSpiceListen(listen);
                        detail.setSpicePort(remotePort);
                    }
                }
            }

            try {
                List<Element> qemuCommandlines = (List<Element>) doc.selectNodes("/domain/qemu:commandline/qemu:arg");
                if (qemuCommandlines != null && qemuCommandlines.size() > 0) {
                    for (Element qemuCommandline : qemuCommandlines) {
                        String value = qemuCommandline.attributeValue("value");
                        if (StringUtils.isNotBlank(value)) {
                            detail.getQemuArgs().add(value);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("qemu:commandline/qemu:arg not find", e);
            }

            Element emulator = (Element) doc.selectSingleNode("/domain/devices/emulator");
            if (emulator == null) {
                log.error("emulator not found!");
            } else {
                String emulatorPath = emulator.getText();
                detail.setEmulator(emulatorPath);
            }

            List<DomainDisk> domainDisks = new ArrayList<DomainDisk>();

            List<Element> diskList = (List<Element>) doc.selectNodes("/domain/devices/disk");
            if (diskList != null && diskList.size() > 0) {

                for (Element diskE : diskList) {

                    DomainDisk domDisk = new DomainDisk();

                    String diskDevice = diskE.attributeValue("device");
                    String diskType = diskE.attributeValue("type");
                    domDisk.setDiskDevice(diskDevice);
                    domDisk.setDiskType(diskType);

                    Element driverE = (Element) diskE.selectSingleNode("driver");
                    if (driverE != null) {
                        String driverName = driverE.attributeValue("name");
                        String driverCache = driverE.attributeValue("cache");
                        String driverType = driverE.attributeValue("type");
                        domDisk.setDriverName(driverName);
                        domDisk.setDriverCache(driverCache);
                        domDisk.setDriverType(driverType);
                    }

                    Element sourceE = (Element) diskE.selectSingleNode("source");
                    if (sourceE != null) {
                        String sourceFile = sourceE.attributeValue("file");
                        String sourceDev = sourceE.attributeValue("dev");
                        domDisk.setSourceFile(sourceFile);
                        domDisk.setSourceDev(sourceDev);
                    }

                    Element targetE = (Element) diskE.selectSingleNode("target");
                    if (targetE != null) {
                        String targetDev = targetE.attributeValue("dev");
                        String targetBus = targetE.attributeValue("bus");
                        domDisk.setTargetDev(targetDev);
                        domDisk.setTargetBus(targetBus);
                    }

                    domainDisks.add(domDisk);

                }

            }

            detail.setDomainDisks(domainDisks);

            List<DomainInterface> domainInterfaces = new ArrayList<DomainInterface>();

            List<Element> interfaceList = (List<Element>) doc.selectNodes("/domain/devices/interface");
            if (interfaceList != null && interfaceList.size() > 0) {

                for (Element interfaceE : interfaceList) {

                    DomainInterface domInterface = new DomainInterface();

                    Element sourceE = (Element) interfaceE.selectSingleNode("source");
                    if (sourceE != null) {
                        String sourceBridge = sourceE.attributeValue("bridge");
                        domInterface.setSourceBridge(sourceBridge);
                    }

                    Element targetE = (Element) interfaceE.selectSingleNode("target");
                    if (targetE != null) {
                        String targetDev = targetE.attributeValue("dev");
                        domInterface.setTargetDev(targetDev);
                    }

                    Element macE = (Element) interfaceE.selectSingleNode("mac");
                    if(macE != null){
                        String macAddress = macE.attributeValue("address");
                        domInterface.setMacAddress(macAddress);
                    }

                    domainInterfaces.add(domInterface);

                }

            }

            detail.setDomainInterfaces(domainInterfaces);

            String[] domInfoCmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "dominfo", instanceId};
            String domInfoRes = SystemUtil.run(domInfoCmd);
            if (!StringUtils.isBlank(domInfoRes)) {
                String[] infos = domInfoRes.split("\n");
                for (String info : infos) {
                    String[] infoDetail = info.split(":");
                    if (infoDetail.length != 2) {
                        continue;
                    }

                    String key = infoDetail[0].trim();
                    String value = infoDetail[1].trim();

                    if ("Persistent".equals(key)) {
                        Boolean isPresistent = null;
                        if ("yes".equals(value)) {
                            isPresistent = true;
                        } else if ("no".equals(value)) {
                            isPresistent = false;
                        }
                        detail.setIsPersistent(isPresistent);

                    } else if ("State".equals(key)) {
                        detail.setDomState(value);
                    }

                }

            }

        } catch (Exception e) {
            log.error("::获取虚拟机信息失败", e);
            throw new GCloudException("::获取虚拟机信息失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ce) {
                    log.error("修改关闭失败", ce);
                }

            }
        }

        return detail;

    }

    @Override
    public void faultTolreant(String sourceIp, String targetIp, int port, String instanceId) {
        ComputeNodeProp computeNodeProp = SpringUtil.getBean(ComputeNodeProp.class);
        String[] cmd = new String[]{computeNodeProp.getConfigurePath() + "/faulttolreant/faultTolreantForMc.sh", sourceIp, targetIp, String.valueOf(port), instanceId};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::faulttolreant失败");
    }

    @Override
    public FtInfo faultTolreantInfo(String instanceId, String targetIp) {
        FtInfo ftInfo = new FtInfo();
        ftInfo.setStatus(FtState.FAILED.getName());
        String res = null;
        try {
            String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + targetIp + "/system", "qemu-monitor-command", instanceId, "--hmp", "info migrate"};
            res = SystemUtil.run(cmd);
            String[] lines = res.split("\n");
            if (lines != null && lines.length > 0) {
                String[] state = lines[1].split(":");
                ftInfo.setStatus(state[1].replace("\r", "").trim());
            }
        } catch (Exception e) {
            log.error("获取云服务器容错状态异常，命令返回：" + res, e);
            ftInfo.setStatus(FtState.FAILED.getName());
        }
        return ftInfo;
    }

    @Override
    public void attachDevice(String instanceId, String devFilePath) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "attach-device", instanceId, devFilePath};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::挂载失败");

    }

    @Override
    public void detachDevice(String instanceId, String devFilePath) {

        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "detach-device", instanceId, devFilePath};
        int ret = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, ret, "::卸载失败");

    }

    //只处理，名称没有空格的，如果名称有空格，则匹配错误，也不属于管理范围内
    //减少调用次数，如果实在有特殊要求，改用 --uuid，然后再一个个获取状态
    private List<DomainListInfo> getListVm(List<DomainListInfo> list, String[] cmd) {
        String result = SystemUtil.run(cmd);

        String[] resultArr = result.split("\n");

        if (resultArr.length > 2) {

            for (int i = 2; i < resultArr.length; i++) {

                String dom = resultArr[i];
                dom = dom.trim();
                dom = dom.replaceAll(" +", " ");
                Pattern pat = Pattern.compile("([^ ]+) ([^ ]+) (.+)");
                Matcher mat = pat.matcher(dom.trim());
                if (mat.find()) {

                    DomainListInfo dInfo = new DomainListInfo();
                    dInfo.setDomainId(mat.group(1));
                    dInfo.setDomainAlias(mat.group(2));
                    dInfo.setState(mat.group(3));
                    list.add(dInfo);
                }

            }

        }
        return list;
    }

    private List<DomainBlk> getDomBlkList(String[] cmd) {
        List<DomainBlk> list = new ArrayList<DomainBlk>();
        String result = SystemUtil.run(cmd);

        String[] resultArr = result.split("\n");

        if (resultArr.length > 2) {

            for (int i = 2; i < resultArr.length; i++) {

                String dom = resultArr[i];
                dom = dom.trim();
                dom = dom.replaceAll("        ", " ");
                String[] res = dom.split(" ");
                if (res != null && res.length == 2) {
                    DomainBlk db = new DomainBlk();
                    db.setTarget(res[0]);
                    db.setSource(res[1]);
                    list.add(db);
                }

            }

        }
        return list;
    }

    public String ttyconsole(String instanceId) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "ttyconsole", instanceId};
        return SystemUtil.run(cmd);
    }

    public int queryVncPort(String instanceId) {
        String[] cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "vncdisplay", instanceId};
        String ret = SystemUtil.run(cmd);
        String _ret = ret.replace('\n', ' ').replace(':', ' ').trim();
        return 5900 + Integer.valueOf(_ret);
    }

    private void removeStartInfo(String instanceId) {
        try {
            VmStartLock.removeStartList(instanceId);
        } catch (Exception ex) {
            log.error("移除startList失败", ex);
        }

        try {
            VmStartLock.removeConcurrentNum(instanceId);
        } catch (Exception ex) {
            log.error("移除startMap失败", ex);
        }
    }

    private void startSleep(String instanceId) throws InterruptedException {
        // 避免启动风暴，启动成功之后sleep
        ComputeNodeProp computeNodeProp = SpringUtil.getBean(ComputeNodeProp.class);
        double result = 0.0;
        double maxLocalAve = computeNodeProp.getMaxLocalAverage();
        int maxSleepCount = computeNodeProp.getMaxSleepCount();

        result = VmUtil.getNowLoadAverage(String.format("虚拟机开机【%s】，获取当前负载错误", instanceId));

        int sleepNum = 1;
        boolean isTimeout = false;
        log.debug(String.format("虚拟机开机【%s】，当前负载%s", instanceId, result));
        log.debug(String.format("虚拟机开机【%s】，最大负载%s", instanceId, maxLocalAve));

        // 负载小于最大设置负载
        if (result <= maxLocalAve) {
            Thread.sleep(computeNodeProp.getVmStartSleepTime() * 1000L);
            result = VmUtil.getNowLoadAverage(String.format("虚拟机开机【%s】，获取当前负载错误", instanceId));
            log.debug(String.format("虚拟机开机【%s】，当前负载%s", instanceId, result));
            log.debug(String.format("虚拟机开机【%s】，最大负载%s", instanceId, maxLocalAve));
        }

        while (result >= maxLocalAve) {
            if (sleepNum <= maxSleepCount) {
                log.debug(String.format("虚拟机开机【%s】，第%s次睡眠，睡眠时长%s秒", instanceId, sleepNum, computeNodeProp.getVmStartSleepTime()));
                Thread.sleep(computeNodeProp.getVmStartSleepTime() * 1000L);
                result = VmUtil.getNowLoadAverage(String.format("虚拟机开机【%s】，获取当前负载错误", instanceId));
                log.debug(String.format("虚拟机开机【%s】，当前负载%s", instanceId, result));
                log.debug(String.format("虚拟机开机【%s】，最大负载%s", instanceId, maxLocalAve));
            } else {
                isTimeout = true;
                break;
            }

            sleepNum++;
        }

        if (isTimeout) {
            log.debug(String.format("虚拟机开机【%s】超出重试次数%s，当前负载%s，继续开机", instanceId, maxSleepCount, result));
        } else {
            log.debug(String.format("虚拟机开机【%s】当前负载%s达到可以开机", instanceId, result));
        }
    }

    public Boolean isDomPersistent(String instanceId) {
        Boolean isPresistent = false;
        String[] domInfoCmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "dominfo", instanceId};
        String domInfoRes = SystemUtil.run(domInfoCmd);
        if (!StringUtils.isBlank(domInfoRes)) {
            String[] infos = domInfoRes.split("\n");
            for (String info : infos) {
                String[] infoDetail = info.split(":");
                if (infoDetail.length != 2) {
                    continue;
                }

                String key = infoDetail[0].trim();
                String value = infoDetail[1].trim();

                if ("Persistent".equals(key)) {

                    if ("yes".equals(value)) {
                        isPresistent = true;
                    } else if ("no".equals(value)) {
                        isPresistent = false;
                    }
                }
            }
        }
        return isPresistent;
    }

    @Override
    public boolean agentGuestFsFreezeFreeze(String instanceId) {
        try {
            String[] cmd = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-fsfreeze-freeze\"}'", computeNodeProp.getNodeIp(), instanceId)};
            String res = SystemUtil.run(cmd);
            printCommand(cmd, res);
            Map<String, Object> map = ParamUtil.stringToMap(res);
            int code = Integer.parseInt(String.valueOf(map.get("return")));// code
            return code > 0;
        } catch (Exception e) {
            log.error("::agentGuestFsFreezeFreeze失败", e);
            return false;
        }
    }

    @Override
    public boolean agentGuestFsFreezeThaw(String instanceId) {
        try {
            String[] cmd = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system  qemu-agent-command %s '{\"execute\": \"guest-fsfreeze-thaw\"}'", computeNodeProp.getNodeIp(), instanceId)};
            String res = SystemUtil.run(cmd);
            printCommand(cmd, res);
            Map<String, Object> map = ParamUtil.stringToMap(res);
            int code = Integer.parseInt(String.valueOf(map.get("return")));// code
            return code > 0;
        } catch (Exception e) {
            log.error("::agentGuestFsFreezeThaw失败", e);
            return false;
        }
    }

    @Override
    public FsfreezeType agentGuestFsFreezeStatus(String instanceId) {
        try {
            String[] cmd = new String[]{"bash", "-c", String.format("virsh -c qemu+tcp://%s/system qemu-agent-command %s '{\"execute\": \"guest-fsfreeze-status\"}'", computeNodeProp.getNodeIp(),instanceId)};
            String res = SystemUtil.run(cmd);
            printCommand(cmd, res);
            Map<String, Object> map = ParamUtil.stringToMap(res);
            String type = String.valueOf(map.get("return"));// code
            if (StringUtils.isNotBlank(type)) {
                return FsfreezeType.getByValue(type);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("::获取agentGuestFsFreezeStatus失败", e);
            return null;
        }
    }

    @Override
    public void tlsMigrate(String type, String instanceId, String targetHostName, Boolean isStorageAll) {
        String[] cmd = null;
        if (isStorageAll) {
            cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "migrate", "--" + type, "--tunnelled", "--p2p", instanceId, "qemu+tls://" + targetHostName + "/system", "--storage-all"};
        } else {
            cmd = new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "migrate", "--" + type, "--tunnelled", "--p2p", instanceId, "qemu+tls://" + targetHostName + "/system"};
        }

        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "::加密迁移失败");
    }

    public PlatformType getSystemType(String instanceId) throws GCloudException {

		String[] linuxCmd = new String[] { "uname", "-a" };

		boolean linuxRet = agentGuestExec(instanceId, linuxCmd, 30);
		if (linuxRet) {
			return PlatformType.LINUX;
		} else {
			return PlatformType.WINDOWS;
		}
	}

    @Override
    public String domInfo(String instanceId) {
        String result = null;
        String[] ret = SystemUtil.runAndGetCodeAndValue(new String[]{"virsh", "-c", "qemu+tcp://" + computeNodeProp.getNodeIp() + "/system", "dominfo", instanceId});
        int retCode = Integer.valueOf(ret[0]);
        if(retCode == 0){
            result = ret[1];
        }
        return result;
    }
    
}
