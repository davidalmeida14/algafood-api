package com.algaworks.algafood;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.exceptionhandler.ProblemType;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.infraestructure.repository.CidadeRepository;
import com.algaworks.algafood.infraestructure.repository.CozinhaRepository;
import com.algaworks.algafood.infraestructure.repository.EstadoRepository;
import com.algaworks.algafood.infraestructure.repository.FormaPagamentoRepository;
import com.algaworks.algafood.infraestructure.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {
	
	private static final String DEVE_CONTER_FRETE_GRÁTIS = "deve conter Frete Grátis";

	private static final String DADOS_INVALIDOS = "Um ou mais dados estão inválidos. Faça o preenchimento correto e tente novamente";

	private static final String BASE_PATH = "/restaurantes";
	
	@LocalServerPort
	private Integer port;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private FormaPagamentoRepository pagamentoRepository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
	private Cozinha cozinhaItaliana;
	
	private Cozinha cozinhaFrancesa;
	
	private Restaurante restauranteSaulos;
	
	private Restaurante barDoCusCuz;
	
	
	
	@Before
	public void beforeTest() {
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.basePath = BASE_PATH;
		RestAssured.port = port;
		
		preparaBase();
		
	}
	
	@Test
	public void deveRetornar201_QuandoCadastrarRestaurante() {
		
		Restaurante restaurante = new Restaurante();
		restaurante.setNome("Saulos Pizzaria");
		restaurante.setCozinha(cozinhaRepository.findById(1L).get());
		restaurante.setTaxaFrete(BigDecimal.valueOf(10.6));

		Restaurante retornoAPI = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(restaurante)
			.log().all()
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value())
			.log().all()
		.and()
			.extract()
			.response()
			.as(Restaurante.class);
			
		assertEquals(restaurante.getNome(), retornoAPI.getNome());
		assertEquals(restaurante.getCozinha().getId(), retornoAPI.getCozinha().getId());
		assertEquals(restaurante.getTaxaFrete(), retornoAPI.getTaxaFrete());
		
	}
	
	@Test
	public void deveRetornarStatus200_QuandoListarRestaurantes () {
		
		int quantidadeRestaurantes = restauranteRepository.findAll().size();
		
		RestAssured.given()
						.accept(ContentType.JSON)
					.when()
						.get()
					.then()
						.statusCode(HttpStatus.OK.value())
					.and()
						.body("nome", hasSize(quantidadeRestaurantes));
		
	}

	@Test
	public void deveRetornarErroEStatusCode400_QuandoCadastrarRestauranteSemPassarCozinha() {
		
		
		Restaurante restaurante = new Restaurante();
		restaurante.setNome("Teste");
		restaurante.setTaxaFrete(BigDecimal.valueOf(10.5));
		
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		
		try {
			json = mapper.writeValueAsString(restaurante);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post()
		.then()
			.body("userMessage", equalTo(DADOS_INVALIDOS))
			.and()
			.body("title", equalTo(ProblemType.ERRO_NEGOCIO.getTitle()))
		.and()
			.statusCode(HttpStatus.BAD_REQUEST.value());
		
	}
	
	@Test
	public void deveRetornarErroEStatusCode400_QuandoCadastrarRestauranteComFreteGratisSemNome() {
		
		Restaurante restaurante = new Restaurante();
		restaurante.setNome("Thai Lanches");
		restaurante.setCozinha(cozinhaRepository.findById(1L).get());
		restaurante.setTaxaFrete(BigDecimal.valueOf(0));
		
		Problem problem = given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(restaurante)
			.log().all()
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
		.and()
			.log().all()
			.extract()
			.response()
			.as(Problem.class);
		
		List<Problem.Object> collect = problem.getObjects()
			   .stream()
			   .filter(f-> f.getName().equalsIgnoreCase(restaurante.getClass().getSimpleName()) && f.getUserMessage().contains(DEVE_CONTER_FRETE_GRÁTIS))
			   .collect(Collectors.toList());
		
		assertThat(collect.size() > 0);
		
	}
	
	@Test
	public void deveRetornar404_quandoBuscarRestauranteQueNaoExiste() {
		given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.pathParam("id", 465)
			.log().all()
		.when()
			.get("{id}")
		.then()
			.log().all()
			.statusCode(HttpStatus.NOT_FOUND.value())
		.and()
			.body("title", equalTo(ProblemType.RECURSO_NAO_ENCONTRADO.getTitle()));
		
	}
	private void preparaBase() {
		
		Estado estado = new Estado();
		estado.setNome("Paraiba");
		estado = estadoRepository.save(estado);
		
		Cidade cidade = new Cidade();
		cidade.setEstado(estado);
		cidade.setNome("Joao Pessoa");
		cidade = cidadeRepository.save(cidade);
		
		
		Endereco endereco = new Endereco();
		endereco.setLogradouro("Rua Antonio Claudino Leal");
		endereco.setBairro("Costa e Silva");
		endereco.setNumero("127");
		endereco.setCidade(cidade);
		
		cozinhaItaliana = new Cozinha();
		cozinhaItaliana.setNome("Italiana");
		cozinhaItaliana = cozinhaRepository.save(cozinhaItaliana);
		
		cozinhaFrancesa = new Cozinha();
		cozinhaFrancesa.setNome("Francesa");
		cozinhaFrancesa = cozinhaRepository.save(cozinhaFrancesa);
		
		
		FormaPagamento pagamentoCartao = new FormaPagamento();
		pagamentoCartao.setDescricao("Cartão de Crédito");
		pagamentoCartao = pagamentoRepository.save(pagamentoCartao);
		
		restauranteSaulos = new Restaurante();
		restauranteSaulos.setCozinha(cozinhaItaliana);
		restauranteSaulos.setNome("Saulos - Restaurante dos Estados");
		restauranteSaulos.setTaxaFrete(BigDecimal.valueOf(9.5));
		restauranteSaulos.setEndereco(endereco);
		//restauranteSaulos.setFormasPagamentos(Lists.newArrayList(pagamentoCartao));
		
		restauranteSaulos = restauranteRepository.save(restauranteSaulos);
		
		barDoCusCuz = new Restaurante();
		barDoCusCuz.setCozinha(cozinhaFrancesa);
		barDoCusCuz.setNome("Bar do CusCuz - Restaurante dos Estados");
		barDoCusCuz.setTaxaFrete(BigDecimal.valueOf(8.3));
		barDoCusCuz.setEndereco(endereco);
		//barDoCusCuz.setFormasPagamentos(Lists.newArrayList(pagamentoCartao));
		
		barDoCusCuz = restauranteRepository.save(barDoCusCuz);
		
	}

}
