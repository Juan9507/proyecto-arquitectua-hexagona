package co.com.sofka.arquitectura.limpia.rabbitmq.publisher;

import co.com.sofka.arquitectura.limpia.model.persona.gateways.PersonaPublicador;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Creación del publicador
 */

@Component
@EnableDirectAsyncGateway // sirve para disparar los comandos
@RequiredArgsConstructor
public class PublicadorRabbitmq implements PersonaPublicador {

    // permite la emisión de comandos y solicitudes de consultas asíncronas.
    private final DirectAsyncGateway disparadorComando;

    // Creación de la cola
    static final String PERSONAS_VIAJERAS = "personas.viajeras";

    @Override
    public Mono<String> publicarPersonasViajeras() {
        // comando de reactive common
        final Command<String> comando = new Command<>(
                PERSONAS_VIAJERAS,
                UUID.randomUUID().toString(),
                "hola"
        );

        return disparadorComando.sendCommand(
            comando, "proyecto-arquitectura-hexagonal"
        ).thenReturn("mensaje publicado");
    }
}
