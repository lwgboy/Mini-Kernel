
package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.storage.model.DescribeStoragePoolsResponse;
import com.gcloud.header.storage.model.StoragePoolModel;

import java.util.List;

public class ApiDescribeStoragePoolsReplyMsg extends PageReplyMessage<StoragePoolModel> {

    private static final long serialVersionUID = 1L;

    private DescribeStoragePoolsResponse pools;

    @Override
    public void setList(List<StoragePoolModel> list) {
        this.pools = new DescribeStoragePoolsResponse();
        this.pools.setStoragePools(list);

    }

    public DescribeStoragePoolsResponse getPools() {
        return pools;
    }

    public void setPools(DescribeStoragePoolsResponse pools) {
        this.pools = pools;
    }

}
