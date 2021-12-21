#!/bin/bash

docker network create photo_drop_network

docker build -t server .
docker build -t client -f client.Dockerfile .
docker build -t coord -f coord.Dockerfile .

docker run -d -p 2000:2000 -ti --rm coord  2000
docker run -d -p 6001:6001 -ti --rm --name s1 server  6001 s1 host.docker.internal host.docker.internal Coordinator db1 2000
docker run -d -p 6002:6002 -ti --rm --name s2 server  6002 s2 host.docker.internal host.docker.internal Coordinator db2 2000
docker run -d -p 6003:6003 -ti --rm --name s3 server  6003 s3 host.docker.internal host.docker.internal Coordinator db3 2000
docker run -d -p 6004:6004 -ti --rm --name s4 server  6004 s4 host.docker.internal host.docker.internal Coordinator db4 2000
docker run -d -p 6005:6005 -ti --rm --name s5 server  6005 s5 host.docker.internal host.docker.internal Coordinator db5 2000


