package com.project.facturacion.service;

import com.project.facturacion.model.PayloadKafka;

public interface FacturacionService {

	void sendMessage(PayloadKafka request);
}
