package com.soprasteria.springbatchmongoproducer.processors;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.soprasteria.springbatchmongoproducer.model.Cliente;
import com.soprasteria.springbatchmongoproducer.model.Prodotto;
import com.soprasteria.springbatchmongoproducer.service.ClienteProducerService;

public class ClienteProcessor implements ItemProcessor<Cliente, Cliente> {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ClienteProducerService clienteProducerService;

	@Override
	public Cliente process(Cliente item) throws Exception {
		// TODO Auto-generated method stub
		Prodotto prodotto = mongoTemplate.findOne(new Query(Criteria.where("codiceProdotto").is(item.getProdottoAcquistato())), Prodotto.class);
		item.setNomeDescrizioneProdottoAcquistato(prodotto.getNome() 
				+ " " + prodotto.getDescrizione());
		clienteProducerService.publish(item);
		return item;
	}

}
