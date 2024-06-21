# CellphoneS: CellphoneS Clone Website

CellphoneS Clone Website is a pet project aimed to practice building a typical Spring Boot application in Java


# Technologies
- Java 21
- Spring boot 3.2.5
- PostgreSQL
- Firebase Storage
- VnPay

## Overview

CellphoneS is an e-commerce platform that allows public users, customers, and admin users to interact with various functionalities such as viewing products, managing orders, and handling customer data. The system is divided into modules for public users, registered customers, and administrators

## Actors

- **Public User**: Anyone who visits the site without logging in.
- **Customer**: A registered user who can purchase products.
- **Admin**: A user with administrative privileges to manage products, orders, customers, and other administrative tasks.

  ## Use Cases Summary

| Use Case              | Description                                                             | Actor        | Preconditions                   | Postconditions                     |
|-----------------------|-------------------------------------------------------------------------|--------------|----------------------------------|------------------------------------|
| **View Products**     | View product details, featured products, and products by category       | Public User  | None                             | None                               |
| **Search Products**   | Search for products based on various criteria                           | Public User  | None                             | Display search results             |
| **Register**          | Create a new user account                                               | Public User  | None                             | User account created               |
| **Add to Cart**       | Add products to shopping cart                                           | Customer     | User must be logged in           | Product added to cart              |
| **Order Product**     | Place an order for products in the cart                                 | Customer     | Products in cart, user logged in | Order placed                       |
| **View My Orders**    | View past and current orders                                            | Customer     | User must be logged in           | Display order list                 |
| **Search by Image with AI** | Upload an image to search for similar products using AI           | Customer     | User must be logged in           | Display search results             |
| **Rate Product**      | Rate products (one rating per product)                                  | Customer     | User must be logged in, not rated before | Rating submitted           |
| **Manage Orders**     | View, paginate, and change order status                                 | Admin        | Admin must be logged in          | Order statuses updated             |
| **Manage Products**   | Create, read, update, delete, and filter products                       | Admin        | Admin must be logged in          | Product list updated               |
| **Manage Categories** | Create, update, and delete product categories                           | Admin        | Admin must be logged in          | Category list updated              |
| **Manage Customers**  | View, activate, deactivate, and paginate customer records               | Admin        | Admin must be logged in          | Customer list updated              |
| **Authentication**    | Handle user login, logout, and sessio

# References
1. [JPA & JWT (Hoang Nguyen)] (https://github.com/hoangnd-dev/rookies-java)
2. [Springboot Demo (Phu Le)] (https://github.com/phulecse2420/demo)
3. NashTech Slide
