package kr.ac.skhu.project.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "kr.ac.skhu.project.repository", // TODO Repository 패키지 지정
		transactionManagerRef = "mysqlTransactionManager", entityManagerFactoryRef = "mysqlFactoryBean")
public class DBConfig {

	@Bean(name = "mysqlDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource getDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "mysqlFactoryBean")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("mysqlDataSource") DataSource dataSource) {
		Map<String, String> map = new HashMap<>();
		map.put("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
		map.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		return builder.dataSource(dataSource).packages("kr.ac.skhu.project.domain") // TODO Model 패키지 지정
				.properties(map).build();
	}

	@Bean(name = "mysqlTransactionManager")
	PlatformTransactionManager transactionManager(
			@Qualifier("mysqlFactoryBean") EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}

}