package alura.com.br.ecommerce;

import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

	public static void main(String[] args) {
		EmailService emailService = new EmailService();
		try (KafkaService<Email> service = new KafkaService<>(Email.class, EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL",
				emailService::parse)) {
			service.run();
		}
 	}

	private void parse(ConsumerRecord<String, Email> record) {
		System.out.println("----------------------------------------");
		System.out.println("Send email..."+" "+new Date());
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Email sent");
	}

}
