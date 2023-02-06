# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

# Cómo se crea el proyecto 

### Debes crear una carperta donde vas a alojar el proyecto, dentro de esa carpeta agregas el archivo build.gradle con la siguiente linea
```CS
plugins {
    id "co.com.bancolombia.cleanArchitecture" version "2.4.9"
}
```

### Luego en la terminar agregar el siguiente comando para la creación del proyecto
Nota:
1. El nombre del package --package <b>Colocar el que desees</b>
2. El nombre del proyecto --name <b>tambien el nombre de tu proyecto a crear</b>
3. El --type: reactive en este caso <b>Depende del proyecto que vayas a crear</b>
```CS
    gradle cleanArchitecture --package=co.com.sofka.arquitectura.limpia --type=reactive --name=proyecto-arquitectura-hexagonal --coverage=jacoco --lombok=true
```


# Configuraciónes e implementaciones para la configuración del proyecto 

### Creación de los modelos
Nota
1. En el nombre del modelo colocar el de tu proyecto o seguir el de este caso
2. recuerda que estos comando se realizan desde la terminal de comandos
3. En la capa de domain package model se veran reflejados nuestros modelos

```CS
gradle generateModel --name=persona
```
```CS
gradle generateModel --name=viaje
```

### Creación de nuestro caso de uso
nota
1. Al crear el caso de uso se le agrega a la clase de manera autonoma el <b>UseCase</b> asi que quedaria PersonaUseCase
2. sSe veran visibles en la capa de domain 

```CS
gradle generateUseCase --name=persona
```

### Creación de los drivers adapters

Capa para la persistencia de los datos ej: mongodb, jpa

Nota
1. Agregaremos tres driver adapter ya que utilizaremos brocker de mensajeria con RabbitMq y no hay un adapter creado para este caso asi que vamos a utilizar <b>jpa</b>, <b>restconsumer</b> y <b>generic</b>
2. En el caso explicito para RabbitMQ utilizamos el generic
2. Los dirver adapter se visualizan en la capa de infrasturcture package driven-adapters

```CS
gradle generateDrivenAdapter --type=jpa
```

```CS
gradle generateDrivenAdapter --type=restconsumer
```

```CS
gradle generateDrivenAdapter --type=generic --name=rabbitmq-publisher
```

### Creación de los entry points
Nota
1. los entry point se crean en la capa de infastructure package entry-points

```CS
gradle generateEntryPoint --type=restmvc --name=api-rest
```

```CS
gradle generateEntryPoint --type=generic --name=rabbitmq-subscriptor
```

## Configuración application.yaml

### Se encuentra en la capa de application, resources

Configuración RabbitMQ se coloca debajo de devtools
```CS
  ....
  devtools: // linea de codigo ya existente
    add-properties: false
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
```

dependencia para ayudar con las configuraciones, se coloca debajo de profiles

```CS
  ....
  profiles: // linea de codigo ya existente
    include: null
  main:
    allow-bean-definition-overriding: true
```

Configuración base de datos

Colocar la configuracion dependiendo su gestor de base de datos, username y password

```CS
  ......
  datasource:
    url: "jdbc:mysql://${MYSQL_HOST:localhost}:3306/agencia_viajes_test"
    username: "root"
    password: "0709"
    driverClassName: "com.mysql.cj.jdbc.Driver"
```

Configuración jpa, en mi caso con mysql

```CS
  ....
   jpa:
    databasePlatform: "org.hibernate.dialect.MySQL5InnoDBDialect"
    properties:
      hibernate:
        ddl-auto: update
        format_sql: true
    show-sql: true
```

 ## Configurar el build.gradle en infrastructure, driven-adapter, jpa-repository, con las dependencias necesarias para funcionar

En este caso quitamos esta dependencia ya que no se necesita

```CS
implementation 'com.github.bancolombia:aws-secrets-manager-sync:3.2.0'
```

También se quita la de h2 ya que utilizaos mysql cómo base de datos

```CS
 runtimeOnly 'com.h2database:h2'
```

Se implementa apache commons.

Commons Lang es un proyecto de Jakarta que extiende la funcionalidad del
paquete estándar java.lang.

Nos proporciona un gran número de clases que nos hacen ahorrar tiempo en el
desarrollo de nuestros proyectos.

```CS
implementation 'org.apache.commons:commons-lang3:3.12.0'
```

