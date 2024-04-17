package com.example.casas_bahia_vendedor_crud.models;

import com.example.casas_bahia_vendedor_crud.data.Filial;

public class VendedorDTO {
    public VendedorDTO(){
    }

    public VendedorDTO(String matricula, String nome, String dataNascimento, String cpf, String cnpj, String email, String contrato, Filial filial){
        this.matricula = matricula;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.email = email;
        this.contrato = contrato;
        this.filial = filial;
    }

    public String matricula;

    public String nome;

    public String dataNascimento;

    public String cpf;

    public String cnpj;

    public String email;

    public String contrato;

    public Filial filial;
}
