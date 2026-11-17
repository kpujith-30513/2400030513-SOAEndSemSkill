# TouristService — SOA Lab

**Tourist Records API with Token Validation**
Spring Boot · PostgreSQL · Swagger UI · Java 17

---

## Project Structure

```
TouristService/
 ├── pom.xml
 └── src/
      ├── main/
      │    ├── java/soa/
      │    │    ├── TouristServiceApplication.java   ← Entry point
      │    │    ├── entity/
      │    │    │    └── Tourist.java                ← JPA Entity
      │    │    ├── repository/
      │    │    │    └── TouristRepository.java      ← Spring Data JPA
      │    │    ├── service/
      │    │    │    └── TouristService.java          ← Business logic
      │    │    ├── controller/
      │    │    │    └── TouristController.java       ← REST endpoint + token check
      │    │    └── config/
      │    │         ├── SwaggerConfig.java           ← OpenAPI config
      │    │         └── DataInitializer.java         ← Sample data loader
      │    └── resources/
      │         └── application.properties            ← DB + token config
      └── test/
           └── java/soa/
                └── TouristServiceApplicationTests.java
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| JDK  | 17+ |
| Maven | 3.8+ |
| PostgreSQL | 14+ |
| pgAdmin | 4+ |
| Spring Tool Suite (STS) | 4.x |

---

## Step 1 — Set Up PostgreSQL with pgAdmin

1. Open **pgAdmin 4**.
2. Right-click **Databases** → **Create** → **Database**.
3. Set **Database name** = `tourism`, click **Save**.
4. The `tourists` table will be **created automatically** by Spring Boot JPA on first run.

> **Default credentials used in `application.properties`:**
> - Username: `postgres`
> - Password: `postgres`
> - Port: `5432`
>
> If yours are different, update `application.properties` before running.

---

## Step 2 — Import Project into Spring Tool Suite (STS)

1. Open **STS**.
2. Go to **File → Import → Maven → Existing Maven Projects**.
3. Click **Browse** → select the `TouristService` folder.
4. Ensure `pom.xml` is ticked → click **Finish**.
5. Wait for Maven to download all dependencies (first run may take a few minutes).
6. Right-click the project → **Maven → Update Project** (Alt+F5) → click **OK**.

---

## Step 3 — Configure the Database

Open `src/main/resources/application.properties` and verify:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tourism
spring.datasource.username=postgres
spring.datasource.password=postgres
app.valid.token=MY_VALID_TOKEN
```

Change the username/password if your PostgreSQL setup is different.

---

## Step 4 — Run the Application

### Option A — From STS
1. Right-click `TouristServiceApplication.java` → **Run As → Spring Boot App**.
2. Watch the console — you should see:
   ```
   ✅  Sample tourist records inserted successfully.
   Tomcat started on port(s): 8080
   ```

### Option B — From Terminal (Command Prompt / PowerShell)
```bash
cd TouristService
mvn spring-boot:run
```

### Option C — Build JAR and Run
```bash
mvn clean package -DskipTests
java -jar target/TouristService-0.0.1-SNAPSHOT.jar
```

---

## Step 5 — Test with Swagger UI

Open your browser and go to:

```
http://localhost:8080/swagger-ui/index.html
```

### Test 1 — Valid Token ✅

1. Click **GET /api/tourists** to expand it.
2. Click **Try it out**.
3. In the **Token** field, enter: `MY_VALID_TOKEN`
4. Click **Execute**.
5. **Expected Response:**
   - Status: `200 OK`
   - Body: JSON array of all 10 tourist records.

### Test 2 — Missing Token ❌

1. Click **GET /api/tourists** → **Try it out**.
2. Leave the **Token** field **empty**.
3. Click **Execute**.
4. **Expected Response:**
   - Status: `500 Internal Server Error`
   - Body: `Unauthorized: Invalid Token Provided`

### Test 3 — Invalid Token ❌

