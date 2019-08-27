package com.gcloud.compute.virtual.libvirt.volume;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.common.Host;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.storage.model.VmVolumeDetail;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("VmVolumeRbdImpl")
public class VmVolumeRbdImpl implements IVmVolume {

	@Autowired
	private ComputeNodeProp computeNodeProp;


	@Override
	public DiskProtocol disProtocol() {
		return DiskProtocol.RBD;
	}

	@Override
	public void addDiskDevice(Document doc, VmVolumeDetail vmDisk, boolean isCreateElement) {
		Element disk = null;
		if (!isCreateElement) {
			Element devices = (Element) doc.selectSingleNode("/domain/devices");
			disk = devices.addElement("disk");
		} else {
			disk = doc.addElement("disk");
		}

		disk.addAttribute("type", "network");
		disk.addAttribute("device", "disk");

		Element driver = disk.addElement("driver");
		driver.addAttribute("name", "qemu");
		driver.addAttribute("type", vmDisk.getFileFormat());

		String diskCacheType = computeNodeProp.getRbdDiskCacheType();
		if (StringUtils.isNotBlank(diskCacheType)) {
			driver.addAttribute("cache", diskCacheType);
		} else {
			Attribute cacheAtt = driver.attribute("cache");
			if (cacheAtt != null) {
				driver.remove(cacheAtt);
			}
		}

		Element auth = disk.addElement("auth");
		auth.addAttribute("username", computeNodeProp.getRbdUserName());
		Element secret = auth.addElement("secret");
		secret.addAttribute("type", "ceph");
		secret.addAttribute("uuid", computeNodeProp.getRbdSecretUuid());

		Element source = disk.addElement("source");
		source.addAttribute("protocol", "rbd");
		source.addAttribute("name", vmDisk.getSourcePath());


		List<Host> monHosts = VmNodeUtil.getCephMon();
		if(monHosts == null || monHosts.size() == 0){
			throw new GCloudException("::获取不到monitor");
		}

		for(Host mon : monHosts){
			Element monE = source.addElement("host");
			monE.addAttribute("name", mon.getHostname());
			monE.addAttribute("port", mon.getPort());
		}

		Element target = disk.addElement("target");
		target.addAttribute("dev", vmDisk.getTargetDev());
		target.addAttribute("bus", vmDisk.getBusType());

		Element serial = disk.addElement("serial");
		serial.setText(vmDisk.getVolumeRefId());

	}

}
