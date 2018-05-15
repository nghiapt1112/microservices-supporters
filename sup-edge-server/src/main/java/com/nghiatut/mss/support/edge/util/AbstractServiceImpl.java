package com.nghiatut.mss.support.edge.util;

import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class AbstractServiceImpl extends com.nghia.libraries.commons.mss.infrustructure.service.AbstractServiceImpl {

    public URI getServicePATH(String serviceId, String fallBack) {
        URI uri = super.getServiceURL(serviceId, fallBack);
        return uri;
    }
}
