package next.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import next.aop.PerformanceAspect;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
@ComponentScan(basePackages = { "next.service", "next.dao", "next.aop" })
@PropertySource("classpath:/application.properties")
public class AppConfig {
	@Autowired
	private Environment env;

	@Value("${db.driver}")
	private String DB_DRIVER;
	@Value("${db.url}")
	private String DB_URL;
	@Value("${db.username}")
	private String DB_USERNAME;
	@Value("${db.pw}")
	private String DB_PW;

	// private String DB_DRIVER;
	// private String DB_URL;
	// private String DB_USERNAME;
	// private String DB_PW;
	//
	@Bean
	public DataSource dataSource() {

		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(DB_DRIVER);
		ds.setUrl(DB_URL);
		ds.setUsername(DB_USERNAME);
		ds.setPassword(DB_PW);
		return ds;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
