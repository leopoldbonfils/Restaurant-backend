# 🍽️ Digital Dine-In & Order Management System — Backend

A Spring Boot REST API backend for a self-service restaurant ordering system. Customers scan a QR code, browse the menu, place orders, and track them in real-time. Kitchen staff manage orders through a live dashboard. Admins control the menu and view analytics.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Real-time | WebSocket (STOMP over SockJS) |
| Validation | Jakarta Bean Validation |
| Build tool | Maven |
| Utilities | Lombok |

---

## 📁 Project Structure

```
src/main/java/com/restaurant/
│
├── RestaurantApplication.java        ← Entry point
│
├── model/                            ← JPA Entities (auto-creates DB tables)
│   ├── Customer.java
│   ├── Order.java
│   ├── MenuItem.java
│   ├── OrderItem.java
│   ├── OrderStatus.java              (enum)
│   └── DietaryTag.java               (enum)
│
├── repository/                       ← Spring Data JPA interfaces
│   ├── CustomerRepository.java
│   ├── OrderRepository.java
│   └── MenuItemRepository.java
│
├── service/                          ← Business logic
│   ├── CustomerService.java
│   ├── MenuItemService.java
│   ├── OrderService.java
│   └── impl/
│       ├── CustomerServiceImpl.java
│       ├── MenuItemServiceImpl.java
│       └── OrderServiceImpl.java
│
├── controller/                       ← REST Controllers
│   ├── CustomerController.java
│   ├── MenuItemController.java
│   └── OrderController.java
│
├── dto/
│   ├── request/                      ← Incoming request bodies
│   │   ├── CustomerCheckInRequest.java
│   │   ├── MenuItemRequest.java
│   │   ├── PlaceOrderRequest.java
│   │   └── UpdateOrderStatusRequest.java
│   └── response/                     ← Outgoing response shapes
│       ├── ApiResponse.java
│       ├── CustomerResponse.java
│       ├── MenuItemResponse.java
│       ├── OrderItemResponse.java
│       ├── OrderResponse.java
│       └── AnalyticsSummaryResponse.java
│
├── config/
│   ├── WebSocketConfig.java          ← STOMP / SockJS real-time setup
│   ├── CorsConfig.java               ← CORS for React frontend
│   └── DataSeeder.java               ← Seeds sample Rwandan menu on first boot
│
├── exception/
│   ├── BadRequestException.java
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
│
└── websocket/
    ├── OrderNotificationEvent.java
    └── OrderNotificationService.java ← Broadcasts status changes via STOMP
```

---

## 🗄️ Database Tables (Auto-Created)

Hibernate creates all tables automatically on first startup via `ddl-auto=update`. No SQL scripts needed.

| Table | Description |
|---|---|
| `customers` | Customer sessions per table visit |
| `orders` | Orders placed by customers |
| `order_items` | Individual line items within an order |
| `menu_items` | All food and drink items |
| `menu_item_dietary_tags` | Dietary tags per menu item (join table) |
| `menu_item_allergens` | Allergen labels per menu item (join table) |

---

## ⚙️ Setup & Configuration

### 1. Prerequisites

- Java 17+
- PostgreSQL 14+
- Maven 3.8+

### 2. Create the Database

```sql
CREATE DATABASE restaurant_db;
```

### 3. Configure `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db
spring.datasource.username=postgres
spring.datasource.password=your_password_here
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The server starts on **http://localhost:8080**

Tables are created automatically and the menu is seeded with sample Rwandan dishes on the first run.

---

## 🌐 REST API Reference

All responses follow this envelope format:

```json
{
  "success": true,
  "message": "...",
  "data": { }
}
```

---

### 👤 Customer Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/customers/check-in` | Check customer into a table |
| `PATCH` | `/api/customers/{id}/check-out` | Check customer out |
| `GET` | `/api/customers/{id}` | Get customer by ID |
| `GET` | `/api/customers/table/{tableNumber}` | Get active session by table |
| `GET` | `/api/customers` | List all customers |

**Check-in request body:**
```json
{
  "tableNumber": "Table 5",
  "name": "Alice",
  "phone": "+250788000000",
  "preferredLanguage": "en"
}
```

---

