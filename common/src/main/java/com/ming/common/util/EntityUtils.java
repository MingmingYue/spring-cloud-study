package com.ming.common.util;

import com.ming.common.context.BaseContextHandler;
import com.ming.common.entity.RequestInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Date;

/**
 * Created by 2019-06-05
 */
public class EntityUtils {

    public static <T> void setCreatAndUpdateInfo(T entity) {
        setCreateInfo(entity);
        setUpdatedInfo(entity);
    }

    /**
     * 快速将bean的crtUser、crtHost、crtTime附上相关值
     */
    public static <T> void setCreateInfo(T entity) {
        RequestInfo requestInfo = getInfo();
        // 默认属性
        String[] fields = {"crtName", "crtUser", "crtHost", "crtTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "crtTime");
        // 默认值
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{requestInfo.getName(), requestInfo.getId(), requestInfo.getHostIp(), new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 快速将bean的updUser、updHost、updTime附上相关值
     */
    public static <T> void setUpdatedInfo(T entity) {
        RequestInfo requestInfo = getInfo();
        // 默认属性
        String[] fields = {"updName", "updUser", "updHost", "updTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "updTime");
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{requestInfo.getName(), requestInfo.getId(), requestInfo.getHostIp(), new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    private static RequestInfo getInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String hostIp = "";
        String name = "";
        String id = "";
        if (request != null) {
            hostIp = String.valueOf(request.getHeader("userHost"));
            name = String.valueOf(request.getHeader("userName"));
            name = URLDecoder.decode(name);
            id = String.valueOf(request.getHeader("userId"));
        }
        if (StringUtils.isBlank(name)) {
            name = BaseContextHandler.getUsername();
        }
        if (StringUtils.isBlank(id)) {
            id = BaseContextHandler.getUserID();
        }
        return RequestInfo.builder().hostIp(hostIp).id(id).name(name).build();
    }

    /**
     * 依据对象的属性数组和值数组对对象的属性进行赋值
     */
    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (ReflectionUtils.hasField(entity, field)) {
                ReflectionUtils.invokeSetter(entity, field, value[i]);
            }
        }
    }

    /**
     * 根据主键属性，判断主键是否值为空
     */
    public static <T> boolean isPKNotNull(T entity, String field) {
        if (!ReflectionUtils.hasField(entity, field)) {
            return false;
        }
        Object value = ReflectionUtils.getFieldValue(entity, field);
        return value != null && !"".equals(value);
    }
}
