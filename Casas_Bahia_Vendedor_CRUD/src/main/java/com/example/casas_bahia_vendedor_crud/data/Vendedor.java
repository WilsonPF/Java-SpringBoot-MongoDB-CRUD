package com.example.casas_bahia_vendedor_crud.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection  = "Vendedores")
public class Vendedor {

    public Vendedor(){
    }

    public Vendedor(String matricula, String nome, Date dataNascimento, String cpf, String cnpj, String email, Contrato contrato, Filial filial){
        this.matricula = matricula;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.email = email;
        this.contrato = contrato;
        this.filial = filial;

        if (contrato == Contrato.CLT){
            this.cpf = cpf;
        }
        else{
            this.cnpj = cnpj;
        }
    }

    @Id
    private String matricula;

    private String nome;

    private Date dataNascimento;

    private String cpf;

    private String cnpj;

    private String email;

    private Contrato contrato;

    private Filial filial;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public Date getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCnpj() { return cnpj; }

    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Contrato getContrato() { return contrato; }

    public void setContrato(Contrato contrato) { this.contrato = contrato; }

    public Filial getFilial() { return filial; }

    public void setFilial(Filial filial) { this.filial = filial; }
}
