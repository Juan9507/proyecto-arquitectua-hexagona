package co.com.sofka.arquitectura.limpia.model.persona.gateways;

import co.com.sofka.arquitectura.limpia.model.persona.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaRepository {
    Flux<Persona> buscarPersonas();
    Mono<Persona> crearPersona(Persona persona);
    Mono<Persona> buscarPersonaPorId(Integer id);
}
