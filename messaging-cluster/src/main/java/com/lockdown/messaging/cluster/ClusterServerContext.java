package com.lockdown.messaging.cluster;

import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterServerContext<T extends ClusterProperties> extends AbstractServerContext<T> {


    private Logger logger = LoggerFactory.getLogger(getClass());

    public ClusterServerContext(T properties) {
        super(properties);
    }

    @Override
    protected void checkProperties() {
        if (StringUtil.isNullOrEmpty(getProperties().getNodeWhiteList())) {
            throw new IllegalArgumentException(" node white list can't be empty !");
        }
    }


    @Override
    protected void initNecessary() {

    }

}
