FROM maven:3.9.6-ibmjava-8

# Copy the current directory contents into the container at /app
ADD . /app

# Set the working directory
WORKDIR /app/build

# When running the container, 
RUN mvn clean install

# execute the jar file
CMD ["java", "-jar", "/app/startApp/target/startApp-1.0-SNAPSHOT-jar-with-dependencies.jar"]

