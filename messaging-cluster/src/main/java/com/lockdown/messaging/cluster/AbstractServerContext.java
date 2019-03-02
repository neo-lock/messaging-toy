package com.lockdown.messaging.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public abstract class AbstractServerContext<T extends ServerProperties> implements ServerContext {


    protected final T properties;
    protected Pattern nodeWhiteList;
    private Logger logger = LoggerFactory.getLogger(getClass());


    public AbstractServerContext(T properties) {
        this.properties = properties;
        this.nodeWhiteList = Pattern.compile(this.properties.getNodeWhiteList());
        logger.info(" 使用的Properties {}", properties);
    }

    @Override
    public Pattern nodeWhiteList() {
        return this.nodeWhiteList;
    }

    @Override
    public ServerProperties getProperties() {
        return properties;
    }

}