1. Click **GET /api/tourists** → **Try it out**.
2. Enter any wrong value in Token, e.g., `WRONG_TOKEN`.
3. Click **Execute**.
4. **Expected Response:**
   - Status: `500 Internal Server Error`
   - Body: `Unauthorized: Invalid Token Provided`

---

## Step 6 — Test with Postman

### Test 1 — Valid Token

| Setting | Value |
|---------|-------|
| Method | GET |
| URL | `http://localhost:8080/api/tourists` |
| Headers | `Token: MY_VALID_TOKEN` |

**Expected:** `200 OK` + JSON array of tourists.

---

### Test 2 — Missing Token

| Setting | Value |
|---------|-------|
| Method | GET |
| URL | `http://localhost:8080/api/tourists` |
| Headers | *(none)* |

**Expected:** `500 Internal Server Error` + `Unauthorized: Invalid Token Provided`

---

### Test 3 — Invalid Token

| Setting | Value |
|---------|-------|
| Method | GET |
| URL | `http://localhost:8080/api/tourists` |
| Headers | `Token: WRONG_TOKEN` |

**Expected:** `500 Internal Server Error` + `Unauthorized: Invalid Token Provided`

---

## Sample API Response (200 OK)

```json
[
  { "id": 1, "name": "Alice Johnson",    "country": "United States" },
  { "id": 2, "name": "Marco Rossi",      "country": "Italy"         },
  { "id": 3, "name": "Yuki Tanaka",      "country": "Japan"         },
  { "id": 4, "name": "Priya Sharma",     "country": "India"         },
  { "id": 5, "name": "Carlos Mendoza",   "country": "Mexico"        },
  { "id": 6, "name": "Emma Müller",      "country": "Germany"       },
  { "id": 7, "name": "Liam O'Brien",     "country": "Ireland"       },
  { "id": 8, "name": "Fatima Al-Rashid", "country": "UAE"           },
  { "id": 9, "name": "Chen Wei",         "country": "China"         },
  { "id": 10, "name": "Sofia Andersen",  "country": "Denmark"       }
]
```

---

## Architecture

```
Client (Swagger / Postman / Browser)
        │
        │  GET /api/tourists
        │  Header: Token: MY_VALID_TOKEN
        ▼
┌──────────────────────┐
│   TouristController  │  ← Receives request + reads Token header
│  (REST Layer)        │
│                      │
│  Token Validation    │  ← Compares with app.valid.token from properties
│  if invalid → 500    │
│  if valid   → ✓      │
└────────┬─────────────┘
         │
         ▼
┌──────────────────────┐
│   TouristService     │  ← Business logic layer
│  (Service Layer)     │
└────────┬─────────────┘
         │
         ▼
┌──────────────────────┐
│  TouristRepository   │  ← Spring Data JPA, calls findAll()
│  (Repository Layer)  │
└────────┬─────────────┘
         │
         ▼
┌──────────────────────┐
│     PostgreSQL       │  ← tourists table
│  (tourism database)  │
└──────────────────────┘
```

---

## Troubleshooting

| Problem | Solution |
|---------|----------|
| `Connection refused` on startup | Make sure PostgreSQL service is running |
| `password authentication failed` | Update `spring.datasource.password` in `application.properties` |
| `database "tourism" does not exist` | Create the `tourism` database in pgAdmin first |
| Port 8080 already in use | Change `server.port=8081` in `application.properties` |
| Swagger page not found | Go to `http://localhost:8080/swagger-ui/index.html` exactly |
| Maven dependency errors in STS | Right-click project → Maven → Update Project |

---

## Key Files Summary

| File | Purpose |
|------|---------|
| `Tourist.java` | JPA Entity mapped to `tourists` table |
| `TouristRepository.java` | Extends `JpaRepository`, provides `findAll()` |
| `TouristService.java` | Service layer, calls repository |
| `TouristController.java` | REST endpoint, token validation |
| `SwaggerConfig.java` | OpenAPI/Swagger UI configuration |
| `DataInitializer.java` | Inserts 10 sample records on first startup |
| `application.properties` | DB credentials, token value, JPA settings |
