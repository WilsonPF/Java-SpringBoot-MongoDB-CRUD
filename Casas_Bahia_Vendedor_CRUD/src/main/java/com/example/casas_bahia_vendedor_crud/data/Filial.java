package com.example.casas_bahia_vendedor_crud.data;

import java.util.Date;

public class Filial {

    public Filial(Integer id, String nome, String cnpj, String cidade, String uf, String tipo, boolean ativo, Date dataCadastro, Date ultimaAtualizacao){
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.cidade = cidade;
        this.uf = uf;
        this.tipo = tipo;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public int id;

    public String nome;

    public String cnpj;

    public String cidade;

    public String uf;

    public String tipo;

    public boolean ativo;

    public Date dataCadastro;

    public Date ultimaAtualizacao;
}
