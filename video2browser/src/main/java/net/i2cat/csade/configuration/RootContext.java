package net.i2cat.csade.configuration;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@ComponentScan(basePackages={"net.i2cat.csade.repositories", "net.i2cat.csade.services"})
@PropertySource(value= {"classpath:video2browser.properties","db.properties"})
@EnableTransactionManagement
@EnableScheduling
public class RootContext {
	@Autowired	private Environment env;
	private static final Logger log = LoggerFactory.getLogger(RootContext.class);

	
	public RootContext() {
		log.info("Initializing Root Context...");
		
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactoryBean() {
		LocalSessionFactoryBean result = new LocalSessionFactoryBean();
		result.setDataSource(getDataSource());
		result.setHibernateProperties(hibernateProperties());
		result.setPackagesToScan("net.i2cat.csade.models");
		return result;
	}

	@Bean
	public Properties hibernateProperties() {
		Properties p = new Properties();
		p.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		p.put("hibernate.hbm2ddl.auto",	env.getProperty("hibernate.hbm2ddl.auto"));
		p.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		return p;
	}

	@Bean
	public DataSource getDataSource() {
		BoneCPDataSource ds = new BoneCPDataSource();
		ds.setDriverClass(env.getProperty("db.driver"));
		ds.setJdbcUrl(env.getProperty("db.url"));
		ds.setUsername(env.getProperty("db.user"));
		ds.setPassword(env.getProperty("db.pass"));
		ds.setIdleConnectionTestPeriod(60, TimeUnit.MINUTES);
		ds.setIdleMaxAge(240, TimeUnit.MINUTES);
		ds.setMaxConnectionsPerPartition(30);
		ds.setMinConnectionsPerPartition(10);
		ds.setPartitionCount(3);
		ds.setAcquireIncrement(5);
		ds.setStatementsCacheSize(100);
		return ds;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
//		txManager.setSessionFactory(getSessionFactory());
		txManager.setSessionFactory(sessionFactoryBean().getObject());
		txManager.setDataSource(getDataSource());
		return txManager;
	}


}
