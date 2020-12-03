package com.algaworks.algafood.api.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.algaworks.algafood.infraestructure.repository.PedidoRepository;
import com.algaworks.algafood.infraestructure.repository.filter.PedidoFilter;
import com.algaworks.algafood.infraestructure.repository.spec.PedidoSpecs;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import com.algaworks.algafood.api.assembler.impl.PedidoInputDisassembler;
import com.algaworks.algafood.api.assembler.impl.PedidoModelAssemblerImpl;
import com.algaworks.algafood.api.assembler.impl.PedidoResumoModelAssemblerImpl;
import com.algaworks.algafood.api.model.PedidoModel;
import com.algaworks.algafood.api.model.PedidoResumoModel;
import com.algaworks.algafood.api.model.input.CidadeIdInput;
import com.algaworks.algafood.api.model.input.EnderecoInput;
import com.algaworks.algafood.api.model.input.FormaPagamentoIdInput;
import com.algaworks.algafood.api.model.input.ItemPedidoInput;
import com.algaworks.algafood.api.model.input.PedidoInput;
import com.algaworks.algafood.api.model.input.RestauranteIdInput;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroPedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private CadastroPedidoService pedidoService;
	
	@Autowired
	private PedidoModelAssemblerImpl pedidoAssembler;
	
	@Autowired
	private PedidoResumoModelAssemblerImpl pedidoResumoAssembler;
	
	@Autowired
	private PedidoInputDisassembler pedidoInputDisassembler;

	@Autowired
	private PedidoRepository pedidoRepository;

	private static final String PEDIDOFILTER = "pedidoFilter";


//	@GetMapping
//	public ResponseEntity<?> listarPedidos(@RequestParam(required = false) String campos) {
//
//		List<Pedido> pedidos = pedidoService.listar();
//		List<PedidoResumoModel> collectionModel = pedidoResumoAssembler.toCollectionModel(pedidos);
//		return ResponseEntity.ok(collectionModel);
//	}


//	@GetMapping
//	public ResponseEntity<MappingJacksonValue> listarPedidos(@RequestParam(required = false) String campos) {
//
//		List<Pedido> pedidos = pedidoService.listar();
//		List<PedidoResumoModel> collectionModel = pedidoResumoAssembler.toCollectionModel(pedidos);
//
//		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(collectionModel);
//
//		// Criando filtro de retorno dos campos a serem serializados
//		SimpleFilterProvider filterProvider = buildFilter(PEDIDOFILTER, campos);
//
//		mappingJacksonValue.setFilters(filterProvider);
//		return ResponseEntity.ok(mappingJacksonValue);
//	}

	@GetMapping
	public ResponseEntity<?> pesquisar(PedidoFilter filtro) {
		List<Pedido> pedidos = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro));
		List<PedidoResumoModel> collectionModel = pedidoResumoAssembler.toCollectionModel(pedidos);
		return ResponseEntity.ok(collectionModel);
	}

	@GetMapping(value = "/{codigo}")
	public ResponseEntity<?> buscar(@PathVariable String codigo) {
		Pedido buscar = pedidoService.buscar(codigo);
		PedidoModel model = pedidoAssembler.toModel(buscar);
		return ResponseEntity.ok(model);
	}

	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping
	public ResponseEntity<?> salvar (@RequestBody @Valid PedidoInput pedidoInput) {
		Pedido pedido = pedidoInputDisassembler.toDomainObject(pedidoInput);
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		pedido.setCliente(usuario);
		Pedido pedidoEmitido = pedidoService.emitir(pedido);
		return ResponseEntity.ok().body(pedidoAssembler.toModel(pedidoEmitido));
	}

	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PutMapping("/{codigo}/confirmacao")
	public void confirmacao(@PathVariable("codigo") String codigo) {
		pedidoService.confirmar(codigo);
	}
	
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PutMapping("/{codigo}/entrega")
	public void entrega(@PathVariable("codigo") String codigo) {
		pedidoService.entregar(codigo);
	}
	
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@PutMapping("/{codigo}/cancelamento")
	public void cancelamento(@PathVariable("codigo") String codigo) {
		pedidoService.cancelar(codigo);
	}

	/**
	 * Constrói filtro de retorno dos dados
	 * @param nameFilter
	 * @param fields
	 * @return
	 */
	public SimpleFilterProvider buildFilter(String nameFilter, String fields){
		// Criando filtro de retorno dos campos a serem serializados
		// SimpleFilterProvider é uma classe que estende FilderProvider
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();

		// PEDIDOFILTER = Filtro mapeado no Model
		filterProvider.addFilter(PEDIDOFILTER, SimpleBeanPropertyFilter.serializeAll());

		if(StringUtils.isNotBlank(fields)) {
			filterProvider.addFilter(PEDIDOFILTER, SimpleBeanPropertyFilter.filterOutAllExcept(fields.split(",")));
		}
		return filterProvider;
	}

//
//	public static void main(String[] args) throws JsonProcessingException {
//
//		ObjectMapper mapper = new ObjectMapper();
//		String json = "";
//		PedidoInput input = new PedidoInput();
//
//		RestauranteIdInput restauranteIdInput = new RestauranteIdInput();
//		restauranteIdInput.setId(1L);
//
//		EnderecoInput enderecoInput = new EnderecoInput();
//		enderecoInput.setBairro("Costa e Silva");
//		enderecoInput.setCidade(new CidadeIdInput(1L));
//		enderecoInput.setCep("38400-000");
//		enderecoInput.setNumero("1242");
//		enderecoInput.setLogradouro("Rua Floriano Peixoto");
//
//		ItemPedidoInput itemPedido1 = new ItemPedidoInput();
//		itemPedido1.setProdutoId(1L);
//		itemPedido1.setObservacao("Sem alface");
//		itemPedido1.setQuantidade(2);
//
//		ItemPedidoInput itemPedido2 = new ItemPedidoInput();
//		itemPedido2.setProdutoId(2L);
//		itemPedido2.setObservacao("Sem molho");
//		itemPedido2.setQuantidade(3);
//
//		List<ItemPedidoInput> listaItens = Arrays.asList(itemPedido1, itemPedido2);
//
//		FormaPagamentoIdInput formaPagamentoInput = new FormaPagamentoIdInput();
//		formaPagamentoInput.setId(1L);
//
//		input.setRestaurante(restauranteIdInput);
//		input.setEnderecoEntrega(enderecoInput);
//		input.setFormaPagamento(formaPagamentoInput);
//		input.setItens(listaItens);
//
//		json = mapper.writeValueAsString(input);
//		System.out.println(json);
//	}
}
