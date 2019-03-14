package com.lockdown.messaging.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClusterServerContext<T extends ClusterProperties> extends AbstractServerContext<T> {


    private Logger logger = LoggerFactory.getLogger(getClass());

    public ClusterServerContext(T properties) {
        super(properties);
    }


}
