package alura.com.br.ecommerce;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonDeserializer<T> implements Deserializer<T>{
	private final Gson gson = new GsonBuilder().create();
	private Class<T> type;
	public static final String TYPE_CONFIG = "type_config";

	/**
	 * Recebe as configura��es do kafka. Valores do properties.
	 */
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		String typeName = String.valueOf(configs.get(TYPE_CONFIG));
		try {
			this.type = (Class<T>)Class.forName(typeName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Type for deserialization does nor exists in the classpath");
		}
		
	}
	
	@Override
	public T deserialize(String s, byte[] bytes) {
		return this.gson.fromJson(new String(bytes), type);
	}
	

}
