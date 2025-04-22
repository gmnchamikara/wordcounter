# Distributed Word Counting System (MQTT-based)

This project is a distributed word counting system using Node.js and MQTT, following a sidecar-based architecture. The system includes four main types of nodes:

- **Coordinator**
- **Proposer**
- **Acceptor**
- **Learner**

Each node runs as a separate process and communicates over MQTT.

## 🚀 How to Run the System

### 1. Start the MQTT Broker

Ensure Docker is installed and running. Then start the MQTT broker (Mosquitto):

```bash
docker-compose up -d mosquitto
```

### 2. Start System Components

Coordinator	

```bash 
npm run start:coordinator
```
Proposer

```bash 
npm run start:proposer
```
Acceptor

```bash 
npm run start:acceptor
```
Learner	

```bash 
npm run start:learner
```
