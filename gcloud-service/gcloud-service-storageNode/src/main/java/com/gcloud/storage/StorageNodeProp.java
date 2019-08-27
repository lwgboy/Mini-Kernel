
package com.gcloud.storage;

import com.gcloud.storage.service.impl.ReportStoragePoolModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "gcloud.storage-node")
public class StorageNodeProp {

    private Boolean reportPools;

    private List<ReportStoragePoolModel> pools;

    private String cachedImagesPath = "/var/lib/glance/images/";

}
