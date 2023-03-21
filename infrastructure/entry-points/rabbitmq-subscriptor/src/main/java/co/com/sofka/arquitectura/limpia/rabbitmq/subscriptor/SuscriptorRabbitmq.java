package co.com.sofka.arquitectura.limpia.rabbitmq.subscriptor;

import co.com.sofka.arquitectura.limpia.model.persona.Persona;
import co.com.sofka.arquitectura.limpia.usecase.persona.PersonaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.annotations.EnableCommandListeners;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import static org.reactivecommons.async.api.HandlerRegistry.register;

@Log
@Configuration
@EnableCommandListeners
@RequiredArgsConstructor
public class SuscriptorRabbitmq {

    private final PersonaUseCase personaUseCase;

    static final String PERSONAS_VIAJERAS = "personas.viajeras";

    @Bean
    public HandlerRegistry comandoSuscriptor(){
        return register().handleCommand(PERSONAS_VIAJERAS,
                p -> {
                    personaUseCase.buscarPersonas();
                    return Mono.empty();
                }, String.class);
    }

    @Bean
    public HandlerRegistry commandSaveUserubscription() {
        return register().handleCommand("persona.save",
                persona -> {
                    personaUseCase.guardarPersona(persona.getData());
                            return Mono.empty();
                }, Persona.class);
    }

    @Bean
    public HandlerRegistry queryExistSubscription() {
        return register()
                .serveQuery("persona.exist", persona -> {
                    log.info(() -> String.valueOf(persona));
                    return Mono.just(persona);
                }, Persona.class);
    }
}
