package com.gcloud.service.common.compute.model;

import java.util.ArrayList;
import java.util.List;

public class DomainDetail {
    //以下4项(除了emulator是表示vnc端口相关参数)
    private String remotePort;
    private String remoteType;
    private String emulator;
    private String listen;
    private Boolean autoPort;
    //以下4项用于表示spice相关参数
    private String spicePort;
    private String spiceType;
    private String spiceListen;

    private Integer memory;  //mb
    private Integer currentMemory;  //mb
    private Integer vcpu;

    private Boolean spiceAutoPort;
    private List<DomainDisk> domainDisks;
    private Boolean isPersistent;
    private String domState;
    private List<DomainInterface> domainInterfaces;
    private List<String> qemuArgs = new ArrayList<String>();

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public String getRemoteType() {
        return remoteType;
    }

    public void setRemoteType(String remoteType) {
        this.remoteType = remoteType;
    }

    public String getListen() {
        return listen;
    }

    public void setListen(String listen) {
        this.listen = listen;
    }

    public Boolean getAutoPort() {
        return autoPort;
    }

    public void setAutoPort(Boolean autoPort) {
        this.autoPort = autoPort;
    }

    public String getSpicePort() {
        return spicePort;
    }

    public void setSpicePort(String spicePort) {
        this.spicePort = spicePort;
    }

    public String getSpiceType() {
        return spiceType;
    }

    public void setSpiceType(String spiceType) {
        this.spiceType = spiceType;
    }

    public String getSpiceListen() {
        return spiceListen;
    }

    public void setSpiceListen(String spiceListen) {
        this.spiceListen = spiceListen;
    }

    public Boolean getSpiceAutoPort() {
        return spiceAutoPort;
    }

    public void setSpiceAutoPort(Boolean spiceAutoPort) {
        this.spiceAutoPort = spiceAutoPort;
    }

    public List<DomainDisk> getDomainDisks() {
        return domainDisks;
    }

    public void setDomainDisks(List<DomainDisk> domainDisks) {
        this.domainDisks = domainDisks;
    }

    public Boolean getIsPersistent() {
        return isPersistent;
    }

    public void setIsPersistent(Boolean isPersistent) {
        this.isPersistent = isPersistent;
    }

    public String getDomState() {
        return domState;
    }

    public void setDomState(String domState) {
        this.domState = domState;
    }

    public List<DomainInterface> getDomainInterfaces() {
        return domainInterfaces;
    }

    public void setDomainInterfaces(List<DomainInterface> domainInterfaces) {
        this.domainInterfaces = domainInterfaces;
    }

    public String getEmulator() {
        return emulator;
    }

    public void setEmulator(String emulator) {
        this.emulator = emulator;
    }

    public List<String> getQemuArgs() {
        return qemuArgs;
    }

    public void setQemuArgs(List<String> qemuArgs) {
        this.qemuArgs = qemuArgs;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getCurrentMemory() {
        return currentMemory;
    }

    public void setCurrentMemory(Integer currentMemory) {
        this.currentMemory = currentMemory;
    }

    public Integer getVcpu() {
        return vcpu;
    }

    public void setVcpu(Integer vcpu) {
        this.vcpu = vcpu;
    }
}
