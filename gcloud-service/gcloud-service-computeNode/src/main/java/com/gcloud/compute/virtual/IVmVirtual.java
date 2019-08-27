package com.gcloud.compute.virtual;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.FsfreezeType;
import com.gcloud.header.compute.enums.PlatformType;
import com.gcloud.service.common.compute.model.DomainBlk;
import com.gcloud.service.common.compute.model.DomainDetail;
import com.gcloud.service.common.compute.model.DomainListInfo;
import com.gcloud.service.common.compute.model.FtInfo;
import com.gcloud.service.common.compute.model.NodeInfo;

import java.util.List;

/**
 * Created by yaowj on 2018/9/7.
 */
public interface IVmVirtual {

    /**
     * @return
     * @Title: info
     * @Description: 查询物理机节点信息
     * @date 2015-5-20 下午7:35:13
     */
    NodeInfo info();

    /**
     * 云服务器开机
     *
     * @param instanceId 云服务器id
     * @throws GCloudException
     * @Title: start
     * @date 2015-5-22 下午2:54:13
     */
    void start(String instanceId) throws GCloudException;

    /**
     * 云桌面开机
     *
     * @param instanceId 云服务器id
     * @throws GCloudException
     * @Title: start
     * @Description: TODO
     * @date 2015-5-22 下午2:54:13
     */
    void startDesktop(String instanceId) throws GCloudException;

    /**
     * 云服务器开机
     *
     * @param instanceId 云服务器id
     * @param hostIp
     * @throws GCloudException
     * @Title: start
     * @date 2015-5-22 下午2:54:13
     */
    void start(String instanceId, String hostIp) throws GCloudException;

    /**
     * 正常关机
     *
     * @param instanceId 云服务器id
     * @return
     * @throws GCloudException
     * @Title: shutdown
     * @date 2015-5-22 下午2:43:29
     */
    void shutdown(String instanceId) throws GCloudException;

    /**
     * 强制关机
     *
     * @param instanceId 云服务器id
     * @throws GCloudException
     * @Title: destroy
     * @date 2015-5-22 下午2:43:03
     */
    void destroy(String instanceId) throws GCloudException;

    /**
     * 强制关机
     *
     * @param instanceId 云服务器id
     * @param hostIp
     * @throws GCloudException
     * @Title: destroy
     * @date 2015-5-22 下午2:43:03
     */
    void destroy(String instanceId, String hostIp) throws GCloudException;

    /**
     * 云服务器正常重启
     *
     * @param instanceId 云服务器id
     * @throws GCloudException
     * @Title: hotReboot
     * @date 2015-5-22 下午2:53:45
     */
    void reboot(String instanceId) throws GCloudException;

    /**
     * 查询服务器状态
     *
     * @param instanceId
     * @return
     * @Title: domstate
     * @date 2015-5-22 下午2:53:34
     */
    String domstate(String instanceId) throws GCloudException;

    String domInfo(String instanceId);

    /**
     * 查询远程libvirt服务器状态
     *
     * @param instanceId
     * @param targetIp
     * @return
     * @Title: domstate
     * @date 2015-5-22 下午2:53:34
     */
    String domstate(String instanceId, String targetIp) throws GCloudException;

    /**
     * 把libvirt的状态转换成vmState(gcloud自己的虚拟机状态)的状态
     *
     * @param instanceId
     * @return
     * @Title: queryStateForGcloud
     * @date 2015-5-20 下午3:07:12
     */
    String queryStateForGcloud(String instanceId);

    /**
     * 远程查询状态，把libvirt的状态转换成vmState(gcloud自己的虚拟机状态)的状态
     *
     * @param instanceId
     * @param hostIp
     * @return
     * @Title: queryStateForGcloud
     * @date 2015-5-20 下午3:07:12
     */
    String queryStateForGcloud(String instanceId, String hostIp);

    /**
     * @param state
     * @return
     * @Title: changeStateForGcloud
     * @Description: 把云服务器状态
     * @date 2015-7-10 上午9:24:41
     */
    String changeStateForGcloud(String state);

    /**
     * undefine云服务器
     *
     * @param instanceId 云服务器id
     * @return
     * @Title: undefine
     * @date 2015-5-19 上午10:56:02
     */
    void undefine(String instanceId) throws GCloudException;

    /**
     * undefine云服务器
     *
     * @param instanceId 云服务器id
     * @param hostIp
     * @return
     * @Title: undefine
     * @date 2015-5-19 上午10:56:02
     */
    void undefine(String instanceId, String hostIp) throws GCloudException;

