package com.project.facturacion.eventflow;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import com.project.facturacion.model.PayloadKafka;
import com.project.facturacion.service.FacturacionService;

@Component
public class DomainEventListener {

	private static Logger logger = LoggerFactory.getLogger(DomainEventListener.class);
	
	@Autowired
	private FacturacionService facturacionService;
	
	@Bean
	public Consumer<GenericMessage<PayloadKafka>> notificacionfacturacion() {
		return value -> {
			PayloadKafka payload = value.getPayload();
			logger.info("Mensaje obtenido: " + payload.getId());
			facturacionService.sendMessage(payload);
		};	
	}
}
