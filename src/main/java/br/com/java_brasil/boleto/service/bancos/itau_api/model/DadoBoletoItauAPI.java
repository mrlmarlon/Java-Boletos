package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DadoBoletoItauAPI {

    private String descricao_instrumento_cobranca;
    private String forma_envio;
    private String tipo_boleto;
    private ProtestoBoletoItauAPI protesto;
    private NegativacaoBoletoItauAPI negativacao;
    private PagadorBoletoItauAPI pagador;
    private SacadorAvalistaBoletoItauAPI sacador_avalista;
    private String codigo_carteira;
    private String codigo_tipo_vencimento;
    private String valor_total_titulo;
    private List<DadosIndividuaisBoletoItauAPI> dados_individuais_boleto;
    private String codigo_especie;
    private String data_emissao;
    private String pagamento_parcial;
    private String quantidade_maximo_parcial;
    private String valor_abatimento;
    private JurosBoletoItauAPI juros;
    private MultaBoletoItauAPI multa;
    private DescontoBoletoItauAPI desconto;
    private List<MensagemCobrancaBoletoItauAPI> lista_mensagem_cobranca;
    private RecebimentoDivergenteBoletoItauAPI recebimento_divergente;
    private Boolean desconto_expresso;
    private DadosQrCodeBoletoItauAPI dados_qrcode;
}
