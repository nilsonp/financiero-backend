# financiero-backend
Proyecto prueba tecnica - Flypass

## Requisitos para ejecucion del proyecto
- Docker - para ejecutar base de datos postgresql como contenedor, tambien para el caso de ejecutar todo el proyecto en contenedor
- JDK Java 21 

### Iniciar docker para ejecutar base de datos
Iniciar instancia docker, solo el servicio de postgresql:

```shell
docker compose -f docker-compose.yaml --profile db up -d 
```
