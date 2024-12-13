# Используем официальный образ JDK для сборки приложения
FROM openjdk:17-jdk-slim AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта в контейнер
COPY . /app

# Собираем приложение с помощью Maven
RUN ./mvnw clean package -DskipTests

# Используем образ с Java для запуска приложения
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR файл из первого этапа
COPY --from=build /app/target/microservice-0.0.1-SNAPSHOT.jar /app/microservice.jar

# Открываем порт, на котором будет работать приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "microservice.jar"]
