/*
 * @Date 2015-4-14
 *
 * @Author zhangzj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 磁盘操作qemu实现类
 */
package com.gcloud.service.common.compute.uitls;

import com.alibaba.fastjson.JSONObject;
import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.FileFormat;
import com.gcloud.header.compute.enums.Unit;
import com.gcloud.service.common.compute.model.QemuInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DiskQemuImgUtil {

    public static void convert() {

    }

    /**
     * @param systemDiskPath
     * @param format
     * @throws GCloudException
     * @Title: convert
     * @Description: 转换格式
     * @date 2015-6-11 下午3:25:05
     */
    public static void convert(String systemDiskPath, String targetPath, String sourceFormat, String targetFormat, boolean isCompress) {
        String[] cmd = null;
        if (isCompress) {
            cmd = new String[]{"qemu-img", "convert", "-c", "-f", sourceFormat, "-O", targetFormat, systemDiskPath, targetPath};
        } else {
            cmd = new String[]{"qemu-img", "convert", "-f", sourceFormat, "-O", targetFormat, systemDiskPath, targetPath};
        }
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "::convert 转换是失败");

    }

    /**
     * @param bFile
     * @param disk
     * @param format
     * @return
     * @Title: create
     * @Description: 基础create一个disk，指定backing file
     * @date 2015-5-16 下午1:56:48
     */
    public static void create(String bFile, String disk, String format) throws GCloudException {

        String[] cmd = new String[]{"qemu-img", "create", "-b", bFile, "-f", format, disk};
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "1010506::基于镜像创建磁盘文件失败");

    }

    public static void create(String bFile, String disk, String format, Integer size, String unit) throws GCloudException {
        String[] cmd = new String[]{"qemu-img", "create", "-b", bFile, "-f", format, disk, size + unit};
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "1010507::基于镜像创建磁盘文件失败");
    }

    /**
     * @param size
     * @param unit
     * @param disk
     * @param format
     * @return
     * @Title: create
     * @Description: 基础create 一个 disk
     * @date 2015-5-16 下午1:57:13
     */
    public static void create(Integer size, String unit, String disk, String format) throws GCloudException {

        String[] cmd = new String[]{"qemu-img", "create", "-f", format, disk, size + unit};
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "1010505::创建磁盘文件失败");

    }

    /**
     * @param bFile
     * @param disk
     * @return
     * @Title: create
     * @Description: 创建一个盘，指定backing file，默认用qcow2
     * @date 2015-5-16 上午11:36:34
     */
    public static void create(String bFile, String disk) throws GCloudException {
        create(bFile, disk, FileFormat.QCOW2.getValue());
    }

    public static void create(String bFile, String disk, Integer size, Unit unit) throws GCloudException {
        create(bFile, disk, FileFormat.QCOW2.getValue(), size, unit.getValue());
    }

    /**
     * @param size
     * @param disk
     * @param errorCode
     * @return
     * @Title: create
     * @Description: 创建一个盘.单位为G，文件格式为QCOW2
     * @date 2015-5-16 上午11:36:23
     */
    public static void create(String disk, Integer size, Unit unit) throws GCloudException {
        create(size, unit.getValue(), disk, FileFormat.QCOW2.getValue());
    }

    /**
     * @param path
     * @return QemuInfo
     * @throws GCloudException
     * @Title: info
     * @Description: qmeu-img info 查询
     * @date 2015-7-21 下午3:57:44
     */
    public static QemuInfo info(String path) throws GCloudException {
        String res = SystemUtil.run(new String[]{"qemu-img", "info", "--output", "json", path});

        if(StringUtils.isBlank(res)){
            throw new GCloudException("::获取磁盘信息失败");
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(res);
            QemuInfo info = new QemuInfo();
            info.setFormat(jsonObject.getString("format"));
            info.setVirtualSize(jsonObject.getLong("virtual-size"));
            info.setBackingFile(jsonObject.getString("backing-filename"));

            return info;
        } catch (Exception ex) {
            log.error("解析qemu json 失败", ex);
            throw new GCloudException("::获取qemu info 信息失败");
        }
    }

    public static void resize(String path, Integer newSize) throws GCloudException {

        String[] cmd = new String[]{"qemu-img", "resize", path, "+" + newSize + "G"};
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "::resize 失败");

    }

    public static void rebase(String snapPath, String imagePath) throws GCloudException {

        String[] cmd = new String[]{"qemu-img", "rebase", "-u", "-b", imagePath, snapPath};
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, "::rebase 失败");

    }

}
