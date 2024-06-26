package org.buyhub.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.buyhub.service.DTOs.orcamento.DadosAtualizacaoOrcamento;
import org.buyhub.service.DTOs.orcamento.DadosCadastroOrcamento;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "t_compra_orcamento")
public class CompraOrcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrcamento;

    @ManyToOne(targetEntity = CompraRequisicao.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_requisicao")
    private List<CompraRequisicao> compraRequisicao;

    private Date dtOrcamento;

    @ManyToOne(targetEntity = CompraCliente.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioCliente")
    private List<CompraCliente> compraCliente;

    @ManyToOne(targetEntity = CompraFornecedor.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "cnpjFornecedor")
    private List<CompraFornecedor> compraFornecedor;

    @ManyToOne(targetEntity = CompraProduto.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produto")
    private List<CompraProduto> compraProduto;

    public CompraOrcamento(DadosCadastroOrcamento dados) {
        this.idOrcamento = dados.idOrcamento();
        this.compraRequisicao = dados.compraRequisicao();
        this.dtOrcamento = dados.dtOrcamento();
        this.compraCliente = dados.compraCliente();
        this.compraFornecedor = dados.compraFornecedor();
        this.compraProduto = dados.compraProduto();
    }

   public void atualizarInformacoes(DadosAtualizacaoOrcamento dados) {
        if (dados.idOrcamento() != null) {
            this.idOrcamento = dados.idOrcamento();
        }
        if (dados.compraRequisicao() != null) {
            this.compraRequisicao = dados.compraRequisicao();
        }
        if (dados.dtOrcamento() != null) {
            this.dtOrcamento = dados.dtOrcamento();
        }
        if (dados.compraCliente() != null) {
            this.compraCliente = dados.compraCliente();
        }
        if (dados.compraFornecedor() != null) {
            this.compraFornecedor = dados.compraFornecedor();
        }
        if (dados.compraProduto() != null) {
            this.compraProduto = dados.compraProduto();
        }
   }
}
