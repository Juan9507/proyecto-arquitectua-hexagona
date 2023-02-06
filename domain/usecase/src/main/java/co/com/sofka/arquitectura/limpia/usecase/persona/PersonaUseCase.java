package co.com.sofka.arquitectura.limpia.usecase.persona;

import co.com.sofka.arquitectura.limpia.model.persona.Persona;
import co.com.sofka.arquitectura.limpia.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonaUseCase {

    private final PersonaRepository personaRepository;

    public Flux<Persona> buscarPersonas() {
        return personaRepository.buscarPersonas();
    }

    public Mono<Persona> buscarPersonaPorId(Integer id){
        return personaRepository.buscarPersonaPorId(id);
    }

    public Mono<Persona> guardarPersona(Persona persona){
        return personaRepository.crearPersona(persona);
    }

}
