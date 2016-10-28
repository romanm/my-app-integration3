package my.app; 
import org.springframework.stereotype.Service;

/**
 * @author roman
 *
 */
@Service
public class HelloWorldService {

	private final HelloWorldMessageObject messageObject;

	public HelloWorldService(HelloWorldMessageObject configuration) {
		this.messageObject = configuration;
	}

	/**
	 * Фактично дія
	 * 
	 * @param name
	 * @return
	 */
	public String operationWithMessage(String name) {
		return this.messageObject.getGreeting() + " " + name;
	}

}
