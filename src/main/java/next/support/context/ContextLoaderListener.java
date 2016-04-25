package next.support.context;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;


@Component
public class ContextLoaderListener {
	private static final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);
	
	@Resource
	private DataSource datasource;
	
	@PostConstruct
	public void contextInitialized() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("jwp.sql"));
		DatabasePopulatorUtils.execute(populator, datasource);
//		DatabasePopulatorUtils.execute(populator, getDataSource());
		
		logger.info("Completed Load ServletContext!");
	}

}
