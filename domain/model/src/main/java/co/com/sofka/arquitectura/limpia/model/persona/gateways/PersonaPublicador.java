package co.com.sofka.arquitectura.limpia.model.persona.gateways;

import reactor.core.publisher.Mono;

public interface PersonaPublicador {

    Mono<String> publicarPersonasViajeras();
}
