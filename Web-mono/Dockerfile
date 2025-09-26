# Usa una imagen base de Java
FROM eclipse-temurin:17-jre-alpine

# Establece el directorio de trabajo dentro del contenedor
ARG JAR_FILE=target/toolrent-backend.jar

# Copia el archivo JAR de la aplicación al contenedor
COPY ${JAR_FILE} toolrent-backend.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/toolrent-backend.jar"]