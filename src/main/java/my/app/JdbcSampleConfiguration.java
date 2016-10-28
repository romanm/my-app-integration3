package my.app;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.jdbc.ExpressionEvaluatingSqlParameterSourceFactory;
import org.springframework.integration.jdbc.JdbcOutboundGateway;
import org.springframework.integration.jdbc.SqlParameterSourceFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.messaging.MessageHandler;

import my.app.domain.Person;
import my.app.domain.PersonMapper;

//@Configuration
//@EnableIntegration
//@IntegrationComponentScan
public class JdbcSampleConfiguration {
	@Autowired private DataSource dataSource;
	/*
	 * https://gist.github.com/artembilan/8d7d4cf6959c71be2cc9
	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:setup-tables.sql")
				.build();
	}
	 * */
	@Bean
	public RowMapper<Person> personResultMapper() {
		return new PersonMapper();
	}

	@Bean public DirectChannel createPersonRequestChannel() { return new DirectChannel(); }
	@Bean public DirectChannel createPersonReplyChannel() { return new DirectChannel(); }
	@Bean public DirectChannel findPersonRequestChannel() { return new DirectChannel(); }
	@Bean public DirectChannel findPersonReplyChannel() { return new DirectChannel(); }

	@MessagingGateway(name = "personService")
	public interface PersonServiceGateway {
		
		@Gateway(requestChannel = "createPersonRequestChannel"
				, replyChannel = "createPersonReplyChannel"
				, replyTimeout = 2, requestTimeout = 200)
		Person createPerson(Person person);

		@Gateway(requestChannel = "findPersonRequestChannel"
				, replyChannel = "findPersonReplyChannel"
				, replyTimeout = 2, requestTimeout = 200)
		List<Person> findPersonByName(String name);
	}

	private @Value("${sql.sample.person.insert}") String sqlSamplePersonInsert;
	
	@Bean
	@ServiceActivator(inputChannel = "findPersonRequestChannel", outputChannel = "findPersonReplyChannel")
	public MessageHandler personServiceFindPerson() {
		JdbcOutboundGateway gateway = new JdbcOutboundGateway(dataSource, 
				"UPDATE DUMMY SET DUMMY_VALUE='test'",
				"select * from Person where lower(name)=lower(:payload)");
		gateway.setRowMapper(personResultMapper());
		gateway.setMaxRowsPerPoll(100);		
		return gateway;
	}
	@Bean
	@ServiceActivator(inputChannel = "createPersonRequestChannel", outputChannel = "createPersonReplyChannel")
	public MessageHandler personServiceCreatePerson() {
//		JdbcOutboundGateway gateway = new JdbcOutboundGateway(dataSource(), 
		JdbcOutboundGateway gateway = new JdbcOutboundGateway(dataSource, 
				"insert into Person (name,gender,dateOfBirth) values(:name,:gender,:dateOfBirth)",
				"select * from Person where id = :id");
		gateway.setRowMapper(personResultMapper());
		gateway.setKeysGenerated(true);
		gateway.setRequestSqlParameterSourceFactory(requestSource());
		gateway.setReplySqlParameterSourceFactory(replySource());
		return gateway;
	}

	@Bean
	public SqlParameterSourceFactory replySource() {
		ExpressionEvaluatingSqlParameterSourceFactory sourceFactory = new ExpressionEvaluatingSqlParameterSourceFactory();
		sourceFactory.setParameterExpressions(Collections.singletonMap("id", "#this['SCOPE_IDENTITY()']"));
		return sourceFactory;
	}
	
	@Bean
	public SqlParameterSourceFactory requestSource() {
		ExpressionEvaluatingSqlParameterSourceFactory sourceFactory = new ExpressionEvaluatingSqlParameterSourceFactory();
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", "payload.name.toUpperCase()");
		params.put("gender", "payload.gender.identifier");
		params.put("dateOfBirth", "payload.dateOfBirth");
		sourceFactory.setParameterExpressions(params);
		return sourceFactory;
	}
}
