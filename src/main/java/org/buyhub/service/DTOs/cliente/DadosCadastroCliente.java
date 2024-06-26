package org.buyhub.service.DTOs.cliente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.buyhub.domain.entities.CompraRequisicao;

import java.util.List;

public record DadosCadastroCliente(
        @NotBlank
        @Email
        String usuarioCliente,
        @NotBlank
        long cnpjCliente,
        @NotBlank
        String nmCliente,
        @NotBlank
        String senhaCliente,
        @NotBlank
        String setorCliente,
        @NotNull
        @Valid
        List<CompraRequisicao> requisicao
        ) {
}
