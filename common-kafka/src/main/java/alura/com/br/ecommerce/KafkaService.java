package alura.com.br.ecommerce;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaService<T> implements Closeable{
	private final KafkaConsumer<String, T> consumer;
	private final ConsumerFunction<T> parse;
	
	public KafkaService(Class<T> type, String groupId, String topic, ConsumerFunction<T> parse, Map<String, String> overrideProperties) {
		this(type, groupId, parse, overrideProperties);
		//se inscreve no topic desejado para "ouvir"
		this.consumer.subscribe(Collections.singletonList(topic));
	}

	public KafkaService(Class<T> type, String groupId, Pattern topic, ConsumerFunction<T> parse, Map<String, String> overrideProperties) {
		this(type, groupId, parse, overrideProperties);
		//se inscreve no topic desejado para "ouvir"
		this.consumer.subscribe(topic);
	}

	private KafkaService(Class<T> type, String groupId,ConsumerFunction<T> parse, Map<String, String> overrideProperties) {
		this.parse = parse;
		this.consumer = new KafkaConsumer<String, T>(getProperties(type, groupId, overrideProperties));
	}

	public void run(){
		while (true) {
			ConsumerRecords<String, T> records = consumer.poll(Duration.ofMillis(100));
			if (!records.isEmpty()) {
				System.out.println("Found " + records.count() +" records");
				for (ConsumerRecord<String, T> record : records) {
					parse.consume(record);
				}
			}
		}
		
	}

	private Properties getProperties(Class<T> type, String groupId, Map<String, String> overrideProperties) {
		Properties properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
		properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
		properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
		properties.putAll(overrideProperties);
		return properties;
	}

	@Override
	public void close() {
		this.consumer.close();
	}
}
