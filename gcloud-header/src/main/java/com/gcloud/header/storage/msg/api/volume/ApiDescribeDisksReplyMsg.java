package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.storage.model.DiskItemType;
import com.gcloud.header.storage.model.DescribeDisksResponse;

import java.util.List;

/**
 * Created by yaowj on 2018/9/29.
 */
public class ApiDescribeDisksReplyMsg extends PageReplyMessage<DiskItemType> {

    private static final long serialVersionUID = 1L;

    private DescribeDisksResponse disks;

    @Override
    public void setList(List<DiskItemType> list) {
        disks = new DescribeDisksResponse();
        disks.setDisk(list);
    }

    public DescribeDisksResponse getDisks() {
        return disks;
    }

    public void setDisks(DescribeDisksResponse disks) {
        this.disks = disks;
    }
}
