package com.soprasteria.springbatchmongoproducer.service;

import com.soprasteria.springbatchmongoproducer.model.Cliente;

public interface ClienteProducerService {
	
	public void publish(Cliente cliente);

}
