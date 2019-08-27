package com.gcloud.service.common.compute.uitls;

import java.util.ArrayList;
import java.util.List;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.service.common.compute.model.DdBlockModel;

public class DdUtil {

	/**
	 * 
	 * @Title: ddCopyFile
	 * @Description: dd 文件，默认bs=2M
	 * @date 2015-5-19 上午10:28:22
	 *
	 * @param sourePath
	 * @param targetPath
	 * @return
	 * @throws GCloudException 
	 */
	public static void ddCopyFile(String sourePath, String targetPath) throws GCloudException {
		String[] cmd = new String[] { "dd", "if=" + sourePath, "of=" + targetPath, "bs=2M"};
		int rc = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, rc, "::dd失败");
	}
	
	/**
	 * 
	  * @Title: ddCopyFile
	  * @Description: dd 文件， 通过 DdBlockUtil.getDdBlockInfo(size) 得到DdBlockModel，然后传入count,skip和bs进去
	  * @date 2016-12-6 下午2:23:37
	  *
	  * @param sourePath
	  * @param targetPath
	  * @param count
	  * @param bs  单位：k
	  * @param skip
	  * @return
	 */
	public static boolean ddCopyFile(String sourePath, String targetPath, Long count, Long bs, Long skip) {
		List<String> cmdList = new ArrayList<String>();
		cmdList.add("dd");
		cmdList.add("if=" + sourePath);
		cmdList.add("of=" + targetPath);
		if (count != null) {
			cmdList.add("count=" + count);
		}
		if (bs != null) {
			cmdList.add("bs=" + bs + "k");
		}
		if (count != null) {
			cmdList.add("skip=" + skip);
		}
		
		String[] cmd = new String[cmdList.size()];
		cmdList.toArray(cmd);
		int rc = SystemUtil.runAndGetCode(cmd);
		return rc == 0 ? true : false;
	}
	
	
    public static DdBlockModel getDdBlockInfo(long size)
    {
        Long blockSize = 2097152l;// 暂定这个数(2M)
        DdBlockModel model = new DdBlockModel();
        model.setBs(blockSize);
        model.setCount(size / blockSize);
        if (size % blockSize != 0)
        {
            model.setCount(model.getCount() + 1);
        }

        return model;
    }
}
