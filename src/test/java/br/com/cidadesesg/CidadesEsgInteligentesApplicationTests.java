package br.com.cidadesesg;

import br.com.cidadesesg.controller.HealthController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CidadesEsgInteligentesApplicationTests {
    @Test
    void healthEndpointReturnsUpStatus() {
        var response = new HealthController().health();
        assertEquals("UP", response.get("status"));
        assertEquals("Cidades ESG Inteligentes", response.get("application"));
        assertNotNull(response.get("timestamp"));
    }
}
