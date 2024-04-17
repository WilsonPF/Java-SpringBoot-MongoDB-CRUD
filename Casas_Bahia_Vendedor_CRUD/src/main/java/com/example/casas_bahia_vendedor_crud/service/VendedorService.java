package com.example.casas_bahia_vendedor_crud.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import com.example.casas_bahia_vendedor_crud.data.Vendedor;
import com.example.casas_bahia_vendedor_crud.data.Contrato;
import com.example.casas_bahia_vendedor_crud.data.repository.VendedorRepository;
import com.example.casas_bahia_vendedor_crud.models.ServiceResult;
import com.example.casas_bahia_vendedor_crud.models.VendedorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VendedorService {
    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private FilialService filialService;

    public List<VendedorDTO> findAll() {
        var vendedores = vendedorRepository.findAll();
        var vendedoresDTO = new ArrayList<VendedorDTO>();

        for (var vendedor : vendedores) {
            vendedoresDTO.add(mapVendedorToVendedorDTO(vendedor));
        }

        return vendedoresDTO;
    }

    public Optional<VendedorDTO> findByMatricula(String matricula) {
        var vendedorOptional = vendedorRepository.findByMatricula(matricula);
        if (vendedorOptional.isPresent()){
            return Optional.of(mapVendedorToVendedorDTO(vendedorOptional.get()));
        }

        return Optional.empty();
    }

    public ServiceResult saveVendedor(VendedorDTO vendedorDTO) {
        try {
            var vendedorValidation = validateVendedor(vendedorDTO);
            if (!vendedorValidation.isBlank()){
                return new ServiceResult(false, vendedorValidation);
            }

            var contrato = getContrato(vendedorDTO.contrato).get();
            var matricula = vendedorRepository.count() + "-" + contrato.toString();

            Date dataNascimento = null;
            if (vendedorDTO.dataNascimento != null){
                DateFormat sdf = new SimpleDateFormat("d-M-y");
                dataNascimento = sdf.parse(vendedorDTO.dataNascimento);
            }

            var vendedor = new Vendedor(
                    matricula,
                    vendedorDTO.nome,
                    dataNascimento,
                    vendedorDTO.cpf,
                    vendedorDTO.cnpj,
                    vendedorDTO.email,
                    contrato,
                    filialService.getFilial()
            );

            vendedorRepository.save(vendedor);
            return new ServiceResult(true, "Vendedor cadastrado com sucesso.");
        } catch (Exception e){
            return new ServiceResult(false, "Uma exceção ocorreu ao tentar cadastrar o vendedor.");
        }
    }

    public ServiceResult updateVendedor(VendedorDTO vendedorDTO) {
        try {
            var vendedorOptional = vendedorRepository.findByMatricula(vendedorDTO.matricula);
            if (vendedorOptional.isEmpty()){
                return new ServiceResult(false, "Vendedor com matrícula " + vendedorDTO.matricula + " não foi encontrado.");
            }

            var vendedorValidation = validateVendedor(vendedorDTO);
            if (!vendedorValidation.isBlank()){
                return new ServiceResult(false, vendedorValidation);
            }

            var contrato = getContrato(vendedorDTO.contrato);

            Date dataNascimento = null;
            if (vendedorDTO.dataNascimento != null){
                DateFormat sdf = new SimpleDateFormat("d-M-y");
                dataNascimento = sdf.parse(vendedorDTO.dataNascimento);
            }

            var vendedor = vendedorOptional.get();
            vendedor.setNome(vendedorDTO.nome);
            vendedor.setDataNascimento(dataNascimento);
            vendedor.setEmail(vendedorDTO.email);
            vendedor.setContrato(contrato.get());

            if (contrato.get() == Contrato.CLT){
                vendedor.setCpf(vendedorDTO.cpf);
                vendedor.setCnpj(null);
            }
            else{
                vendedor.setCnpj(vendedorDTO.cnpj);
                vendedor.setCpf(null);
            }

            vendedorRepository.save(vendedor);
            return new ServiceResult(true, "Vendedor atualizado com sucesso.");
        } catch (Exception e) {
            return new ServiceResult(false, "Uma exceção ocorreu ao tentar atualizar o vendedor.");
        }
    }

    public ServiceResult deleteVendedor(String matricula) {
        try {
            var vendedor = vendedorRepository.findByMatricula(matricula);
            if (vendedor.isEmpty()){
                return new ServiceResult(false, "Vendedor com matrícula " + matricula + " não foi encontrado.");
            }

            vendedorRepository.delete(vendedor.get());
            return new ServiceResult(true, "Vendedor removido com sucesso.");
        } catch (Exception e) {
            return new ServiceResult(false, "Uma exceção ocorreu ao tentar remover o vendedor.");
        }
    }

    private VendedorDTO mapVendedorToVendedorDTO(Vendedor vendedor){
        return new VendedorDTO(
            vendedor.getMatricula(),
            vendedor.getNome(),
            vendedor.getDataNascimento() != null ? vendedor.getDataNascimento().toString() : null,
            vendedor.getCpf(),
            vendedor.getCnpj(),
            vendedor.getEmail(),
            vendedor.getContrato().getDisplayName(),
            vendedor.getFilial()
        );
    }

    private String validateVendedor(VendedorDTO vendedorDTO){
        if (vendedorDTO.nome == null || vendedorDTO.email == null || vendedorDTO.contrato == null ||
                (vendedorDTO.cnpj == null && vendedorDTO.cpf == null)){
            return "Vendedor está com informações incompletas.";
        }

        if (vendedorDTO.dataNascimento != null && !isDateValid(vendedorDTO.dataNascimento)){
            return "Data de nascimento inválida.";
        }

        if (!isEmailValid(vendedorDTO.email)){
            return "E-mail inválido.";
        }

        var contrato = getContrato(vendedorDTO.contrato);
        if (contrato.isEmpty()){
            return "Contrato inválido.";
        }

        if (contrato.get() == Contrato.CLT){
            if (vendedorDTO.cpf == null || !isCpfValid(vendedorDTO.cpf)){
                return "CPF inválido.";
            }
        }
        else if (vendedorDTO.cnpj == null || !isCnpjValid(vendedorDTO.cnpj)){
            return "CNPJ inválido.";
        }

        return "";
    }

    private boolean isDateValid(String date){
        DateFormat sdf = new SimpleDateFormat("d-M-y");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private boolean isCnpjValid(String cnpj){
        if (!cnpj.matches("\\d+") && !CNPJ_PATTERN.matcher(cnpj).matches()) {
            return false;
        }

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int sum = 0;
        int weight = 2;
        for (int i = 11; i >= 0; i--) {
            sum += (cnpj.charAt(i) - '0') * weight;
            weight = (weight == 9) ? 2 : weight + 1;
        }
        int digit1 = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

        if (digit1 != (cnpj.charAt(12) - '0')){
            return false;
        }

        sum = 0;
        weight = 2;
        for (int i = 12; i >= 0; i--) {
            sum += (cnpj.charAt(i) - '0') * weight;
            weight = (weight == 9) ? 2 : weight + 1;
        }
        int digit2 = (sum % 11 < 2) ? 0 : 11 - (sum % 11);

        return digit2 == (cnpj.charAt(13) - '0');
    }

    private boolean isCpfValid(String cpf){
        if(!cpf.matches("\\d+") && !CPF_PATTERN.matcher(cpf).matches()){
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (cpf.charAt(i) - '0') * (10 - i);
        }

        int digit1 = 11 - (sum % 11);
        if (digit1 > 9) {
            digit1 = 0;
        }

        if (digit1 != (cpf.charAt(9) - '0')){
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digit2 = 11 - (sum % 11);

        if (digit2 > 9) {
            digit2 = 0;
        }

        return digit2 == (cpf.charAt(10) - '0');
    }

    private boolean isEmailValid(String email){
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private Optional<Contrato> getContrato(String tipoContrato){
        switch(tipoContrato){
            case "CLT":
                return Optional.of(Contrato.CLT);
            case "Pessoa Jurídica":
                return Optional.of(Contrato.PJ);
            case "Outsourcing (terceirizado)":
                return Optional.of(Contrato.OUT);
            default:
                return Optional.empty();
        }
    }
}
