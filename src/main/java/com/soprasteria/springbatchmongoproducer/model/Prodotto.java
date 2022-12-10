package com.soprasteria.springbatchmongoproducer.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "prodotti")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prodotto {
	
	@Id
	private Integer codiceProdotto;
	
	private String nome;
	
	private String descrizione;
	
	private BigDecimal prezzo;

}
