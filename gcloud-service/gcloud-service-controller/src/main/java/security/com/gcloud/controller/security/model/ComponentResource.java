package com.gcloud.controller.security.model;

public class ComponentResource {

    private int core;
    private int memory;

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int increaseCore(int inc){
        core += inc;
        return core;
    }

    public int increaseMemory(int inc){
        memory += inc;
        return memory;
    }
}
