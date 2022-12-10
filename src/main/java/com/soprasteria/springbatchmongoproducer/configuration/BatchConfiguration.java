package com.soprasteria.springbatchmongoproducer.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.soprasteria.springbatchmongoproducer.model.Cliente;
import com.soprasteria.springbatchmongoproducer.model.Prodotto;
import com.soprasteria.springbatchmongoproducer.processors.ClienteProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public ClienteProcessor clienteProcessor() {
		return new ClienteProcessor();
	}

	@Bean
	public FlatFileItemReader<Cliente> txtFileClienteReader() {
		FlatFileItemReader<Cliente> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource("src\\main\\resources\\clienti.txt"));
		reader.setLineMapper(new DefaultLineMapper<Cliente>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "codiceCliente", "nome", 
								"cognome", "indirizzo", "email", "telefono",
								"prodottoAcquistato" });
						setDelimiter("|");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Cliente>() {
					{
						setTargetType(Cliente.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public FlatFileItemReader<Prodotto> txtFileProdottoReader() {
		FlatFileItemReader<Prodotto> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource("src\\main\\resources\\prodotti.txt"));
		reader.setLineMapper(new DefaultLineMapper<Prodotto>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "codiceProdotto", "nome", 
								"descrizione", "prezzo" });
						setDelimiter("|");
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Prodotto>() {
					{
						setTargetType(Prodotto.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public MongoItemWriter<Cliente> mongoDbClienteWriter() {
		MongoItemWriter<Cliente> writer = new MongoItemWriter<Cliente>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("clienti");
		return writer;
	}

	@Bean
	public MongoItemWriter<Prodotto> mongoDbProdottoWriter() {
		MongoItemWriter<Prodotto> writer = new MongoItemWriter<Prodotto>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection("prodotti");
		return writer;
	}

	public Step stepProdotti() {
		return stepBuilderFactory.get("stepProdotti").<Prodotto, Prodotto>chunk(10)
				.reader(txtFileProdottoReader())
				.writer(mongoDbProdottoWriter())
				.build();
	}

	@Bean
	public Step stepClienti() {
		return stepBuilderFactory.get("stepClienti").<Cliente, Cliente>chunk(10)
				.reader(txtFileClienteReader())
				.processor(clienteProcessor())
				.writer(mongoDbClienteWriter())
				.build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("job")
				.incrementer(new RunIdIncrementer())
				.start(stepProdotti())
				.next(stepClienti())
				.build();
	}

}
