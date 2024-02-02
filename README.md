Rest Api for an E-Commerce Application

 This REST API was developed as a part of the final project during the project-weeks at StatStep Education School. 
 Serving as the backend for an E-Commerce application, it was crated to meet the demands of modern online retail.
 
Key Features

 User Management: Seamlessly handle user authentication, registration, and profile management.
 Product Operations: Manage product listings, including additions, updates, and deletions.
 Shopping Cart: Implement a shopping cart system for a shopping experience(adding and deleteng items from cart).
 Order Processing: Allow to make an order, see order history, and real-time order status tracking.
 Exception Handling: Implemented error handling and informative exception messages.
 
 Technologies Used
 Spring Boot: Spring framework for creating scalable and efficient Java applications.
 Spring Data JPA: to simplify data access and manipulation.
 Hibernate: for object-relational mapping and simplified database interaction.
 MySQL Database: as a database management system for data storage and retrieval.
 Lombok: Increase code readability and conciseness through the use of Lombok annotations.
 Logging: Implement logging in SLF4J for efficient debugging, bug tracking, and system monitoring.

Diagram for the application
![Image 02.02.24 at 15.39.jpeg](..%2FImage%2002.02.24%20at%2015.39.jpeg)

Getting Started
Clone the Repository:
git clone https://github.com/MariiaLobanova/E-Commerce-Shop-RESTful-API
cd your-repository
Build and Run:

API Documentation:
Access the Swagger API documentation at http://localhost:8090/swagger-ui/index.html#/ 
for comprehensive API details and testing.

Modules: 
- User;
- Product;
- Cart;
- Order.

Features:

- User authentication and validation with session token having validity of 24 hours for security purpose,
- Admin Role with valid session token can add/delete/uddate products and see whole list of users;
- All users can see products and logged in User as a Role with valid session token can add products to a cart, 
see list of products in the cart, placing orders, see history and status of his orders.
