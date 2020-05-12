package alura.com.br.ecommerce;

import java.util.Date;
import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class FraudDetectorService {
	public static void main(String[] args) {
		executarFraud();
	}

	public static void executarFraud() {
		FraudDetectorService fraudService = new FraudDetectorService();
		try (KafkaService<Order> service = new KafkaService<>(Order.class, fraudService.getClass().getSimpleName(), "ECOMMERCE_NEW_ORDER",
				fraudService::parse,
				new HashMap<>())) {
			service.run();
		}
	}

	protected void parse(ConsumerRecord<String, Order> record) {
		System.out.println("----------------------------------------");
		System.out.println("Processing new order, checking for fraud" + " " + new Date());
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Order processed");
	}

}
