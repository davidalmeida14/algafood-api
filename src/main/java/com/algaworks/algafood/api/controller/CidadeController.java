package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.impl.CidadeDisassemblerImpl;
import com.algaworks.algafood.api.assembler.impl.CidadeModelAssemblerImpl;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(value = "/cidades", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CidadeController {

    @Autowired
    private CadastroCidadeService cidadeService;

    @Autowired
    private CidadeModelAssemblerImpl cidadeAssembler;

    @Autowired
    private CidadeDisassemblerImpl cidadeDisassembler;

    @GetMapping
    public ResponseEntity<List<CidadeModel>> listar() {
        List<CidadeModel> response = cidadeAssembler.toCollectionModel(cidadeService.listar());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeModel> buscar(@PathVariable Long id) {
        CidadeModel response = cidadeAssembler.toModel(cidadeService.buscar(id));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cidadeService.remover(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeModel> atualizar(@PathVariable Long id, @Valid @RequestBody CidadeInput cidade) {
        Cidade atualizar = cidadeService.atualizar(id, cidadeDisassembler.toDomainObject(cidade));
        return ResponseEntity.ok(cidadeAssembler.toModel(atualizar));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CidadeModel> salvar(@RequestBody @Valid CidadeInput cidadeInput) {

        Cidade cidade = cidadeDisassembler.toDomainObject(cidadeInput);

        CidadeModel response = cidadeAssembler.toModel(cidadeService.salvar(cidade));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
