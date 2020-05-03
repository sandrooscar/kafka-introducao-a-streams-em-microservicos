package alura.com.br.ecommerce;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class NewOrderMain {

	public static <V> void main(String[] args) throws InterruptedException, ExecutionException {

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties());
		String value = "12334,1001,189";
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("ECOMMERCE_NEW_ORDER", value, value);
		//assincono
//		producer.send(record);
		//aguarda a execução, e adiciona uma callback
		producer.send(record, (data, exception) ->{
			if(exception != null){
				exception.printStackTrace();
				return;
			}else{
				System.out.println("sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp " + data.timestamp());
			}
		}).get();
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
