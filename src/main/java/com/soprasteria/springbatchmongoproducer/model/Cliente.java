package com.soprasteria.springbatchmongoproducer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "clienti")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
	
	@Id
	private Integer codiceCliente;
	
	private String nome;
	
	private String cognome;
	
	private String indirizzo;
	
	private String email;
	
	private String telefono;
	
	private Integer prodottoAcquistato;
	
	private String nomeDescrizioneProdottoAcquistato;

}
