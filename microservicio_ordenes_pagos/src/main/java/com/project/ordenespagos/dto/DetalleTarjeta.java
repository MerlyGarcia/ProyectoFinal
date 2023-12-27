package com.project.ordenespagos.dto;

import lombok.Data;

@Data
public class DetalleTarjeta {

    private String numeroTarjeta;
    private String fechaExpiracion;
    private String cvv;
    private TipoTarjeta tipoTarjeta;
}
