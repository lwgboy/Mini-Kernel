package com.gcloud.compute.virtual.libvirt.volume;

import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.compute.enums.DiskProtocol;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class VmVolumeImpls {

    private static Map<DiskProtocol, IVmVolume> impls = new HashMap<>();

    @PostConstruct
    public void init(){
        for(IVmVolume volume : SpringUtil.getBeans(IVmVolume.class)){
            impls.put(volume.disProtocol(), volume);
        }
    }

    public static IVmVolume vmVolumeImpl(DiskProtocol diskProtocol){
        return impls.get(diskProtocol);
    }

}
