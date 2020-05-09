package alura.com.br.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService implements Closeable{
	private final KafkaConsumer<String, String> consumer;
	private ConsumerFunction parse;
	
	public KafkaService(String groupId, String topic, ConsumerFunction parse) {
		this.parse = parse;
		this.consumer = new KafkaConsumer<String, String>(properties(groupId));
		//se inscreve no topic desejado para "ouvir"
		consumer.subscribe(Collections.singletonList(topic));
	}
	
	public void run(){
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			if (!records.isEmpty()) {
				System.out.println("Found " + records.count() +" records");
				for (ConsumerRecord<String, String> record : records) {
					parse.consume(record);
				}
			}
		}
		
	}

	private Properties properties(String groupId) {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		return properties;
	}

	@Override
	public void close() {
		this.consumer.close();
	}
}
