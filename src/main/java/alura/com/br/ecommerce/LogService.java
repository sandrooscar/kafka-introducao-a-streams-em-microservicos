package alura.com.br.ecommerce;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class LogService {
	public static void main(String[] args) {
		LogService logService = new LogService();
		try (KafkaService<String> service = new KafkaService(String.class, LogService.class.getSimpleName(), 
				Pattern.compile("ECOMMERCE.*"), 
				logService::parse)){
			service.run();
		}
	}

	private void parse(ConsumerRecord<String, String> record) {
		System.out.println("----------------------------------------");
		System.out.println("LOG: "+record.topic() +" "+new Date());
		System.out.println(record.key());
		System.out.println(record.value());
		System.out.println(record.partition());
		System.out.println(record.offset());
	}

}
