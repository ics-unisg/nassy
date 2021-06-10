# Installation of the tool

## Requirement

- docker-compose
- pipenv
- python 3.9
- npm
- vscode


## Installation on Ubuntu
```
sudo apt install curl python3.9 pip pipenv -y
curl https://get.docker.com | sh && sudo systemctl --now enable docker
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

## Installation on Mac
Make sure to increase the resource limit of docker to at least 4GB Ram.

## Run the infrastructure

```
docker-compose up --build
```

DCAP is now running and can be used. In `nifi/processors/dcapclient/` is a filter notebook that shows how filtering works.

## Setup NIFI

Navigate to (http://localhost:1111/nifi)[http://localhost:1111/nifi] and add the infrastructure from the nifi template. Be sure to update the websocket.controller.service.id with the corresponding jetty websocket server.


## Start ET-Indicator
```
cd nifi/et-indicator/
npm install
npm run dev
```

Navigate to (http://localhost:8080)[http://localhost:8080]


Navigate to (http://localhost:8080)[http://localhost:8080]


## Configure IMotions
API

# iTrace / iMotions
Make sure to follow the steps here: https://github.com/ics-unisg/dcap/tree/main/imotions

Start the reader:
```
cd nifi/et-reader/
pipenv install
pipenv run python3 reader.py
```


# Ideas

Matlab in nifi can be done with matlab in the nifi docker container https://github.com/mathworks-ref-arch/matlab-dockerfile




# TODO
- Screenshots
- Comment Header in each file (Why it is needed and what it does / design & architecture)
- Manually pipe data
- Tag working state
- licence free
- fix lhipa
