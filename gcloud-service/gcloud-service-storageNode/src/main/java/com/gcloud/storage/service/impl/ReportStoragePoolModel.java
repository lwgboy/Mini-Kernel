
package com.gcloud.storage.service.impl;

import lombok.Data;

@Data
public class ReportStoragePoolModel {

    private Integer provider;
    private String storageType;
    private String poolName;
    private String categoryCode;
    private String snapshotType;

}
