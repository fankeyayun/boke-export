package com.example.export.base;

import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BaseDaoImpl<T>implements BaseDao<T> {

    @Autowired
    private HibernateTemplate hibernateTemplate;


    protected SessionFactory sessionFactory;


    @Override
    @Transactional(readOnly = false)
    public T save(T t) throws Exception {
        hibernateTemplate.save(t);
        return t;
    }

    @Override
    @Transactional(readOnly = false)
    public T add(T t) throws Exception {
        hibernateTemplate.save(t);
        return t;
    }
    @Override
    @Transactional
    public void saveOrUpdate(T t) throws Exception {
        hibernateTemplate.saveOrUpdate(t);
    }

    @Override
    @Transactional
    public void update(T t) throws Exception {
        hibernateTemplate.update(t);
    }


    @Override
    @Transactional
    public void merge(T t) throws Exception {
        hibernateTemplate.merge(t);
    }


    @Override
    @Transactional
    public void delete(T t) throws Exception {
        hibernateTemplate.delete(t);
    }

    @Override
    @Transactional
    public T get(Class<T> entityClass, Serializable id) {
        T ret = (T) hibernateTemplate.get(entityClass, id);
        return ret;
    }

    @Override
    @Transactional
    public void deleteById(Serializable id) throws Exception {
        T ret = this.get(this.getEntityClass(), id);
        if (ret != null){
            delete(ret);
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public T findById(Serializable id) throws Exception {
        return (T)hibernateTemplate.get(getEntityClassName(), id);
    }

    @Transactional
    protected Integer executeUpdate(final String hql, final Object ... objects){
        return hibernateTemplate.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                Query createQuery = createQuery(session, hql, objects);
                return createQuery.executeUpdate();
            }
        });
    }

    @Transactional
    protected Class<T> getEntityClass(){
        @SuppressWarnings("unchecked")
        Class<T> entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return entityClass;
    }

    @Transactional
    public Type getPkType() {
        ClassMetadata meta = hibernateTemplate.getSessionFactory().getClassMetadata(getEntityClass());
        return meta.getIdentifierType();
    }


    @Transactional
    public String getPkColunmName(){
        ClassMetadata meta = hibernateTemplate.getSessionFactory().getClassMetadata(getEntityClass());
        return meta.getIdentifierPropertyName();
    }

    @Transactional
    protected String getEntityClassName() {
        ClassMetadata meta = hibernateTemplate.getSessionFactory().getClassMetadata(getEntityClass());
        return meta.getEntityName();
    }

    @Transactional
    protected Query createQuery(Session session, String hql, Object... objects) {
        System.out.println(hql);
        Query query = session.createQuery(hql);
        if (objects != null){
            for (int i = 0; i < objects.length; i++) {
                query.setParameter(i, objects[i]);
            }
        }
        return query;
    }

    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public List<T> find(String hql, String[] paramNames, Object[] values) {
        List<T> ret = null;
        ret = (List<T>)hibernateTemplate.findByNamedParam(hql, paramNames,values);
        return ret;
    }


    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public List find(final String hql, final Map<String, Object> param, final int pageNo,
                     final int pageSize) {
        return hibernateTemplate.execute(new HibernateCallback<List>() {
            @Override
            public List doInHibernate(Session session) throws HibernateException {
                String shql=hql;
//                if (pageNo == 0&&pageSize!=0) {
//                    String sql = "select count(*) ";
//                    shql = sql + hql;
//                }
                Query query = session.createQuery(shql);
                if (param != null && param.size() > 0) {
                    for (String property : param.keySet()) {
                        if (param.get(property) instanceof List){
                            query.setParameterList(property, (Collection)param.get(property));
                        }else{
                            query.setParameter(property,
                                    param.get(property));
                        }
                    }
                }
                if (pageNo != 0 && pageSize != 0) {
                    query.setFirstResult((pageNo - 1) * pageSize);
                    query.setMaxResults(pageSize);
                }
                List list = null;
                try{
                    list = query.list();
                }catch (HibernateException e) {
                    e.printStackTrace();
                    throw e;
                }
                return list;
            }
        });
    }


    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public T findId(final String hql, final Map<String, Object> param) {
        return (T) hibernateTemplate.execute(new HibernateCallback() {
            @Override
            public T doInHibernate(Session session) throws HibernateException {
                String shql=hql;

                Query query = session.createQuery(shql);
                if (param != null && param.size() > 0) {
                    for (String property : param.keySet()) {
                        if (param.get(property) instanceof List){
                            query.setParameterList(property, (Collection)param.get(property));
                        }else{
                            query.setParameter(property,
                                    param.get(property));
                        }
                    }
                }

                List list = null;
                try{
                    list = query.list();

                }catch (HibernateException e) {
                    e.printStackTrace();
                    throw e;
                }
                if(list.size()>0&&list!=null) {
                    return (T) list.get(0);
                }
                return null;

            }
        });
    }

    @Transactional
    @Override
    public List myFind(final String hql, final Map<String, Object> param, final int pageNo,
                       final int pageSize) {
        return hibernateTemplate.execute(new HibernateCallback<List>() {
            @Override
            public List doInHibernate(Session session) throws HibernateException {
                String shql= "from " + hql;
//                if (pageNo == 0&&pageSize!=0) {
//                    String sql = "select count(e) ";
//                    shql = sql + hql;
//                }
                Query query = session.createQuery(shql);
                if (param != null && param.size() > 0) {
                    for (String property : param.keySet()) {
                        if (param.get(property) instanceof List){
                            query.setParameterList(property, (Collection)param.get(property));
                        }else{
                            query.setParameter(property,
                                    param.get(property));
                        }
                    }
                }
                if (pageNo != 0 && pageSize != 0) {
                    query.setFirstResult((pageNo - 1) * pageSize);
                    query.setMaxResults(pageSize);
                }
                List list = null;
                try{
                    list = query.list();
                }catch (HibernateException e) {
                    e.printStackTrace();
                    throw e;
                }
                return list;
            }
        });
    }

    @Transactional
    @Override
    public int total(String hql, final Map<String, Object> param) {
        return (int) hibernateTemplate.execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                String shql= "from " + hql;
                Query query = session.createQuery(shql);
                if (param != null && param.size() > 0) {
                    for (String property : param.keySet()) {
                        if (param.get(property) instanceof List){
                            query.setParameterList(property, (Collection)param.get(property));
                        }else{
                            query.setParameter(property,
                                    param.get(property));
                        }
                    }
                }
                int i = 0;
                try{
                    i = Integer.parseInt(query.list().get(0).toString());
                }catch (HibernateException e) {
                    e.printStackTrace();
                    throw e;
                }
                return i;
            }
        });
    }

    @Override
    @Transactional
    public Long getTotalCount(String entityName) {
        String hql = "select count(*) " + entityName ;
        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        return (Long) session.createQuery(hql).uniqueResult();
    }

    @Override
    @Transactional
    public Criteria createCriteria(String entityName) {
        return this.getTemplate().getSessionFactory().getCurrentSession().createCriteria(entityName);
    }

    @Override
    @Transactional
    public List<?> findByCriteria(DetachedCriteria criteria, Integer page , Integer limit) {
        return this.getTemplate().findByCriteria(criteria,(page-1)*limit,limit);
    }

    @Override
    @Transactional
    public List<?> findByCriteria(DetachedCriteria criteria) {
        return this.getTemplate().findByCriteria(criteria);
    }


    @Override
    @Transactional
    public Session getSession(){
        return this.getTemplate().getSessionFactory().getCurrentSession();
    }


    @Override
    @Transactional
    public List<T> find(final String hql,final Map<String, Object> param) {
        Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        if (param != null && param.size() > 0) {
            for (String property : param.keySet()) {
                query.setParameter(property,param.get(property));
            }
        }
        return query.list();
    }


    @Transactional
    @Override
    public Object getById(Class c, Serializable id) {
        Object ret = hibernateTemplate.get(c, id);
        return ret;
    }


    @Transactional
    @Override
    public List findHql(final String hql, final Map<String, Object> param) {
        return hibernateTemplate.execute(new HibernateCallback<List>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hql);
                if (param != null && param.size() > 0) {
                    for (String property : param.keySet()) {
                        query.setParameter(property,param.get(property));
                    }
                }
                return query.list();
            }
        });
    }


    @Transactional
    @Override
    public int updateHql(String hql) {
        return executeUpdate(hql, null);
    }


    @Transactional
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    @Transactional
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    @Transactional
    public List<T> find(String hql) {
        List<T> list = this.find(hql, null);
        return list;
    }


    @Override
    @Transactional
    public List findHql(String hql) {
        List list = findHql(hql, null);
        return list;
    }

    @Override
    public HibernateTemplate getTemplate() {
        return this.hibernateTemplate;
    }
}
