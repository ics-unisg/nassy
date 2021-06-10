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

Navigate to (http://localhost:1111/nifi)[http://localhost:1111/nifi] and add the infrastructure from the nifi template. 

![image](https://user-images.githubusercontent.com/2293142/121515080-0bf57580-c9ed-11eb-8fe3-98d1244a79cf.png)

Be sure to update the `websocket.controller.service.id` with the corresponding jetty websocket server.

![image](https://user-images.githubusercontent.com/2293142/121515379-6b538580-c9ed-11eb-9a45-39d7a2ea2c88.png)
![image](https://user-images.githubusercontent.com/2293142/121515455-7dcdbf00-c9ed-11eb-91cc-7d206481ad06.png)
![image](https://user-images.githubusercontent.com/2293142/121515154-24fe2680-c9ed-11eb-8e0f-5b4fe87a3e4c.png)



## Start ET-Indicator
```
cd nifi/et-indicator/
npm install
npm run dev
```

Navigate to (http://localhost:8080)[http://localhost:8080]


# iTrace / iMotions
Make sure to follow the steps here: https://github.com/ics-unisg/dcap/tree/main/imotions

# Usage

## Recorded data
You can "replay" recorded data in realistic speed. ET-Simulator can do that.
```
cd nifi/et-simulator
pipenv install
pipenv run python read_file.py
```
This will play all data in sample.csv.


## Live data
Start the reader. Do not forget to point iMotions to your IP. You have to be in the same network.
```
cd nifi/et-reader/
pipenv install
pipenv run python3 reader.py
```


# Ideas

Matlab in nifi can be done with matlab in the nifi docker container https://github.com/mathworks-ref-arch/matlab-dockerfile
