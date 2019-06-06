package com.ming.zuul.gateway.fallback;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Ming
 * @data: Create on 2019-06-05.
 */
@Service
@Log4j2
public class UserServiceFallback implements IUserService {
    @Override
    public List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username) {
        log.error("调用{}异常{}","getPermissionByUsername",username);
        return null;
    }

    @Override
    public List<PermissionInfo> getAllPermissionInfo() {
        log.error("调用{}异常","getPermissionByUsername");
        return null;
    }
}