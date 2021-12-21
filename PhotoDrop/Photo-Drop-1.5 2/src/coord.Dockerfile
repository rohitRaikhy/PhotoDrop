FROM bellsoft/liberica-openjdk-alpine-musl:16 AS client-build
COPY . ~/Documents/Project_Final_Photo_Drop
WORKDIR ~/Documents/Project_Final_Photo_Drop
RUN javac -cp PAXOS/mongo-java-driver-3.11.2.jar: PAXOS/*.java

ENTRYPOINT ["java", "-cp"," PAXOS.mongo-java-driver-3.11.2.jar: ", "PAXOS.StartCoordindator"]


