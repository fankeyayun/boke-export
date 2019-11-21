package com.example.export.base;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate4.HibernateTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T>{
    public T save(T t) throws Exception;

    public T add(T t) throws Exception;

    public void saveOrUpdate(T t) throws Exception;

    public void update(T t) throws Exception;

    public void merge(T t) throws Exception;

    public void delete(T t) throws Exception;

    public T get(Class<T> entityClass, Serializable id);

    public void deleteById(Serializable id) throws Exception;

    public T findById(Serializable id) throws Exception;

    public List<T> find(String hql, String[] paramNames, Object[] values);

    public List find(final String hql, final Map<String, Object> param, final int pageNo,
                     final int pageSize);

    public List<T> find(final String hql, final Map<String, Object> param);

    public Object getById(Class c, Serializable id);

    public List findHql(final String hql, final Map<String, Object> param);

    public int updateHql(String hql);

    public List<T> find(String hql);

    public List findHql(String hql);

    public HibernateTemplate getTemplate();

    public List myFind(final String hql, final Map<String, Object> param, final int pageNo,
                       final int pageSize);

    public int total(final String hql, final Map<String, Object> param);


    public Long getTotalCount(String entityName);

    public Criteria createCriteria(String entityName);

    public List<?> findByCriteria(DetachedCriteria criteria, Integer page , Integer limit);

    public List<?> findByCriteria(DetachedCriteria criteria);

    public Session getSession();

    public Object findId(final String hql, final Map<String, Object> param);

}
