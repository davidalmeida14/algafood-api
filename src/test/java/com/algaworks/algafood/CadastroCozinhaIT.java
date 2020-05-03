package com.algaworks.algafood;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.infraestructure.repository.CozinhaRepository;
import com.algaworks.algafood.util.DatabaseCleaner;
import com.algaworks.algafood.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroCozinhaIT {

	private static final int COZINHAID = 150;

	@LocalServerPort
	public int port;

	public final String BASE_PATH_COZINHAS = "/cozinhas";

	@Autowired
	CozinhaRepository cozinhaRepository;

	@Autowired
	private DatabaseCleaner cleaner;

	Cozinha cozinha1 = new Cozinha();

	Cozinha cozinha2 = new Cozinha();

	@Before
	public void setUp() {

		cozinha1.setNome("Tailandesa");
		cozinha2.setNome("Americana");

		enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = BASE_PATH_COZINHAS;
		cleaner.clearTables();
		prepararDados();
	}

	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {

		given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value());

	}

	@Test
	public void deveConterDozeCozinhasQuandoConsultarCozinhas() {

		List<Cozinha> cozinhas = cozinhaRepository.findAll();
		
		int tamanhoItens = cozinhas.size();

		enableLoggingOfRequestAndResponseIfValidationFails();

		given().accept(ContentType.JSON).when().get().then().body("", hasSize(tamanhoItens)).body("nome",
				hasItems(cozinhas.get(0).getNome(), cozinhas.get(1).getNome()));

	}

	@Test
	public void deveRetornarStatus201_QuandoCadastrarCozinha() {
		
		String nameFile = "CadastroCozinhaJsonTest.json";
		
		String json = new ResourceUtils().getJsonFromName((nameFile));
		
		given()
			.accept(ContentType.JSON)
			.contentType(ContentType.JSON)
			.body(json)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());

	}

	@Test
	public void deveRetornarOk_QuandoConsultarCozinhaExistente() {

		given()
			.accept(ContentType.JSON)
			.pathParam("cozinhaId", cozinha2.getId())
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.OK.value())
		.and()
			.body("nome", equalTo(cozinha2.getNome()));

	}

	@Test
	public void deveRetornar404_QuandoConsultarCozinhaInexistente() {

		given()
			.accept(ContentType.JSON)
			.pathParam("cozinhaId", COZINHAID)
		.when()
			.get("/{cozinhaId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());

	}

	private void prepararDados() {
		cozinhaRepository.save(cozinha1);
		cozinhaRepository.save(cozinha2);
	}

//	
//	@Autowired
//	private CadastroCozinhaService cadastroCozinha;
//	
//	/**
//	 * Introdução aos testes de integração.
//	 * 
//	 * É baseado em 3 pernas/passos: Cenário, Ação e Validação
//	 * 
//	 * return @void
//	 */
//	@Test
//	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
//		
//		final String COZINHA = "Chinesa";
//		// Cenário
//		
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome(COZINHA);
//		
//		// Ação
//		
//		novaCozinha = cadastroCozinha.salvar(novaCozinha);
//		
//		// Validações
//		
//		assertThat(novaCozinha).isNotNull();
//		assertThat(novaCozinha.getId()).isNotNull();
//		assertEquals(novaCozinha.getNome(), COZINHA);
//		
//	}
//	
//	@Test(expected = ConstraintViolationException.class )
//	public void deveFalhar_quandoCadastrarCozinhaSemNome() {
//		
//		// Cenário
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome(null);
//		
//		// Ação
//		novaCozinha = cadastroCozinha.salvar(novaCozinha);
//		
//	}
//	
//	@Test(expected = CozinhaEmUsoException.class)
//	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
//		
//		Cozinha cozinha = cadastroCozinha.buscar(1L);
//		
//		cadastroCozinha.excluir(cozinha.getId());
//		
//	}
//	
//	@Test(expected = CozinhaNaoEncontradaException.class)
//	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
//		
//		cadastroCozinha.excluir(154L);
//		
//	}

}
