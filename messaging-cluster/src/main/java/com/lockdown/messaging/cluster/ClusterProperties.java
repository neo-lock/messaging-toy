package com.lockdown.messaging.cluster;

import java.util.regex.Pattern;

public class ClusterProperties implements ServerProperties {


    private int nodePort;
    private ServerDestination master;
    private int bossThreads;
    private int workerThreads;
    private boolean monitorEnable;
    private int monitorSeconds;
    private boolean enableSync;
    private String nodeWhiteList;



    @Override
    public String getNodeWhiteList() {
        return nodeWhiteList;
    }

    public void setNodeWhiteList(String nodeWhiteList) {
        this.nodeWhiteList = nodeWhiteList;
    }

    @Override
    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public ServerDestination getMaster() {
        return master;
    }

    public void setMaster(ServerDestination master) {
        this.master = master;
    }

    @Override
    public int getBossThreads() {
        return bossThreads;
    }

    public void setBossThreads(int bossThreads) {
        this.bossThreads = bossThreads;
    }

    @Override
    public int getWorkerThreads() {
        return workerThreads;
    }

    public void setWorkerThreads(int workerThreads) {
        this.workerThreads = workerThreads;
    }

    public boolean isMonitorEnable() {
        return monitorEnable;
    }

    public void setMonitorEnable(boolean monitorEnable) {
        this.monitorEnable = monitorEnable;
    }

    public int getMonitorSeconds() {
        return monitorSeconds;
    }

    public void setMonitorSeconds(int monitorSeconds) {
        this.monitorSeconds = monitorSeconds;
    }

    @Override
    public boolean isEnableSync() {
        return enableSync;
    }

    public void setEnableSync(boolean enableSync) {
        this.enableSync = enableSync;
    }
}
