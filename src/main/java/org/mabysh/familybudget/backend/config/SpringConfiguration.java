package org.mabysh.familybudget.backend.config;

import java.util.Properties;

import org.mabysh.familybudget.backend.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vaadin.spring.annotation.EnableVaadin;

@Configuration
@ComponentScan(basePackages={"org.mabysh.familybudget"})
@EnableTransactionManagement
@EnableVaadin
public class SpringConfiguration {
	
	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://localhost:3306/family_budget_db?serverTimezone=UTC");
		dataSource.setUsername("budget");
		dataSource.setPassword("budget");
		return dataSource;
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
		emFactory.setDataSource(dataSource());
		emFactory.setJpaVendorAdapter(vendorAdapter());
		emFactory.setPersistenceUnitName("family_budget_db");
		emFactory.setJpaDialect(jpaDialect());
		emFactory.setPersistenceUnitManager(unitManager());
		Properties jpaProp = new Properties();
		jpaProp.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		jpaProp.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		jpaProp.setProperty("hibernate.show_sql", "true");
		emFactory.setJpaProperties(jpaProp);
		return emFactory;
	}
	
	@Bean 
	public DefaultPersistenceUnitManager unitManager() {
		DefaultPersistenceUnitManager unitManager = new DefaultPersistenceUnitManager();
		unitManager.setDefaultDataSource(dataSource());
		unitManager.setDefaultPersistenceUnitName("family_budget_db");
		return unitManager;
	}
	
	@Bean
	public HibernateJpaVendorAdapter vendorAdapter() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(true);
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLInnoDBDialect");
		return vendorAdapter;
	}
	
	@Bean
	public HibernateJpaDialect jpaDialect() {
		return new HibernateJpaDialect();
	}
	
	@Bean(name = "entityManager")
	public SharedEntityManagerBean entityManager() {
		SharedEntityManagerBean entityManager = new SharedEntityManagerBean();
		entityManager.setEntityManagerFactory(entityManagerFactory().getObject());
		entityManager.setPersistenceUnitName("family_budget_db");
		return entityManager;
	}
	
	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
	
	@Bean
	public AccountService accountService() {
		return new AccountService();
	}
}