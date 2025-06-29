package com.perfulandia.service;

import com.perfulandia.service.user.repository.RolRepository;
import com.perfulandia.service.user.repository.UsuarioRepository;
import com.perfulandia.service.inventory.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
class ServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@MockBean
	private UsuarioRepository usuarioRepository;

	@MockBean
	private RolRepository rolRepository;

	@MockBean
	private ProductoRepository productoRepository;

}
