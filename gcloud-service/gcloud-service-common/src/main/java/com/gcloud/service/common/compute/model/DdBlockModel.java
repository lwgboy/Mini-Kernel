package com.gcloud.service.common.compute.model;

public class DdBlockModel
{
    private long count;// dd的块数
    private long bs;// dd每一块的大小
    public long getCount()
    {
        return count;
    }
    public void setCount(long count)
    {
        this.count = count;
    }
    public long getBs()
    {
        return bs;
    }
    public void setBs(long bs)
    {
        this.bs = bs;
    }
    
}