    /**
     * @param filePath
     * @return
     * @Title: define
     * @Description: define 虚拟机（libvirt）
     * @date 2015-5-18 上午9:57:19
     */
    void define(String filePath) throws GCloudException;

    /**
     * @param filePath
     * @param hostIp
     * @return
     * @Title: define
     * @Description: define 虚拟机（libvirt）
     * @date 2015-5-18 上午9:57:19
     */
    void define(String filePath, String hostIp) throws GCloudException;

    /**
     * 是否开机自启动
     *
     * @param instanceId 云服务器id
     * @param autoStart  自启动参数，1，自启动，0。不自启动
     * @return
     * @Title: autoStart
     * @date 2015-5-20 下午5:16:41
     */
    void autoStart(String instanceId, int autoStart) throws GCloudException;

    /**
     * @param type
     * @param instanceId
     * @param targetIp
     * @return
     * @Title: migrate
     * @Description: 迁移命令
     * @date 2015-5-22 上午10:09:49
     */
    void migrate(String type, String instanceId, String targetIp, Boolean isStorageAll) throws GCloudException;

    /**
     * @param instanceId
     * @return
     * @Title: domblklist
     * @Description: 获取设备列表
     * @date 2015-5-22 下午4:31:15
     */
    List<DomainBlk> domblklist(String instanceId);

    /**
     * @param hostIp
     * @param isAll
     * @param isPersistent
     * @return
     * @Title: listVm
     * @Description: 获取虚拟机列表
     * @date 2015-5-22 下午4:31:15
     */
    List<DomainListInfo> listVm(String hostIp, boolean isAll, boolean isPersistent);

    /**
     * @param instanceId
     * @return
     * @throws GCloudException
     * @Title: getVmDetail
     * @Description: 获取虚拟机详细信息
     * @date 2015-5-23 下午5:20:54
     */
    DomainDetail getVmDetail(String instanceId) throws GCloudException;


    /**
     * @param sourceIp   容错目标节点IP
     * @param targetIp   容错源节点IP
     * @param port       容错端口
     * @param instanceId 容错虚拟机ID
     * @throws GCloudException
     * @Title: faultTolreant
     * @Description: 虚拟机容错命令
     * @date 2015-6-3 上午10:18:43
     */
    void faultTolreant(String sourceIp, String targetIp, int port, String instanceId) throws GCloudException;

    /**
     * @param instanceId 容错虚拟机ID
     * @param targetIp   远程查询操作
     * @Title: faultTolreantInfo
     * @Description: 虚拟机容错详情
     * @date 2015-6-3 上午11:39:03
     */
    FtInfo faultTolreantInfo(String instanceId, String targetIp);

    /**
     * @param instanceId  云服务器ID
     * @param devFilePath 设备配置文件路径
     * @throws GCloudException
     * @Title: attachDevice
     * @Description: 挂载设备
     * @date 2015-6-8 下午1:57:06
     */
    void attachDevice(String instanceId, String devFilePath) throws GCloudException;

    /**
     * @param instanceId  云服务器ID
     * @param devFilePath 设备配置文件路径
     * @throws GCloudException
     * @Title: detachDevice
     * @Description: 卸载设备
     * @date 2015-6-8 下午1:57:42
     */
    void detachDevice(String instanceId, String devFilePath) throws GCloudException;

    String ttyconsole(String instanceId);

    Boolean isDomPersistent(String instanceId);

    int queryVncPort(String instanceId);

    boolean agentGuestInfo(String instanceId);

    boolean agentGuestFsFreezeFreeze(String instanceId);

    boolean agentGuestFsFreezeThaw(String instanceId);

    FsfreezeType agentGuestFsFreezeStatus(String instanceId);

    boolean agentGuestSetUserPassword(String instanceId, String username, String password, boolean crypted) throws GCloudException;

    boolean agentGuestExec(String instanceId, String[] cmd, int timeout) throws GCloudException;

    int agentGuestExecCode(String instanceId, String[] cmd, int timeout);

    boolean agentGuestSaveFile(String instanceId, String context, String path, String fileName, PlatformType type) throws GCloudException;

    boolean agentGuestSaveFileForMultiString(String instanceId, List<String> contexts, String path, String fileName, PlatformType type) throws GCloudException;

    String agentGuestReadFile(String instanceId, String tempReturnFile);

    String agentGuestExecReturn(String instanceId, String[] cmd, int timeout, PlatformType type) throws GCloudException;

    Boolean agentGuestIsExistFile(String instanceId, String path);

    void tlsMigrate(String type, String instanceId, String targetIp, Boolean isStorageAll) throws GCloudException;
    
    PlatformType getSystemType(String instanceId) throws GCloudException;
}
