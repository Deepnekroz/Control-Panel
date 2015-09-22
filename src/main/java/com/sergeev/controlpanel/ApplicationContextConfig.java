package com.sergeev.controlpanel;

import com.sergeev.controlpanel.model.Component;
import com.sergeev.controlpanel.model.ComponentType;
import com.sergeev.controlpanel.model.Node;
import com.sergeev.controlpanel.model.dao.NodeDaoImpl;
import com.sergeev.controlpanel.model.dao.NodeDaoInterface;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by dmitry-sergeev on 03.09.15.
 */

@Configuration
@ComponentScan("com.sergeev.controlpanel")
@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationContextConfig {
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/controlpanel?useUnicode=yes&characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("12345678");

        return dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {

        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);

        sessionBuilder.addAnnotatedClasses(Node.class, Component.class, ComponentType.class);
        sessionBuilder.scanPackages("com.sergeev.controlpanel.model");
        sessionBuilder.addProperties(getHibernateProperties());
        sessionBuilder.setProperty("hibernate.connection.CharSet", "utf8");
        sessionBuilder.setProperty("hibernate.connection.characterEncoding", "utf8");
        sessionBuilder.setProperty("hibernate.connection.useUnicode", "true");

        return sessionBuilder.buildSessionFactory();
    }
    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);

        return transactionManager;
    }

    @Autowired
    @Bean(name = "nodeDao")
    public NodeDaoInterface getUserDao(SessionFactory sessionFactory) {
        return new NodeDaoImpl(sessionFactory);
    }



    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
}
