package co.com.sofka.arquitectura.limpia.usecase.persona;

import co.com.sofka.arquitectura.limpia.model.persona.Persona;
import co.com.sofka.arquitectura.limpia.model.persona.gateways.PersonaPublicador;
import co.com.sofka.arquitectura.limpia.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonaUseCase {

    private final PersonaRepository personaRepository;
    private final PersonaPublicador personaPublicador;

    public Flux<Persona> buscarPersonas() {
        return personaRepository.buscarPersonas();
    }

    public Mono<Persona> buscarPersonaPorId(Integer id){
        return personaRepository.buscarPersonaPorId(id);
    }

    public Mono<Persona> guardarPersona(Persona persona){
        return personaRepository.crearPersona(persona);
    }

    public Mono<String> buscarPersonaPorComando(){
        return personaPublicador.publicarPersonasViajeras();
    }

    /**
     * Metodo para guardar una persona por medio de comando de reativecommons
     * @param persona - recibe la persona
     * @return - boolean si creo o no
     */
    public Mono<Boolean> save(Persona persona){
        return personaPublicador.save(persona);
    }

    /**
     * Metodo para saber si existe una persona por medio de AsyncQuery de reativecommons
     * @param persona - recibe la persona
     * @return - boolean si existe o no
     */
    public Mono<Boolean> exist(Persona persona){
        return personaPublicador.exist(persona).thenReturn(Boolean.TRUE);
    }

}
