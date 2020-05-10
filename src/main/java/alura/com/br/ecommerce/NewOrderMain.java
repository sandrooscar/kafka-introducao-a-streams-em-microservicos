package alura.com.br.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

	public static <V> void main(String[] args) throws InterruptedException, ExecutionException {
		try (KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>()) {
			try (KafkaDispatcher<Email> emailDispatcher = new KafkaDispatcher<>()) {
				for (int i = 0; i < 10; i++) {

					String userId = UUID.randomUUID().toString();
					String orderId = UUID.randomUUID().toString();
					BigDecimal amount = new BigDecimal(Math.random() * 5000 + 1);
					Order order = new Order(userId, orderId, amount);

					orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

					Email email = new Email("Order", "Thank you for your order! We are processing your order!");
					emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);
				}
			}
		}
	}

}