### 🍽️ Menu Item Endpoints

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/menu-items` | Available items (customer view) | Public |
| `GET` | `/api/menu-items/all` | All items including unavailable | Admin |
| `GET` | `/api/menu-items/{id}` | Single item by ID | Public |
| `GET` | `/api/menu-items/category/{category}` | Items by category | Public |
| `GET` | `/api/menu-items/dietary/{tag}` | Items by dietary tag | Public |
| `GET` | `/api/menu-items/categories` | All category names | Public |
| `POST` | `/api/menu-items` | Create a new item | Admin |
| `PUT` | `/api/menu-items/{id}` | Update an item | Admin |
| `DELETE` | `/api/menu-items/{id}` | Delete an item | Admin |
| `PATCH` | `/api/menu-items/{id}/toggle-availability` | Toggle on/off | Admin |

**Dietary tag values:** `VEGETARIAN` `VEGAN` `HALAL` `GLUTEN_FREE`

**Create/update request body:**
```json
{
  "name": "Brochette",
  "description": "Grilled meat skewers",
  "price": 3000,
  "category": "Main Course",
  "imageEmoji": "🍢",
  "prepTimeMinutes": 15,
  "isSpicy": false,
  "isAvailable": true,
  "dietaryTags": ["HALAL"],
  "allergens": ["Meat"]
}
```

---

### 📦 Order Endpoints

| Method | Endpoint | Description | Used by |
|---|---|---|---|
| `POST` | `/api/orders` | Place a new order | Customer |
| `GET` | `/api/orders` | All orders | Admin |
| `GET` | `/api/orders/active` | Active orders (kitchen board) | Kitchen |
| `GET` | `/api/orders/{id}` | Order by ID | All |
| `GET` | `/api/orders/status/{status}` | Filter by status | Kitchen |
| `GET` | `/api/orders/table/{tableNumber}` | Orders by table | Customer |
| `GET` | `/api/orders/customer/{customerId}` | Customer order history | Customer |
| `PATCH` | `/api/orders/{id}/status` | Advance order status | Kitchen |
| `PATCH` | `/api/orders/{id}/cancel` | Cancel an order | Kitchen/Admin |
| `GET` | `/api/orders/analytics` | Revenue & sales summary | Admin |

**Place order request body:**
```json
{
  "customerId": 1,
  "tableNumber": "Table 5",
  "specialRequests": "No onions please",
  "items": [
    { "menuItemId": 1, "quantity": 2, "itemNotes": "Extra spicy" },
    { "menuItemId": 3, "quantity": 1, "itemNotes": "" }
  ]
}
```

**Update status request body:**
```json
{ "status": "PREPARING" }
```

---

## 🔄 Order Status Lifecycle

```
PENDING → PREPARING → READY → DELIVERED → PAID
                                        ↘ CANCELLED
```

| Status | Set by | Meaning |
|---|---|---|
| `PENDING` | System | Order received, kitchen not started yet |
| `PREPARING` | Kitchen staff | Actively cooking |
| `READY` | Kitchen staff | Food ready, waiter to deliver |
| `DELIVERED` | Waiter | Delivered to table |
| `PAID` | Waiter | Payment confirmed, order complete |
| `CANCELLED` | Kitchen/Admin | Cancelled before completion |

Invalid transitions are rejected with `400 Bad Request`.

---

## 📡 Real-Time WebSocket

The backend pushes live order updates to connected clients using STOMP over SockJS.

**Connect:**
```
ws://localhost:8080/ws
```

**Subscribe to channels:**

| Topic | Who subscribes | What it receives |
|---|---|---|
| `/topic/orders` | Kitchen dashboard | All order status changes |
| `/topic/orders/{tableNumber}` | Customer tracking screen | Their own order updates |

**Event payload:**
```json
{
  "orderId": 42,
  "tableNumber": "Table 5",
  "previousStatus": "PREPARING",
  "newStatus": "READY",
  "message": "🎉 Order #42 is ready for delivery!",
  "timestamp": "2024-11-15T14:30:00"
}
```

**Frontend JavaScript example (SockJS + STOMP):**
```javascript
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const client = new Client({
  webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
  onConnect: () => {
    // Kitchen: listen for all orders
    client.subscribe('/topic/orders', (msg) => {
      console.log(JSON.parse(msg.body));
    });

    // Customer: listen for their table only
    client.subscribe('/topic/orders/Table 5', (msg) => {
      console.log(JSON.parse(msg.body));
    });
  }
});

client.activate();
```

---

## 🌱 Automatic Data Seeding

On the first startup, `DataSeeder` populates the menu with **12 Rwandan dishes and drinks** including Brochette, Ugali, Isombe, Sambaza, Mizuzu, Ikawa coffee, Passion Fruit Juice, and more — each with correct dietary tags, allergens, and prep times.

Seeding is skipped automatically if the menu table already has data.

---

## 💡 Loyalty Points

Customers earn **1 point per 100 RWF** spent automatically when an order is placed. Points are stored on the `Customer` record and returned in every customer response.

---

## 🔧 CORS

The backend accepts requests from any origin in development. Before deploying to production, update `CorsConfig.java`:

```java
config.setAllowedOriginPatterns(List.of("https://yourdomain.com"));
```

---

## 👥 Team

**Dream Weavers** — Digital Dine-In & Order Management System

---

## 📄 License

MIT License
