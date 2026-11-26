package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.repo.ClienteRepository;
import com.example.demo.repo.ProductoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@BeforeEach
	void cleanUp() {
		productoRepository.deleteAll();
		clienteRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void crearYConsultarProductos() throws Exception {
		String payload = """
				{
				  "nombre": "Creatina",
				  "descripcion": "Creatina de 500 g",
				  "lote": "12345",
				  "precioUnitario": 500.50,
				  "fechaCaducidad": "2027-05-04"
				}
				""";

		mockMvc.perform(post("/productos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.nombre").value("Creatina"))
				.andExpect(jsonPath("$.lote").value("12345"));

		mockMvc.perform(get("/productos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].nombre").value("Creatina"));
	}

	@Test
	void crearYConsultarClientes() throws Exception {
		String payload = """
				{
				  "nombre": "Ana",
				  "apellidos": "Lopez Perez",
				  "correo": "ana.lopez@example.com",
				  "telefono": "5551234567",
				  "fechaRegistro": "2026-11-25"
				}
				""";

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(payload))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.nombre").value("Ana"))
				.andExpect(jsonPath("$.correo").value("ana.lopez@example.com"));

		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].nombre").value("Ana"));
	}
}
