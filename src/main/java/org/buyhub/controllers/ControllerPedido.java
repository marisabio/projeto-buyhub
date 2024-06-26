package org.buyhub.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.buyhub.domain.entities.ComprasPedido;
import org.buyhub.exceptions.ResourceNotFoundException;
import org.buyhub.service.DTOs.pedido.DadosAtualizacaoPedido;
import org.buyhub.service.DTOs.pedido.DadosCadastroPedido;
import org.buyhub.service.DTOs.pedido.DadosListagemPedido;
import org.buyhub.service.DTOs.pedido.RepositoryPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedido", description = "CRUD de pedido.")
public class ControllerPedido {

    @Autowired
    private RepositoryPedido repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastro de pedido", description = "Endpoint do cadastro de novos pedidos.")
    public ResponseEntity<DadosListagemPedido> cadastrar(@RequestBody @Valid DadosCadastroPedido dados, UriComponentsBuilder uriBuilder) {
        var pedido = new ComprasPedido(dados);
        repository.save(pedido);
        var uri = uriBuilder.path("/pedidos/{idPedido}").buildAndExpand(pedido.getIdPedido()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPedido(pedido));
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Listagem de pedidos", description = "Endpoint da listagem de pedidos cadastrados.")
    public ResponseEntity<Page<DadosListagemPedido>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListagemPedido::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping(path = "/{idPedido}", produces = "application/json")
    @Operation(summary = "Exibir pedido", description = "Endpoint da exibição de um único pedido cadastrado.")
    public ResponseEntity<DadosListagemPedido> exibir(@PathVariable Long idPedido) {
        var pedido = repository.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado para este ID :: " + idPedido));
        return ResponseEntity.ok(new DadosListagemPedido(pedido));
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Atualizar pedido", description = "Endpoint da atualização de um único pedido cadastrado.")
    public ResponseEntity<DadosListagemPedido> atualizar(@RequestBody @Valid DadosAtualizacaoPedido dados) {
        var pedido = repository.findById(dados.idPedido())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado para este ID :: " + dados.idPedido()));
        pedido.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemPedido(pedido));
    }

    @DeleteMapping(path = "/{idPedido}")
    @Transactional
    @Operation(summary = "Excluir pedido", description = "Endpoint da exclusão de um único pedido cadastrado.")
    public ResponseEntity<String> excluir(@PathVariable Long idPedido) {
        var pedido = repository.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado para este ID :: " + idPedido));
        repository.delete(pedido);
        return ResponseEntity.ok("Pedido " + idPedido + " deletado.");
    }
}
