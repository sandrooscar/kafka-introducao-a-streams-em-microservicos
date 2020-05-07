package alura.com.br.ecommerce;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	public static <V> void main(String[] args) throws InterruptedException, ExecutionException {

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties());
		for (int i = 0; i<10; i++){
			
			String key = UUID.randomUUID().toString();
			String value = key+",12334,1001,189";
			ProducerRecord<String, String> record = new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", key, value);
//assincono
//		producer.send(record);
			//aguarda a execução, e adiciona uma callback
			Callback callback = (data, exception) ->{
				if(exception != null){
					exception.printStackTrace();
					return;
				}else{
					System.out.println("sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
				}
			};
			String email = "Thank you for your order! We are processing your order!";
			ProducerRecord<String, String> emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", key, email);
			
			producer.send(record, callback).get();
			producer.send(emailRecord, callback).get();
		}
	}

	private static Properties properties() {
		Properties properties = new  Properties();
		//informa o endereço(s) dos servidores kafka
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		//Iinformar o serializador, tanto chave quanto valor são Strings, necessário informar no properties do Kafka
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		return properties;
	}

}
