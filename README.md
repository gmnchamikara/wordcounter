### To Run the System:

## Start MQTT broker:

docker-compose up -d mosquitto

## Install dependencies:

npm install

## Start nodes in separate terminals:

npm run start:coordinator
npm run start:proposer
npm run start:acceptor
npm run start:learner