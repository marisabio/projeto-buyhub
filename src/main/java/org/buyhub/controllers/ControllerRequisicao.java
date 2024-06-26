package org.buyhub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.buyhub.domain.entities.CompraRequisicao;
import org.buyhub.exceptions.ResourceNotFoundException;
import org.buyhub.service.DTOs.requisicao.DadosAtualizacaoRequisicao;
import org.buyhub.service.DTOs.requisicao.DadosCadastroRequisicao;
import org.buyhub.service.DTOs.requisicao.DadosListagemRequisicao;
import org.buyhub.service.DTOs.requisicao.RepositoryRequisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/requisicoes")
@Tag(name = "Requisição", description = "CRUD de requisição.")
public class ControllerRequisicao {

    @Autowired
    private RepositoryRequisicao repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastro de requisição", description = "Endpoint do cadastro de novas requisições.")
    public ResponseEntity<DadosListagemRequisicao> cadastrar(@RequestBody @Valid DadosCadastroRequisicao dados, UriComponentsBuilder uriBuilder) {
        var requisicao = new CompraRequisicao(dados);
        repository.save(requisicao);
        var uri = uriBuilder.path("/requisicoes/{idRequisicao}").buildAndExpand(requisicao.getIdRequisicao()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemRequisicao(requisicao));
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Listagem de requisições", description = "Endpoint da listagem de requisições cadastradas.")
    public ResponseEntity<Page<DadosListagemRequisicao>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemRequisicao::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping(path = "/{idRequisicao}", produces = "application/json")
    @Operation(summary = "Exibir requisição", description = "Endpoint da exibição de uma única requisição cadastrada.")
    public ResponseEntity<DadosListagemRequisicao> exibir(@PathVariable Long idRequisicao) {
        var requisicao = repository.findById(idRequisicao)
                .orElseThrow(() -> new ResourceNotFoundException("Requisição não encontrada para este ID :: " + idRequisicao));
        return ResponseEntity.ok(new DadosListagemRequisicao(requisicao));
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Atualizar requisição", description = "Endpoint da atualização de uma única requisição cadastrada.")
    public ResponseEntity<DadosListagemRequisicao> atualizar(@RequestBody @Valid DadosAtualizacaoRequisicao dados) {
        var requisicao = repository.findById(dados.idRequisicao())
                .orElseThrow(() -> new ResourceNotFoundException("Requisição não encontrada para este ID :: " + dados.idRequisicao()));
        requisicao.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemRequisicao(requisicao));
    }

    @DeleteMapping(path = "/{idRequisicao}")
    @Transactional
    @Operation(summary = "Excluir requisição", description = "Endpoint da exclusão de uma única requisição cadastrada.")
    public ResponseEntity<String> excluir(@PathVariable Long idRequisicao) {
        var requisicao = repository.findById(idRequisicao)
                .orElseThrow(() -> new ResourceNotFoundException("Requisição não encontrada para este ID :: " + idRequisicao));
        repository.delete(requisicao);
        return ResponseEntity.ok("Requisição " + idRequisicao + " deletada.");
    }
}