Se implementa la dependencia de base de datos mysql

```CS
implementation "mysql:mysql-connector-java:5.1.37"
```

## Configurar el build.gradle en infrastructure, driven-adapters, rabbitmq-publisher

Se implementa las dependencias de reactive commons para la conexión con RabbitMQ

```CS
implementation 'org.reactivecommons:async-commons-api:0.6.2'
implementation 'org.reactivecommons:async-commons-starter:0.6.2'
```

## Configurar el build.gradle en infrastructure, driven-adapters, rest-consumer

se puede comunicar con el caso de uso asi que lo implementamos

```CS
implementation project(':usecase')
```

## Configurar el build.gradle en infrastructure, entry-points, rabbitmq-subscriptor

Se implementan estas dependencias

```CS
implementation 'org.springframework.amqp:spring-rabbit'
implementation 'com.fasterxml.jackson.core:jackson-databind:2.14.1'
implementation 'org.reactivecommons:async-commons-api:0.6.2'
implementation 'org.reactivecommons:async-commons-starter:0.6.2'
```



### implementamos los modelos en la capa domain y los metodos del repository en el gateway 

### implementar en infrastructure, driven-adapter lo que necesitamos para la persistencia de datos

nota

* en este caso crearemos 3 carpertas
    
    - persona: <b>data entity de la persona y los repositorios para la persistencia de la persona</b>

    - viaje: <b>data entity del viaje</b>

    - convertidor: <b>para mapear de entidas a object</b>

### Luego sigue la implementación de los casos de uso en la capa domain useCase

### Ya despues sigue la implementación del entry point en infrastructure entry-point, api-rest

## Arrancar la aplicacion se debe remover el todo del h2 para poder usar una base de datos real

```CS
 properties.setProperty("hibernate.hbm2ddl.auto", "update"); // TODO: remove this for non auto create schema
```

nota
1. Al arrancar la aplicación pueden salir errores asi que eliminar las importaciones que no se usan y los metodos que causan el error en este caso en infrastructure, restConsumerConfig.java

  ELIMINAR
```CS
 // import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.reactive.client.DefaultWebClientExchangeTagsProvider;
import org.springframework.boot.actuate.metrics.web.reactive.client.MetricsWebClientFilterFunction;
import org.springframework.boot.actuate.metrics.web.reactive.client.WebClientExchangeTagsProvider;
```

ELIMINAR
```CS
@Bean
    public WebClient getWebClient(MetricsWebClientFilterFunction metricsFilter) {
        return WebClient.builder()
                .baseUrl(url)
                .filter(metricsFilter)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector())
                .build();
    }

    @Bean
    public MetricsWebClientFilterFunction webClientMetricsFilter(MeterRegistry registry) {
        WebClientExchangeTagsProvider tagsProvider = new DefaultWebClientExchangeTagsProvider();
        return new MetricsWebClientFilterFunction(registry, tagsProvider, "http-outgoing", AutoTimer.ENABLED);
    }
```

ELIMINAR LOS COMENTARIOS
```CS
      /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED
        SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();*/
                
      //.secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
```

VACIAR EL REST CONSUMER

```CS

   public class RestConsumer /* implements Gateway from domain */{  // De esta linea quitar el comentario "/* implements Gateway from domain */"


    private final WebClient client;


    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.

    public Mono<ObjectResponse> testGet() {

        return client
            .get()
            .retrieve()
            .bodyToMono(ObjectResponse.class);

    }

    public Mono<ObjectResponse> testPost() {

        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();

        return client
            .post()
            .body(Mono.just(request), ObjectRequest.class)
            .retrieve()
            .bodyToMono(ObjectResponse.class);
    }
```

## Configuraciones extras para conectarse a la base de datos con mysql

### primero en build.gradle carpeta raiz dentro de buildscript implementamos esta dependencia

```CS
buildscript {
	ext { // objecto ya exxistente
		cleanArchitectureVersion = '2.4.9'
		springBootVersion = '2.7.7'
		sonarVersion = '3.0'
		jacocoVersion = '0.8.8'
        lombokVersion = '1.18.24'
	}

    dependencies { // nueva linea a agregar
        classpath "mysql:mysql-connector-java:5.1.37"
    }
}
```

### Despuesde agregar esa linea se debe implementar la dependencia en build gradle en jpa-repository

```CS
implementation "mysql:mysql-connector-java:5.1.37"
```




















