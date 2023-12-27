package com.project.facturacion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.facturacion.dao.FacturaDao;
import com.project.facturacion.documents.Factura;
import com.project.facturacion.model.PayloadKafka;

@Service
public class FacturacionServiceImpl implements FacturacionService {

	private static Logger logger = LoggerFactory.getLogger(FacturacionServiceImpl.class);

	@Autowired
	private FacturaDao facturaDao;

	@Autowired
	private JavaMailSender mail;

	@Override
	public void sendMessage(PayloadKafka request) {

		Factura factura = new Factura();
		factura.setDetalle(request);

		logger.info("Registrando factura en la base de datos");
		facturaDao.save(factura).subscribe();

		logger.info("Enviando factura por correo");

		SimpleMailMessage email = new SimpleMailMessage();

		StringBuilder mensaje = new StringBuilder("DATOS DE LA FACTURA:\n\n");
		mensaje.append("* Fecha: ").append(factura.getDetalle().getFecha()).append("\n");
		mensaje.append("* Cliente: ").append(factura.getDetalle().getUsuario().getNombres())
				.append(" ").append(factura.getDetalle().getUsuario().getApellidos()).append("\n");
		mensaje.append("* Total: ").append(factura.getDetalle().getTotal()).append("\n");

		mensaje.append("\nDetalle de Productos:\n");
		
		factura.getDetalle().getCarrito().getItems().forEach(producto -> mensaje.append("  - ").append(producto.getNombreProducto())
				.append(", Cantidad: ").append(producto.getCantidad())
				.append(", Subtotal: ").append(producto.getTotal()).append("\n"));

		email.setTo(request.getUsuario().getEmail());
		email.setFrom("correo.proyecto.bootcamp@gmail.com");
		email.setSubject("Orden de compra");
		email.setText(mensaje.toString());

		mail.send(email);
	}

}
