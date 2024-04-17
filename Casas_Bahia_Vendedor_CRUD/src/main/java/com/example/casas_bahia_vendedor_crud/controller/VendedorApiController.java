package com.example.casas_bahia_vendedor_crud.controller;

import com.example.casas_bahia_vendedor_crud.data.Vendedor;
import com.example.casas_bahia_vendedor_crud.service.VendedorService;
import com.example.casas_bahia_vendedor_crud.models.VendedorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedor")
public class VendedorApiController {

    @Autowired
    private VendedorService vendedorService;

    @GetMapping("/findAll")
    public List<VendedorDTO> findAll(){
        return vendedorService.findAll();
    }

    @GetMapping("/findByMatricula")
    public VendedorDTO findByMatricula(@RequestParam String matricula){
        if (matricula == null || matricula.isBlank()){
            return new VendedorDTO();
        }

        var vendedor = vendedorService.findByMatricula(matricula);
        if (vendedor.isPresent()){
            return vendedor.get();
        }

        return new VendedorDTO();
    }

    @PutMapping("/save")
    public ResponseEntity<String> save(@RequestBody VendedorDTO vendedor){
        var vendedorSavedResult = vendedorService.saveVendedor(vendedor);
        if (vendedorSavedResult.success){
            return ResponseEntity.ok(vendedorSavedResult.message);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(vendedorSavedResult.message);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody VendedorDTO vendedor){
        if (vendedor.matricula == null || vendedor.matricula.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Matrícula inválida.");
        }

        var vendedorUpdatedResult = vendedorService.updateVendedor(vendedor);
        if (vendedorUpdatedResult.success){
            return ResponseEntity.ok(vendedorUpdatedResult.message);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(vendedorUpdatedResult.message);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(String matricula){
        if (matricula == null || matricula.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Matrícula inválida.");
        }

        var vendedorDeletedResult = vendedorService.deleteVendedor(matricula);
        if (vendedorDeletedResult.success){
            return ResponseEntity.ok(vendedorDeletedResult.message);
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(vendedorDeletedResult.message);
        }
    }
}
