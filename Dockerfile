# Imagem base oficial do Java 21 super leve
FROM eclipse-temurin:21-jdk-alpine

# Cria um diretório temporário
VOLUME /tmp

# Copia o arquivo .jar gerado pelo Maven para dentro da imagem
COPY target/*.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","/app.jar"]