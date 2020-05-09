package alura.com.br.ecommerce;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static <V> void main(String[] args) throws InterruptedException, ExecutionException {
		try (KafkaDispatcher dispatcher = new KafkaDispatcher()) {
			for (int i = 0; i < 10; i++) {

				String key = UUID.randomUUID().toString();
				String value = key + ",12334,1001,189";
				dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

				String email = "Thank you for your order! We are processing your order!";
				dispatcher.send("ECOMMERCE_SEND_EMAIL", key, email);
			}
		}
	}

}
