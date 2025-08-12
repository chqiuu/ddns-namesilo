package com.chqiuu.ddnsnamesilo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResourceRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mainDomain;
    private String recordId;
    private String type;
    private String host;
    private String value;
    private String ttl;
    private String distance;
}
