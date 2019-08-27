package com.gcloud.compute.virtual.libvirt.volume;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.storage.model.VmVolumeDetail;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("VmVolumeFileImpl")
public class VmVolumeFileImpl implements IVmVolume {

	@Autowired
	private ComputeNodeProp computeNodeProp;

	@Override
	public DiskProtocol disProtocol() {
		return DiskProtocol.FILE;
	}

	@Override
	public void addDiskDevice(Document doc, VmVolumeDetail vmDisk, boolean isCreateElement) {
		Element diskE = null;
		if (!isCreateElement) {
			Element devices = (Element) doc.selectSingleNode("/domain/devices");
			diskE = devices.addElement("disk");
		} else {
			diskE = doc.addElement("disk");
		}

		diskE.addAttribute("device", "disk");
		diskE.addAttribute("type", "file");

		Element driverE = diskE.addElement("driver");
		driverE.addAttribute("name", "qemu");
		driverE.addAttribute("type", vmDisk.getFileFormat());

		String diskCacheType = computeNodeProp.getFileDiskCacheType();
		if (StringUtils.isNotBlank(diskCacheType)) {
			driverE.addAttribute("cache", diskCacheType);
		} else {
			Attribute cacheAtt = driverE.attribute("cache");
			if (cacheAtt != null) {
				driverE.remove(cacheAtt);
			}
		}

		Element sourceE = diskE.addElement("source");
		sourceE.addAttribute("file", vmDisk.getSourcePath());

		Element targetE = diskE.addElement("target");
		targetE.addAttribute("dev", vmDisk.getTargetDev());
		targetE.addAttribute("bus", "virtio");

	}

}
