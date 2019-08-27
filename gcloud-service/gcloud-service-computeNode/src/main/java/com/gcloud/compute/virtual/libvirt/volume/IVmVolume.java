package com.gcloud.compute.virtual.libvirt.volume;

import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.storage.model.VmVolumeDetail;
import org.dom4j.Document;

public interface IVmVolume {

	void addDiskDevice(Document doc, VmVolumeDetail volumeInfo, boolean isCreateElement);

	DiskProtocol disProtocol();

}
