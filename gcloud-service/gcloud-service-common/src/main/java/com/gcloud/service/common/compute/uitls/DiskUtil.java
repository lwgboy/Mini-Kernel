package com.gcloud.service.common.compute.uitls;

import com.gcloud.common.util.FileUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.DiskFormatType;
import com.gcloud.header.compute.enums.DiskProtocol;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DiskUtil {
	
	public static void mount(String soursePath, String mountPath) throws GCloudException {
		String[] cmd = new String[] { "mount", soursePath, mountPath };
		int res = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, res, "::挂载失败");
	}
	
	public static void umount(String umountPath) throws GCloudException {
		String[] cmd = new String[] { "umount", "-f", umountPath };
		int res = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, res, "::卸载失败");
	}
	
	public static void format(String diskPath, String type) throws GCloudException {
		String[] cmd = null;
		if (DiskFormatType.EXT4.getValue().equals(type)) {
			cmd = new String[] { "mkfs.ext4", diskPath };
		}
		int res = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, res, "::格式化失败");
	}

	public static void extract(String soursePath, String extractPath) throws GCloudException {
		String[] cmd = new String[] { "tar", "-xzvf", soursePath, "-C", extractPath };
		int res = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, res, "::解压失败");
	}


	public static void rm(String path) throws GCloudException {
		String[] cmd = new String[] { "rm", "-rf", path };
		int res = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, res, "::删除失败");
	}


	public static void rsync(String soursePath, String extractPath) throws GCloudException {
		String[] cmd = new String[] { "rsync", "-a", soursePath, extractPath };
		int res = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, res, "::rsync 失败");
	}
	
	public static String isMount(String mountPath, String string) {
		return SystemUtil.run(new String[]{"bash","-c"," mount | grep "+ mountPath });
	}

	/**
	 * 
	  * @Title: getQcow2Size
	  * @Description: 根据块设备大小（G）计算返回qcow2文件大小，规则：
	  * 		如果大小小于等于1T，qcow2大小比卷大小小30mb
      * 		大于1T，比卷小100mb
	  * @date 2017-2-20 上午11:29:57
	  *
	  * @param volumeSize 
	  * @return 单位:MB
	 */
	public static Integer getQcow2SizeMb(Integer volumeSize) {
		if (volumeSize <= 1024) {
			return volumeSize * 1024 - 30;
		} else {
			return volumeSize * 1024 - 100;
		}
	}
	
	/**
	 * 
	  * @Title: getRoundDiskSizeMb
	  * @Description: 将大小Mb转换成Gb之后再四舍五入，然后再转换成Mb;
	  * @date 2017-2-21 下午1:47:15
	  *
	  * @param size MB
	  * @return
	 */
	public static Integer getRoundDiskSizeMb(Integer size) {
		return getRoundDiskSizeForGb(size) * 1024;
	}
	
	/**
	 * 
	  * @Title: getRoundDiskSizeForGb
	  * @Description: 将大小Mb转换成Gb之后再四舍五入
	  * @date 2017-2-21 下午1:47:15
	  *
	  * @param size MB
	  * @return
	 */
	public static Integer getRoundDiskSizeForGb(Integer size) {
		return (int) Math.round(size / 1024d);
	}
	
	/**
	 * 
	 * @Title: getSystemDiskFile
	 * @Description: 获取系统盘名字
	 * @date 2015-5-18 下午8:12:00
	 * 
	 * @param instanceId
	 * @return
	 */
	public static String getSystemDiskFileName(String instanceId) {
		return String.format("%s_snap", instanceId);
	}
	
	public static String getDeviceMountpoint(String device) {
		return String.format("/dev/%s", device);
	}

	public static String getDiskPath(String path, DiskProtocol diskProtocol){

		String result = path;
		if(diskProtocol == null){
			return result;
		}

		switch (diskProtocol){

			case RBD:
				result = String.format("rbd:%s", path);
				break;
			default:
				result = path;
				break;

		}

		return result;
	}

	public static String volumeSourcePath(String volumeId, String pool, DiskProtocol diskProtocol){

	    String volumeSourcePath = null;
	    if(StringUtils.isBlank(volumeId) || diskProtocol == null){
            return null;
        }
	    switch (diskProtocol){

            case RBD:
                volumeSourcePath = String.format("%s/volume-%s", pool, volumeId);
                break;
            case FILE:
                volumeSourcePath = FileUtil.fileString(pool, volumeId);
                break;
            default:
                break;

        }

	    return volumeSourcePath;
    }
}
