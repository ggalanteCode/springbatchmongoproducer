package com.soprasteria.springbatchmongoproducer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.soprasteria.springbatchmongoproducer.model.Cliente;
import com.soprasteria.springbatchmongoproducer.service.ClienteProducerService;

import static com.soprasteria.springbatchmongoproducer.constants.KafkaConstants.*;

@Service
public class ClienteProducerServiceImpl implements ClienteProducerService {

	@Autowired
	@Qualifier("kafkaTemplate")
	private KafkaTemplate<String, Cliente> clientekafkaTemplate;
	
	@Override
	public void publish(Cliente cliente) {
		// TODO Auto-generated method stub
		System.out.println(cliente + " sta per essere inserito nel topic");
		clientekafkaTemplate.send(TOPIC_NAME, cliente);
	}

}
