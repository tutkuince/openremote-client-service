# OpenRemote Client Service – Setup & Technical Approach

## 1. Project Description
This project is a Spring Boot microservice that connects to the OpenRemote platform and allows you to perform CRUD operations on IoT assets via REST API.  
The service is designed to be easily extensible and maintainable, using modern Java technologies and best practices.

---

## 2. Installation & Setup Notes
### **Requirements**
- Ubuntu/Debian based Linux server (provided by your team)
- Temurin 17.0.11+9 (Java 17)
- Git
- Docker & Docker Compose

---

### **Server Setup Steps**
#### 1. **Connect to the server**
#### 2. **Install Java (Temurin 17.0.11+9)**
```
apt update
apt install wget -y
wget https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.11+9/OpenJDK17U-jdk_x64_linux_hotspot_17.0.11_9.tar.gz
tar -xvf OpenJDK17U-jdk_x64_linux_hotspot_17.0.11_9.tar.gz
mv jdk-17.0.11+9 /opt/java
export PATH=$PATH:/opt/java/bin
java -version
```
#### 3. **Install Git**
```
apt install git -y
```
#### 4. **Clone OpenRemote**
```
git clone https://github.com/openremote/openremote.git
cd openremote
```
#### 5. **Install Docker & Docker Compose**
```
apt install docker.io docker-compose -y
systemctl start docker
systemctl enable docker
docker --version
docker-compose --version
```
#### 6. **Run OpenRemote**
```
OR_HOSTNAME=138.199.200.45 OR_SSL_PORT=443 docker compose -p openremote up -d
```

---

## 3. Usage
The client service will be available at http://localhost:8080.
You can use Postman collections (provided in the repo) to test CRUD operations on IoT assets.

#### 1. **.env Configuration**
- Place the .env file in the root directory of your client service project.
- Example .env:
  - OPENREMOTE_AUTH_URL=https://138...
  - OPENREMOTE_API_BASEURL=https://138...
  - OPENREMOTE_CLIENT_ID=iot...
  - OPENREMOTE_CLIENT_SECRET=somesecret
 
--- 

## 4. Technical Approach & Notes
### Why FeignClient over RestTemplate?
- Declarative: FeignClient allows you to define HTTP clients using Java interfaces, making the codebase cleaner and less boilerplate compared to RestTemplate.
- Integration: Works out-of-the-box with Spring Cloud and Resilience4j for CircuitBreaker, Retry, and RateLimiter.
- Error Handling: Easier to integrate with global exception handlers and fallback strategies.
- Testability: Interfaces are easier to mock/test.

### Other Key Points
- Resilience4j: Used for circuit breaker, retry, and rate limiting to ensure robustness and graceful degradation under failure conditions.
- Global Exception Handling: All exceptions (including rate limiting/circuit breaker triggers) are centrally handled via a @ControllerAdvice, providing clean API responses and better logging.
- Unit Testing: All custom service and utility classes are covered with unit tests. Exception scenarios and fallback logic are also tested.
- Swagger Integration: The project includes Swagger (OpenAPI) documentation. Once the service is running, you can access interactive API docs at: http://localhost:8080/swagger-ui.html

--- 

## 5. Postman Collections
- Postman collections for API testing are included in the /docs directory of the repository.

---

## 6. Build Java Docker Image
- Go to the root directory of your client service and build the image with:
  - mvn spring-boot:build-image "-Dspring-boot.build-image.imageName=tutkuince/openremote-client-service"
- Start the service with:
  - docker run --env-file .env -p 8080:8080 tutkuince/openremote-client-service
- You can find the built image locally, or pull it from Docker Hub:
  - docker pull tutkuince/openremote-client-service
- The image is available on Docker Hub:
  - https://hub.docker.com/r/tutkuince/openremote-client-service

## 7. Test Coverage
- The test coverage results are as follows (based on the JaCoCo report):
![image](https://github.com/user-attachments/assets/1486822c-4261-423c-95ec-184b5a3b18b3)
