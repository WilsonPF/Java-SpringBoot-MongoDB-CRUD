package com.example.casas_bahia_vendedor_crud.service;

import com.example.casas_bahia_vendedor_crud.data.Filial;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FilialService {

    public Filial getFilial(){
        return new Filial(
                1,
                "Filial 1",
                "11.111.111/0001-00",
                "Campos Novos",
                "SC",
                "Filial",
                true,
                new Date(),
                new Date());
    }
}
