package my.app;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.support.Consumer;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class FileToFileIntegrationConfig {

	//------------------spring-boot-sample-integration------------------------
	@Bean
	public FileReadingMessageSource fileReader() {
		FileReadingMessageSource reader = new FileReadingMessageSource();
		reader.setDirectory(new File("target/input"));
		return reader;
	}
	@Bean
	public FileWritingMessageHandler fileWriter() {
		FileWritingMessageHandler writer = new FileWritingMessageHandler(
				new File("target/output"));
		writer.setExpectReply(false);
		return writer;
	}
	@Bean public DirectChannel inputChannel() { return new DirectChannel(); }
	@Bean public DirectChannel outputChannel() { return new DirectChannel(); }

	@Bean
	public IntegrationFlow integrationFlow(HelloWorldFasadeOrEndpoint endpoint) {
		// почати з читування файла з заданим періодом
		IntegrationFlowBuilder from = IntegrationFlows.from(fileReader(), new FixedRatePoller());
		// передати по каналу в точку призначення, на фасад сервісу
		IntegrationFlowBuilder handle2 = from .channel(inputChannel()).handle(endpoint);
		// добавити канал виходу
		IntegrationFlowBuilder channel = handle2.channel(outputChannel());
		// по виходу з каналу віддати роботи відповідному обробнику Mesages
		IntegrationFlowBuilder handle = channel.handle(fileWriter());
		StandardIntegrationFlow standardIntegrationFlow = handle.get();
		return standardIntegrationFlow;
	}

	/**
	 * @author roman
	 * як часто
	 */
	private static class FixedRatePoller implements Consumer<SourcePollingChannelAdapterSpec> { 
		@Override
		public void accept(SourcePollingChannelAdapterSpec spec) {
			spec.poller(Pollers.fixedRate(500));
		}

	}
	//------------------spring-boot-sample-integration------------------------END
}
