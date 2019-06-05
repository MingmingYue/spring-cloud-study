package com.ming.common.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 2019-06-05
 */
@Getter
@Setter
@Builder
public class RequestInfo {
    private String name;
    private String id;
    private String hostIp;
}
