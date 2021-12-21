FROM bellsoft/liberica-openjdk-alpine-musl:16 AS client-build
COPY . ~/Documents/Project_Final_Photo_Drop
WORKDIR ~/Documents/Project_Final_Photo_Drop
RUN javac -cp PAXOS/mongo-java-driver-3.11.2.jar: PAXOS/*.java

# cmd to run server locally - java server.ServerApp 1111 5555
ENTRYPOINT ["java", "-cp"," PAXOS.mongo-java-driver-3.11.2.jar: ", "PAXOS.StartServer"]

