package co.com.sofka.arquitectura.limpia.rabbitmq.publisher;

import co.com.sofka.arquitectura.limpia.model.persona.Persona;
import co.com.sofka.arquitectura.limpia.model.persona.gateways.PersonaPublicador;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.api.DirectAsyncGateway;
import org.reactivecommons.async.impl.config.annotations.EnableDirectAsyncGateway;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Creación del publicador
 */

@Component
@EnableDirectAsyncGateway // sirve para usar las funcionalidades de directAsyncGateway
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

    /**
     * Envio de comando con reactivecommons
     * @param persona
     * @return
     */
    @Override
    public Mono<Boolean> save(Persona persona) {
        String uuid = UUID.randomUUID().toString(); // identificador unico de mensaje
        final Command<Persona> command = new Command<>("persona.save", uuid, persona);
        return disparadorComando
                .sendCommand(command, "proyecto-arquitectura-hexagonal")
                .thenReturn(Boolean.TRUE);
    }

    /**
     * Envio de comando pero con AsyncQuery lo que cambia es que este si espera una respuesta requestReply
     * @param persona
     * @return
     */
    @Override
    public Mono<Persona> exist(Persona persona) {
        final AsyncQuery<Persona> query = new AsyncQuery<>("persona.exist", persona);
        return disparadorComando
                .requestReply(query, "proyecto-arquitectura-hexagonal", Persona.class)
                .map(person -> {
                    return person;
                })
                .switchIfEmpty(Mono.empty())
                .onErrorResume(e -> {
                    return Mono.just(new Persona());
                });
    }
}
