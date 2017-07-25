## Description

Checkout Component 3.0 requirements:

1. Design and implement market checkout component with readable API that calculates the total price of a number of items.
2. Checkout mechanism can scan items and return actual price (is stateful)
3. Our goods are priced individually.
4. Some items are multipriced: buy N of them, and they'll cost you Y cents

    | Item   | Price | Unit | Special Price |
    | ------ |-----| -------|---------------|
    | A     | 40     | 3       |  70            |
    | B   | 10       | 2       |  15            |
    | C   | 30       | 4       |  60            |
    | D  | 25        | 2       |  40            |
    
5. Client receives receipt containing list of all products with corresponding prices after payment
6. Some items are cheaper when bought together - buy item X with item Y and save Z cents

    | Item     | Price   |
    | ---------|---------|
    | A,B      | 10      |
    | A,B,C,D  | 30      |
    
7. Additional Discount of 10% should be added if total order price (including other discounts/special prices) exceeds 600
8. Market can add any type of item or discount at any point in time.
9. Market stores data about quantity of all items sold in all its checkouts and all applied discounts
10. Perform a simple simulation of a market with 3 Checkouts and a single queue of 10 random baskets of items.

## Environment
I implemented Checkout component using Spring boot and H2 database.
Database schema is created on startup and initialized with data declared in file:
```
data.sql
```
You can log into database console using link:
```
localhost:9000/h2Console
```
Jdbc url, password and login is configured in the file:
```
application.properties
```

## Usage
Running project:
```
mvn spring-boot:run
```

After start, application will run simulation and print results on console.
You can also invoke simulation using REST API:
```
localhost:9000/simulation/{numberOfBaskets}/{maxNumberOfItemsInBasket}
```

## Pitest mutations and code coverage 
I used  [pitest](http://pitest.org) for mutation testing. You can execute
```
mvn org.pitest:pitest-maven:mutationCoverage
```
and check pitest report in directory:
```
target/pit-reports/
```