package com.example.export.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * 配置hibernate
 */

@Configuration
@EntityScan(basePackages="com.example.export.modu")
public class HibernateConfig {
    @Autowired
    private DataSource dataSource;



    @Bean
    public LocalSessionFactoryBean factoryBean(){
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan(new String[]{"com.example.export.modu"});
        factoryBean.setHibernateProperties(hibernateProperties());
        SessionFactory object = factoryBean.getObject();
        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager(){
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setDataSource(this.dataSource);
        manager.setSessionFactory(factoryBean().getObject());
        return manager;
    }


    @Bean
    public HibernateTemplate hibernateTemplate(){
        HibernateTemplate hibernateTemplate = new HibernateTemplate();
        hibernateTemplate.setSessionFactory(factoryBean().getObject());
        return hibernateTemplate;
    }


    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.hbm2ddl.auto", "none");
                setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
                setProperty("hibernate.globally_quoted_identifiers","false");
                setProperty("hibernate.show_sql" , "false");
                setProperty("hibernate.current_session_context_class" , "org.springframework.orm.hibernate4.SpringSessionContext");
                setProperty("ENABLE_LAZY_LOAD_NO_TRANS" , "true");
                setProperty("hibernate.enable_lazy_load_no_trans" , "true");
            }
        };
    }
}
