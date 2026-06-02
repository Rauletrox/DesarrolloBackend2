package com.minimarket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MinimarketApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void loginDevuelveJwt() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"gerente\",\"password\":\"Gerente123\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tipo").value("Bearer"))
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andExpect(jsonPath("$.roles[0]").value("ROLE_GERENTE"));
	}

	@Test
	void clienteNoPuedeAdministrarUsuariosYGerenteSiPuede() throws Exception {
		String tokenCliente = obtenerToken("cliente", "Cliente123");
		String tokenGerente = obtenerToken("gerente", "Gerente123");

		mockMvc.perform(get("/api/usuarios")
						.header("Authorization", "Bearer " + tokenCliente))
				.andExpect(status().isForbidden());

		mockMvc.perform(get("/api/usuarios")
						.header("Authorization", "Bearer " + tokenGerente))
				.andExpect(status().isOk());
	}

	private String obtenerToken(String username, String password) throws Exception {
		String response = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		JsonNode json = objectMapper.readTree(response);
		return json.get("token").asText();
	}
}
