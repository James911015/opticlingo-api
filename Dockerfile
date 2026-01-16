# ETAPA 1: Construcción (Build)
# Usamos una imagen de Gradle con Java 21 para compilar el proyecto
FROM gradle:jdk21-alpine AS build
WORKDIR /app

# Copiamos los archivos de configuración primero para aprovechar la caché
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Descargamos las dependencias (esto se cachea si no cambian los archivos de configuración)
RUN gradle dependencies --no-daemon || true

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos el proyecto (saltando los tests para evitar errores de conexión a DB durante el build)
RUN gradle build --no-daemon -x test

# ETAPA 2: Ejecución (Run)
# Usamos una imagen ligera de Java 21 para correr la app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos solo el JAR generado en la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]