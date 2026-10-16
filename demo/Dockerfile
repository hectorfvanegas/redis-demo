# ==========================================
# ETAPA 1: Construcción (Build)
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /build

# Copiar archivos de configuración de Gradle primero (aprovecha la caché de capas de Docker)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Descargar dependencias (se cachea si no hay cambios en build.gradle)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente y compilar saltando los tests para mayor velocidad
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# ==========================================
# ETAPA 2: Producción (Run)
# ==========================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear un usuario no root por seguridad (Best Practice)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar SOLO el .jar generado en la etapa 1
COPY --from=builder /build/build/libs/*SNAPSHOT.jar app.jar

# Exponer el puerto configurado
EXPOSE 8081

# Comando de inicio optimizado
ENTRYPOINT ["java", "-jar", "/app/app.jar"]