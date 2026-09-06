# syntax=docker/dockerfile:1

# ============================================================
#  Etapa 1: build con Maven + JDK 17 (Eclipse Temurin)
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero el pom para cachear la descarga de dependencias:
# si el pom no cambia, esta capa se reutiliza entre builds.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Ahora el codigo fuente y el empaquetado (sin tests, ya se corren aparte).
COPY src ./src
RUN mvn -B clean package -DskipTests

# ============================================================
#  Etapa 2: imagen de ejecucion, solo el JRE 17
# ============================================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Ejecutamos como usuario sin privilegios.
RUN useradd --system --uid 1001 spring
USER spring

# Traemos unicamente el jar generado en la etapa de build.
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# La aplicacion escucha en 8080 por defecto. En Render, si se define la
# variable PORT, se respeta ese valor.
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS:-} -Dserver.port=${PORT} -jar app.jar"]
