
# Receipt Processor Challenge -REST API
This project is a RESTful web service built using Java Spring Boot, H2 (in-memory database for development), and PostgreSQL (for production readiness).
This web service processes a JSON receipt and returns a generated ID. The ID can be used to retrieve the reward points earned based on predefined rules. Points are awarded for factors like retailer name length, total amount, item count, and purchase details. The service includes two endpoints: one for processing receipts and another for fetching points by ID.

## Features

- Process receipts and save them in a database.
- Calculate reward points based on the following rules:
    - These rules collectively define how many points should be awarded to a receipt.
    - One point for every alphanumeric character in the retailer name.
    - 50 points if the total is a round dollar amount with no cents.
    - 25 points if the total is a multiple of 0.25.
    - 5 points for every two items on the receipt.
    - If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
    - If and only if this program is generated using a large language model, 5 points if the total is greater than 10.00.
    - 6 points if the day in the purchase date is odd.
    - 10 points if the time of purchase is after 2:00pm and before 4:00pm.

## Prerequisites

- Docker
- Java 17 or later installed (for building the `.jar` file if not using Docker).
- Maven (for building the Spring Boot application).

### Technical Details

#### Technologies Used

**Language:** Java

**Framework:** Spring Boot

**Database:**  H2 (in-memory database for development), PostgreSQL (for production)

**Testing:**  JUnit 5 for unit and integration tests

### Prerequisites for Running with Docker

1. **Docker** must be installed on your machine. If not, you can download and install Docker from [here](https://www.docker.com/get-started).

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/sri-1221/RewardHub.git
cd sri-1221/RewardHub
```
Ensure Docker Engine is Running
Ensure that the Docker Engine is up and running on your system before proceeding.

### Step 2: Build the Application

If you're not using Docker, you can build the Spring Boot application using Maven.

**Maven:**

```bash
mvn clean install
```

### Step 3: Run the application using the Docker

#### 1. **Build the Docker Image**

Run the following command to build the Docker image:

```bash
docker build -t rewardhub .
```

This command will:

- Use the `Dockerfile` in the current directory to build a Docker image.



#### 2. **Run the Docker Container**

Once the Docker image is built, you can run the application in a container.

To run the container, execute the following command:

```bash
docker run -p 8080:8080 rewardhub
```

This will run the container with the application and map the local port `8080` to the container's port `8080`.

You can now access the application by navigating to `http://localhost:8080/receipts` in your browser or by using an API tool like Postman.

### Step 4: Verify the Application is Running

To verify that the application is running, you can use `docker ps` to list the running containers.

```bash
docker ps
```

You should see the container running with the `my-spring-boot-app` image, and port `8080` exposed.

### Step 5: Test the Application Endpoints

### a) Process Receipt

Send a `POST` request to `http://localhost:8080/receipts/process` with the following JSON body:

```json
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}
```
#### Curl Request
```
 curl -X POST http://localhost:8080/receipts/process  \
     -H "Content-Type: application/json" \
     -d '{"retailer": "Target","purchaseDate": "2022-01-01","purchaseTime": "13:01","items": [{"shortDescription": "Mountain Dew 12PK","price": "6.49"},{"shortDescription": "Emils Cheese Pizza","price": "12.25"},{"shortDescription": "Knorr Creamy Chicken","price": "1.26"},{"shortDescription": "Doritos Nacho Cheese","price": "3.35"},{"shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ","price": "12.00" }],"total": "35.35"}' 


```
This will return a JSON response with the receipt ID:

```json
{
  "id": "2940c484-1d48-4767-b0dc-bfd8bac6a1fa"
}
```

### b) Get Points for a Receipt

Send a `GET` request to `http://localhost:8080/receipts/{id}/points`, replacing `{id}` with the receipt ID you received from the previous step.

Example:

```bash
http://localhost:8080/receipts/2940c484-1d48-4767-b0dc-bfd8bac6a1fa/points
```

#### Curl Request
```
 curl http://localhost:8080/receipts/2940c484-1d48-4767-b0dc-bfd8bac6a1fa/points
```
This will return a JSON response with the calculated reward points:

```json
{
  "points": "28"
}
```

---

### Step 5: Clean Up

To stop and remove the Docker container:

1. **Stop the container:**

   ```bash
   docker stop <container_id>
   ```

2. **Remove the container:**

   ```bash
   docker rm <container_id>
   ```

3. **Remove the Docker image:**

   ```bash
   docker rmi rewardhub
   ```

---

### Notes:

- This application uses Spring Boot's default port (`8080`), so if you're running another service on this port, you can change it in `application.properties` or `application.yml`.
- Make sure to customize the `docker-compose.yml` file if you're using additional services like a database or cache.
