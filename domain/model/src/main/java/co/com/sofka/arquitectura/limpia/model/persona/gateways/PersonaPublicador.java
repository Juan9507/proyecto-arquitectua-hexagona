package co.com.sofka.arquitectura.limpia.model.persona.gateways;

import co.com.sofka.arquitectura.limpia.model.persona.Persona;
import reactor.core.publisher.Mono;

/**
 * gateways
 * actúa como un único punto de entrada para un grupo definido de microservicios.
 */
public interface PersonaPublicador {

    Mono<String> publicarPersonasViajeras();

    Mono<Boolean> save(Persona persona);

    Mono<Persona> exist(Persona persona);
}
