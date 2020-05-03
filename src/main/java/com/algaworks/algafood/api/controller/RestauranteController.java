package com.algaworks.algafood.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.impl.RestauranteInputDisassemblerImpl;
import com.algaworks.algafood.api.assembler.impl.RestauranteModelAssemblerImpl;
import com.algaworks.algafood.api.model.RestauranteModel;
import com.algaworks.algafood.api.model.input.CozinhaIdInput;
import com.algaworks.algafood.api.model.input.RestauranteInput;
import com.algaworks.algafood.core.validator.ValidacaoException;
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
	@ResponseStatus(HttpStatus.OK)
	public List<RestauranteModel> listar() throws JsonProcessingException {
		return restauranteAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@GetMapping("/{id}")
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
		
		Restaurante restaurante = cadastroRestaurante.buscar(id);
		
		return ResponseEntity.ok(restauranteAssembler.toModel(cadastroRestaurante.atualizar(id, restaurante)));
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