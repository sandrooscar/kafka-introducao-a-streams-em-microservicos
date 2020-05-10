package alura.com.br.ecommerce;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaDispatcher<T> implements Closeable {
	private KafkaProducer<String, T> producer;

	public KafkaDispatcher() {
		this.producer = new KafkaProducer<>(properties());
	}
	
	private Properties properties() {
		Properties properties = new  Properties();
		//informa o endereço(s) dos servidores kafka
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		//Iinformar o serializador, tanto chave quanto valor são Strings, necessário informar no properties do Kafka
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
		
		return properties;
	}

	public void send(String topic, String key, T value) throws InterruptedException, ExecutionException {
		ProducerRecord<String, T> record = new ProducerRecord<>(topic, key, value);
		Callback callback = (data, exception) ->{
			if(exception != null){
				exception.printStackTrace();
				return;
			}else{
				System.out.println("sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
			}
		};
		this.producer.send(record, callback).get();
	}

	@Override
	public void close() {
		this.producer.close();
	}
}
