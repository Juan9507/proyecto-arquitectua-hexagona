package co.com.sofka.arquitectura.limpia.model.viaje.gateways;

import co.com.sofka.arquitectura.limpia.model.viaje.Viaje;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ViajeRepository {

    Flux<Viaje> buscarViaje();
    Mono<Viaje> crearViaje(Viaje viaje);
    Mono<Viaje> buscarViajePorId(Integer id);
}
