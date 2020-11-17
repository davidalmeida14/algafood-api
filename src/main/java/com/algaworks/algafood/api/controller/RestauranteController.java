package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.algaworks.algafood.api.model.view.RestauranteResumoView;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import com.algaworks.algafood.api.assembler.impl.RestauranteInputDisassemblerImpl;
import com.algaworks.algafood.api.assembler.impl.RestauranteModelAssemblerImpl;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.CozinhaIdInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.core.validator.ValidacaoException;
import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.algaworks.algafood.infraestructure.repository.CozinhaRepository;
import com.algaworks.algafood.infraestructure.repository.RestauranteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController()
@RequestMapping(value = "/restaurantes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RestauranteController {

	@Autowired
	RestauranteRepository restauranteRepository;

	@Autowired
	CadastroRestauranteService cadastroRestaurante;

	@Autowired
	CozinhaRepository cozinhaRepository;

	@Autowired
	private RestauranteModelAssemblerImpl restauranteAssembler;
	
	@Autowired
	private RestauranteInputDisassemblerImpl restauranteDisassembler;
	
	@Autowired
	private SmartValidator validator;

	@GetMapping
	@Cacheable(value = "restaurantes")
	@ResponseStatus(code = HttpStatus.OK)
	@JsonView(RestauranteResumoView.Resumo.class)
	public List<RestauranteModel> listar(){
		return restauranteAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@GetMapping(params = "projecao=apenasNome")
	@Cacheable(value = "restaurantes")
	@ResponseStatus(code = HttpStatus.OK)
	@JsonView(RestauranteResumoView.ApenasIdENome.class)
	public List<RestauranteModel> listarApenasIdENome(){
		return restauranteAssembler.toCollectionModel(restauranteRepository.findAll());
	}

//	@GetMapping
//	@Cacheable(value = "restaurantes")
//	@ResponseStatus(code = HttpStatus.OK)
//	public MappingJacksonValue listar(@RequestParam(required = false) String projecao){
//		List<Restaurante> restaurantes = restauranteRepository.findAll();
//		List<RestauranteModel> restauranteModels = restauranteAssembler.toCollectionModel(restaurantes);
//		return mapearRetornoEsperado(restauranteModels, projecao);
//	}

	/**
	 * MappingJackonView é uma classe que empacota alguns recursos de mapeamento de JSON. Sendo assim, é possível utilizar as projeções
	 * setando-as programaticamente de acordo com uma regra definida de acordo com a necessidade do negócio.
	 * @param restauranteModel
	 * @param projecao
	 * @return
	 */
	private MappingJacksonValue mapearRetornoEsperado(List<RestauranteModel> restauranteModel, String projecao) {
		MappingJacksonValue mapping = new MappingJacksonValue(restauranteModel);
		mapping.setSerializationView(RestauranteResumoView.ApenasIdENome.class);
		if("completo".equals(projecao)) {
			mapping.setSerializationView(null);
		} else if("resumo".equals(projecao)){
			mapping.setSerializationView(RestauranteResumoView.Resumo.class);
		}
		return mapping;
	}

	@GetMapping(params = "projecao=resumo")
	@ResponseStatus(HttpStatus.OK)
	@JsonView(RestauranteResumoView.Resumo.class)
	public List<RestauranteModel> listarResumo() throws JsonProcessingException {
		return listar();
	}

	@GetMapping(params = "projecao=nome")
	@ResponseStatus(HttpStatus.OK)
	@JsonView(RestauranteResumoView.ApenasIdENome.class)
	public List<RestauranteModel> listarResumoApenasIdENome() throws JsonProcessingException {
		return listar();
	}

//	@GetMapping
//	@Cacheable(value = "restaurantes")
//	@ResponseStatus(HttpStatus.OK)
//	public List<RestauranteModel> listar() throws JsonProcessingException {
//		System.out.println(LocalDateTime.now());
//		return restauranteAssembler.toCollectionModel(restauranteRepository.findAll());
//	}
//


	@GetMapping("/{id}")
	@Cacheable(value = "restaurante")
	public ResponseEntity<RestauranteModel> buscar(@PathVariable Long id) {
		Restaurante restaurante = cadastroRestaurante.buscar(id);
		return ResponseEntity.ok(restauranteAssembler.toModel(restaurante));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<RestauranteModel> adicionar(
			@RequestBody
			@Valid RestauranteInput restauranteInput) {
		
		Restaurante restauranteSalvo = cadastroRestaurante.salvar(restauranteDisassembler.toDomainObject(restauranteInput));

		return ResponseEntity.status(HttpStatus.CREATED).body(restauranteAssembler.toModel(restauranteSalvo));

	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<RestauranteModel> atualizar(@PathVariable Long id, 
									   @Valid @RequestBody RestauranteInput restauranteInput) {
		try {
			Restaurante restauranteBuscado = cadastroRestaurante.buscar(id);
			
			restauranteDisassembler.copyToDomainObject(restauranteInput, restauranteBuscado);
			
			Restaurante restauranteAtualizado = cadastroRestaurante.atualizarRestaurante(restauranteBuscado);
			
			RestauranteModel modelResponse = restauranteAssembler.toModel(restauranteAtualizado);
			
			return ResponseEntity.ok(modelResponse);
		} catch (CidadeNaoEncontradaException | CozinhaNaoEncontradaException ex) {
			throw new NegocioException(ex.getMessage());
		}
				
		
	}

	@PatchMapping("/{id}")
	public ResponseEntity<RestauranteModel> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos,
			HttpServletRequest request) {

		Restaurante restauranteAtual = cadastroRestaurante.buscar(id);
		
		merge(campos, restauranteAtual, request);
		
		validar(restauranteAtual);

		return atualizar(id, toInputModel(restauranteAtual));


	}

	/**
	 *  Validar manualmente a entidade restaurante. 
	 *  Usa-se o {@link BeanPropertyBindingResult} para construir a validação
	 *  Usa-se o {@link SmartValidator} para efetiva a validação
	 *  
	 * @param restaurante
	 */
	private void validar(Restaurante restaurante) {
		
		BeanPropertyBindingResult result = new BeanPropertyBindingResult(restaurante, restaurante.getClass().getSimpleName());
		
		validator.validate(restaurante, result);
		
		if(result.hasErrors()) {
			throw new ValidacaoException(result);
		}
	}

	private void merge(Map<String, Object> camposOrigem, Restaurante restauranteDestino, HttpServletRequest request) {

		ServletServerHttpRequest req = new ServletServerHttpRequest(request);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

			Restaurante restauranteOrigem = objectMapper.convertValue(camposOrigem, Restaurante.class);

			camposOrigem.forEach((nomePropriedade, valorPropriedade) -> {
				Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				field.setAccessible(true);

				Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

				ReflectionUtils.setField(field, restauranteDestino, novoValor);
			});
		} catch (IllegalArgumentException e) {
			Throwable cause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), cause, req);
		}
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		cadastroRestaurante.excluir(id);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable("id") Long id) {
		cadastroRestaurante.ativar(id);
	}
	
	@DeleteMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable("id") Long id) {
		cadastroRestaurante.inativar(id);
	}
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMuliplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.ativar(restauranteIds);	
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMuliplos(@RequestBody List<Long> restauranteIds) {
		try {
			cadastroRestaurante.inativar(restauranteIds);	
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@PatchMapping("/{id}/abertura")
	@CacheEvict(value = "restaurante")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long id) {
		cadastroRestaurante.abrir(id);
	}
	
	@PatchMapping("/{id}/fechamento")
	@CacheEvict(value = "restaurante")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long id) {
		cadastroRestaurante.fechar(id);
	}
	
	public RestauranteInput toInputModel(Restaurante restaurante) {
		
		RestauranteInput input = new RestauranteInput();
		input.setNome(restaurante.getNome());
		
		CozinhaIdInput cozinhaInput = new CozinhaIdInput();
		cozinhaInput.setId(restaurante.getCozinha().getId());
		
		input.setTaxaFrete(restaurante.getTaxaFrete());
		input.setCozinha(cozinhaInput);
		
		return input;
	}
	
}
