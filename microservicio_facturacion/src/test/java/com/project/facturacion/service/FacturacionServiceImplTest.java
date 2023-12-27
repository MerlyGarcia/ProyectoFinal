package com.project.facturacion.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.project.facturacion.dao.FacturaDao;
import com.project.facturacion.documents.Factura;
import com.project.facturacion.model.Carrito;
import com.project.facturacion.model.DetalleTarjeta;
import com.project.facturacion.model.EstadoOrden;
import com.project.facturacion.model.ItemCarrito;
import com.project.facturacion.model.PayloadKafka;
import com.project.facturacion.model.Rol;
import com.project.facturacion.model.TipoTarjeta;
import com.project.facturacion.model.Usuario;

import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
public class FacturacionServiceImplTest {

	@InjectMocks
	private FacturacionServiceImpl facturacionService;

	@Mock
	private FacturaDao facturaDao;

	@Mock
	private JavaMailSender mail;
	
	private PayloadKafka request;
    private Usuario u;
    private Carrito c;
    private DetalleTarjeta tarjeta;
    private Factura factura;
    
    @BeforeEach
    public void setUp() {
    	request = new PayloadKafka();
    	
    	u = new Usuario();
    	
    	u.setId("1");
    	u.setNombres("Miguel");
    	u.setApellidos("Sanchez Gonzales");
    	u.setEmail("merlyf18@hotmail.com");
		u.setFechaNacimiento(LocalDate.of(1996, 10, 27));
		u.setRol(Rol.USER);
		
		c = new Carrito();
	    List<ItemCarrito> items = new ArrayList<>();
	    ItemCarrito item = new ItemCarrito();
    	item.setId("1");
    	item.setIdProducto("1");
    	item.setNombreProducto("Pulsera");
    	item.setTotal(20D);
    	item.setCantidad(2);
    	
    	items = new ArrayList<>();
    	items.add(item);
    	
    	c.setId("1");
    	c.setFecha(LocalDate.now());
    	c.setItems(items);
    	c.setTotal(50d);
	    
    	tarjeta = new DetalleTarjeta();
    	tarjeta.setNumeroTarjeta("1234567895623104");
    	tarjeta.setTipoTarjeta(TipoTarjeta.DEBITO);
    	tarjeta.setCvv("289");
    	tarjeta.setFechaExpiracion("08/26");
    	
    	request.setId("6586a8756c5a9223644f1605");
    	request.setUsuario(u);
    	request.setCarrito(c);
    	request.setStatus(EstadoOrden.CREADA);
    	request.setTotal(c.getTotal());
    	request.setFecha(c.getFecha());
    	request.setTarjeta(tarjeta);
    	request.setDireccion("Av Larco");
    	
    	factura = new Factura();
    	factura.setId("12345");
    	factura.setDetalle(request);
    }
    
    @Test
    void sendMessage() {
    	
    	when(facturaDao.save(any(Factura.class))).thenReturn(Mono.just(factura));
    	
       facturacionService.sendMessage(request);

       verify(facturaDao, times(1)).save(any(Factura.class));
       verify(mail, times(1)).send(any(SimpleMailMessage.class));
    }
    
}
