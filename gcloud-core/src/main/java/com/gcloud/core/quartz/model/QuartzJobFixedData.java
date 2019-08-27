package com.gcloud.core.quartz.model;

import java.io.Serializable;

public class QuartzJobFixedData implements Serializable {
	private static final long serialVersionUID = 1L;
	
//	private JobFixedType jobFixedType;
	private String jobFixedType;
    private long interval;

    public QuartzJobFixedData(String jobFixedType, long interval) {
        this.jobFixedType = jobFixedType;
        this.interval = interval;
    }

    public QuartzJobFixedData() {
    }


    public String getJobFixedType() {
		return jobFixedType;
	}

	public void setJobFixedType(String jobFixedType) {
		this.jobFixedType = jobFixedType;
	}

	public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
