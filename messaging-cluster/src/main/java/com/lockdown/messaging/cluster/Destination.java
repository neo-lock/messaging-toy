package com.lockdown.messaging.cluster;

import java.io.Serializable;

public interface Destination extends Serializable {

    public String identifier();

}
