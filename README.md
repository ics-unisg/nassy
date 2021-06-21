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


Press anywhere outside the NIFI stages

![image](https://user-images.githubusercontent.com/2293142/121515379-6b538580-c9ed-11eb-9a45-39d7a2ea2c88.png)

Press the Configuration icon of NIFI Flow

Go to Controller Services Panel 

<img width="1238" alt="Pasted Graphic 1" src="https://user-images.githubusercontent.com/23260661/122577787-780e5400-d053-11eb-8226-8e1b408d3350.png">

Press the Configration icon of JettyWebSocketServer

Copy the Id

<img width="767" alt="Controller Service Details" src="https://user-images.githubusercontent.com/23260661/122578544-40ec7280-d054-11eb-9fd9-81ca6caee13b.png">

Go to UpdateAttribute phase

<img width="285" alt="image" src="https://user-images.githubusercontent.com/23260661/122577949-b0159700-d053-11eb-95ba-9c5fd5cdb6a7.png">

Press the Configuration icon

Go to the Properties Panel

<img width="765" alt="Configure Processor" src="https://user-images.githubusercontent.com/23260661/122578176-eb17ca80-d053-11eb-8b9e-b72e903f671a.png">

Paste the id in the field websocker.controller.service.id 

Close the Panel 

Press anywhere outside the NIFI stages

Press the Configuration icon of NIFI Flow

Go to Controller Services Panel 

Enable all services by pressing the enable icons of the different services


Run NIFI


## Start ET-Indicator
```
cd nifi/et-indicator/
npm install
npm run serve
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
