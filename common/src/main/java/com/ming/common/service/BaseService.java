package com.ming.common.service;

import java.util.List;

/**
 * Created by 2019-06-05
 */
public interface BaseService<T> {

    /**
     * 查询
     */
    T selectOne(T entity);

    /**
     * 通过Id查询
     */
    T selectById(Object id);

    /**
     * 根据ID集合来查询
     */
//    List<T> selectListByIds(List<Object> ids);

    /**
     * 查询列表
     */
    List<T> selectList(T entity);


    /**
     * 获取所有对象
     */
    List<T> selectListAll();


    /**
     * 查询总记录数
     */
//    Long selectCountAll();

    /**
     * 查询总记录数
     */
    Long selectCount(T entity);

    /**
     * 添加
     */
    void insert(T entity);


    /**
     * 插入 不插入null字段
     */
    void insertSelective(T entity);

    /**
     * 删除
     */
    void delete(T entity);

    /**
     * 根据Id删除
     */
    void deleteById(Object id);


    /**
     * 根据id更新
     */
    void updateById(T entity);


    /**
     * 不update null
     */
    void updateSelectiveById(T entity);

    /**
     * 根据ID集合批量删除
     */
//    void deleteBatchByIds(List<Object> ids);


    /**
     * 批量更新
     */
//    void updateBatch(List<T> entitys);
}
