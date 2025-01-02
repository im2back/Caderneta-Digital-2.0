package com.github.im2back.validation.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(
        basePackages = "com.github.im2back.validation.repository.customer",
        entityManagerFactoryRef = "dbcustomerEntityManager",
        transactionManagerRef = "dbcustomerTransactionManager"
)
@Configuration
public class DbCustomerConfig {
	

    @Bean(name = "dbcustomerDataSource")
    @ConfigurationProperties(prefix = "dbcustomer.datasource")
    public DataSourceProperties dbcustomerDataSource() {
        return new DataSourceProperties();
    }

  
    @Bean(name = "dbcustomerEntityManager")
    public LocalContainerEntityManagerFactoryBean dbcustomerEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dbcustomerDataSource().initializeDataSourceBuilder().build());
        em.setPackagesToScan("com.github.im2back.validation.entities.customer");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }

    @Bean
    public PlatformTransactionManager dbcustomerTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dbcustomerEntityManager().getObject());

        return transactionManager;
    }

}
