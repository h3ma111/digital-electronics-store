# Electronics digital cart springboot backend
  - User authentication
  - CRUD on products and carts

### Tech

The shopping cart uses the following technologies to work:

* Java 1.8
* Built with gradle 3.5
* SpringBoot framework

### Remarks
 * For the moment, the project uses only the default test database from the MongoRepository.
 * All the repositories are flushed on application start, this is done in the ElectronicsStoreApplication class, comment this:
 					cartUserRepository.deleteAll();
 					productRepository.deleteAll();
 					shoppingCartRepository.deleteAll();
 					at the lines 59 to 61.
 * Default users are set in the db on application start, uname: kevinc, psw: kevin, uname: johnw, psw: john
 * Default products are also set in the db on application start.

Installation:
```
$ sudo apt-get install mongodb-org
$ sudo service mongod start
```

Build with gradle and start the server.

```sh
$ cd electronics
$ ./gradlew build
$ ./gradlew bootRun
```

Or

```sh
$ cd electronics
$ ./gradlew build
$ java -jar build/libs/com.digital.electronics-0.0.1-SNAPSHOT.jar
```

## REST API
This API is a JSON RESTful API. The client is expected to send
`application/json` as `Content-Type` and a json web token (format: `Bearer <token>`) as `Authorization` HTTP Header and JSON formatted bodies.

### Users
###### Request
`POST /users/signup`
Registers a user
```
$ curl -i -H "Content-Type: application/json" -X POST -d '{"userName":<uname>, "password":<psw>, "email": <email>}' http://localhost:8080/users/signup

```
###### Response
###### 201 Created
The user has been successfully registered

###### Request
`POST /login`
Registers a user
```
$ curl -i -H "Content-Type: application/json" -X POST -d '{"userName":<uname>, "password":<psw>}' http://localhost:8080/users/signup

```
###### Response
###### 200 OK
login successful and token located in 'Authorization' header with the format explained above

###### Request
`GET /users`
Gets the users list
```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/users

```
###### Response
###### 200 OK
fetched users list, example response:
```
[
    {
        "id":"59d8a05d479ded183906fa3d",
        "firstName":"Kevin",
        "lastName":"Chung",
        "userName":"kevin",
        "password":"$2a$10$W8kg2OC8URIqkHQf4f65FuAwr5YadPiIR2TISlzKEqJNU/OuBruHG",
        "email":"kevin@gmail.com"
    },
...
]
```

###### Request
`GET /users/{userId}`
Gets a user from its Id
```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/users/{userId}

```
###### Response
###### 200 OK
fetched user

###### Request
`DELETE /users/{userId}`
Deletes a user from its Id
```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X DELETE http://localhost:8080/users/{userId}

```
###### Response
###### 200 OK
user deleted

### Products
###### Request
`POST /products`
Adds a product

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X POST -d '<payload>' http://localhost:8080/products
```

Payload to send example:
```
{
  "productName": "sound system",
  "productCode": "S-4320",
  "releaseDate": "123123134",
  "description": "soundsystem",
  "price": 19.95,
  "starRating": 3.0,
  "imageUrl": "https://openclipart.org/detail/315898/sound-system"
}
```
###### Response
###### 201 CREATED
product created

###### Request
`PUT /products/{productId}`
Updates a product

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X POST http://localhost:8080/products
```
###### Response
###### 200
product updated payload with new values in response

###### Request
`GET /products`
Fetches a product list

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/products
```
###### Response
###### 200
product list

###### Request
`GET /products/{productId}`
Fetches a product

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/products/{productId}
```
###### Response
###### 200
product payload

###### Request
`DELETE /products/{productId}`
Deletes a product

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X DELETE http://localhost:8080/products/{productId}
```
###### Response
###### 200
product deleted

### Shopping carts
###### Request
`GET /carts`
Gets all the shopping carts

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/carts
```

###### Response
###### 200
shopping carts list

Response payload example:
```
[
    {
        "id":"59d89875479ded2a718bac15",
        "status":"pending",
        "userName":"testuser","
        products":{
            "59d89875479ded2a718bac13":{
                "id":"59d89875479ded2a718bac13",
                "productName":"soundsystem",
                "productCode":"S-1206",
                "releaseDate":34743774734,
                "lastModified":23238232,
                "description":"dentifrice",
                "price":3.95,
                "starRating":4.5,"
                imageUrl":"http://soundsystem.coom/2/"
            },
            "59d89875479ded2a718bac14":{
                "id":"59d89875479ded2a718bac14",
                "productName":"mouse",
                "productCode":"M-1206",
                "releaseDate":2332332323,
                "lastModified":23323223323,
                "description":"moussearaser",
                "price":2.95,
                "starRating":4.2,
                "imageUrl":"http://mouse.com/2"
            }
        },
        "productQuantities":{"59d89875479ded2a718bac13":1,"59d89875479ded2a718bac14":2},
        "lastModified":233223334343,
        "orderDate":2343445455,
        "totalPrice":0.0
    },
    ...
]
```

###### Request
`GET /carts/user/{userName}`
Gets all the shopping carts for the given username

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/carts/user/{userName}
```

###### Response
###### 200
shopping carts list for the username, same format response as previous request

###### Request
`GET /carts/{cartId}`
Gets a shopping cart from its Id

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X GET http://localhost:8080/carts/{cartId}
```

###### Response
###### 200
shopping carts list for the username, same format response as previous request

###### Request
`POST /carts`
Adds a shopping cart

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X POST -d '<payload>' http://localhost:8080/carts
```

Payload to send example:
```
{
    "id":"",
    "status":"pending",
    "userName":"testuser",
    "products":{},
    "productQuantities": {},
    "lastModified": <epoch>,
    "orderDate": <epoch>",
    "totalPrice": <float>
}
```
###### Response
###### 201 CREATED
shopping cart created


###### Request
`PUT /carts/{cartId}`
Updates a shopping cart

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X POST -d '<payload>' http://localhost:8080/carts/{cartId}
```

Payload to send example:
```
Same as in POST but the id attribute in the payload is mandatory
```
###### Response
###### 200 OK
shopping cart updated

###### Request
`DELETE /carts/{cartId}`
Deletes a shopping cart

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X DELETE http://localhost:8080/carts/{cartId}
```
###### Response
###### 200
shopping cart deleted

###### Request
`POST /carts/{cartId}/product/{productId}`
Adds a product to a shopping cart

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X POST http://localhost:8080/carts/{cartId}/product/{productId}
```

###### Response
###### 200
new shopping cart payload updated with the new product
###### 202
shopping cart processed but product not added because of stock too low
###### Request
`DELETE /carts/{cartId}/product/{productId}`
Removes a product from a shopping cart

```
$ curl -i -H "Content-Type:application/json" -H "Authorization:<token>" -X DELETE http://localhost:8080/carts/{cartId}/product/{productId}
```

###### Response
###### 200
new shopping cart payload updated without the new product in its list or with an updated
productQuantities attribute if there was more than one product

## Errors
### 401 Unauthorized
When a request is sent without the token

### 404 Not Found
When an item (shopping cart, product, user) has not been found

### 409 Conflict
When the item already exists, raised when a user signs up with an already existing username or email for example
