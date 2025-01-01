package com.github.im2back.validation.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(
        basePackages = "com.github.im2back.validation.repository.stock",
        entityManagerFactoryRef = "dbstockEntityManager",
        transactionManagerRef = "dbstockTransactionManager"
)
@Configuration
public class DbStockConfig {
	
	@Primary
    @Bean(name = "dbstockDataSource")
    @ConfigurationProperties(prefix = "dbstock.datasource")
    public DataSourceProperties dbstockDataSource() {
        return new DataSourceProperties();
    }

  
    @Bean(name = "dbstockEntityManager")
    public LocalContainerEntityManagerFactoryBean dbstockEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dbstockDataSource().initializeDataSourceBuilder().build());
        em.setPackagesToScan("com.github.im2back.validation.entities.product");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        em.setJpaVendorAdapter(vendorAdapter);

        return em;
    }
    
    @Primary
    @Bean
    public PlatformTransactionManager dbstockTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dbstockEntityManager().getObject());

        return transactionManager;
    }

}
