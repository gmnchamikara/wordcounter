# Distributed Word Counting System (MQTT-based)

This project is a distributed word counting system using Node.js and MQTT, following a sidecar-based architecture. The system includes four main types of nodes:

- **Coordinator**
- **Proposer**
- **Acceptor**
- **Learner**

Each node runs as a separate process and communicates over MQTT.

## 🚀 How to Run the System

### 1. Install Dependencies

```bash
npm install
```

### 2. Running with Docker Compose

```bash
docker-compose up --build
```

### 3. Running Manually (without Docker)

#### Step 1: Start MQTT Broker Locally (optional if you have Docker)

Install and run Mosquitto:

# Install Mosquitto
sudo apt install mosquitto

# Run it (defaults to port 1883)
mosquitto

#### Step 2: Start Coordinator

```bash 
MQTT_BROKER_URL=mqtt://localhost npm run coordinator
```

#### Step 3: Start Proposer(s)

```bash 
MQTT_BROKER_URL=mqtt://localhost npm run proposer
```
Repeat this in new terminals for more proposers.

#### Step 4: Start Acceptor(s)
```bash 
MQTT_BROKER_URL=mqtt://localhost npm run acceptor
```

#### Step 5: Start Learner
```bash 
MQTT_BROKER_URL=mqtt://localhost npm run learner
```

