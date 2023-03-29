
# Social Network Post

In the project you can create,update,delete,read post and moreover you can
You can find the top 10 views posts
## Features
- Crud Posts :Available for creating new posts, updating,read and deleting.
- Higest Posts : Shows the highest viewed posts.
- Spring Actuator : Actuator endpoints let you monitor and interact with your application.
- Redis Cache:It caches the 10 posts with the highest view count.It also checks every post that has a view update. If a new update view count is higher than the lowest post view count in the cache, it swaps
-
- Flyway:  Helps you implement automated and version-based database migrations. It allows you to define the required update operations in an SQL script or as Java code

## Getting started

To download and run this project locally, you'll need to have JAVA 17,Maven and Redis installed. 
Then, you can follow these steps:

1. Clone this repository
2. Run [docker-compose.yml](docker-compose.yml) to install dependencies
3. Run [PostApplication.java](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2FPostApplication.java) to start the server

## Modules
[CACHE](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fcache) : This module contains the caching logic for the top 10 most popular social network posts.

[Unit/Integration Tests](src%2Ftest) :  This module contains automated tests to ensure that each individual unit of code is functioning correctly and that the different units of code work together seamlessly.

[Config](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fconfig) :This module contains configuration files, such as properties files, that are used to set up and configure the social network application. It includes properties like database connection,Redis Configs and for more.

[Controller](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fcontroller) : This module contains the RESTful API endpoints for managing social network posts and comments.

[Dto](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fdto) :This module contains Data Transfer Objects (DTOs) that are used to transfer data between layers of the application. It includes classes that encapsulate data from the Model layer and present it to the Controller layer, or vice versa.

[Exception](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fexception)

[Mapper](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fmapper) :This module contains classes that are used to map data between different layers of the application. It includes classes that transform data from DTOs to entities and vice versa.

[Model](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fmodel) :This module contains the core data structures that represent social network posts and comments. It includes JPA entity classes that define the fields and relationships between the data, and it provides a consistent representation of the data across different layers of the application.

[Repository](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Frepository) This module contains the JPA repositories for persisting social network posts and comments to a database. It includes classes that implement the CRUD (Create, Read, Update, Delete) operations on the data, and it provides a simple and consistent way to access the data from other parts of the application.

[Service](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fservice) :This module contains the business logic that is responsible for managing social network posts and comments. It includes classes that implement the core functionality of the application, such as creating new posts, deleting comments, and updating post metadata.

[Utils](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Futils) :This module contains utility classes used by other modules.

## Contributing

If you'd like to contribute to this project, please follow these guidelines:

1. Fork this repository
2. Make your changes
3. Submit a pull request

## License

This project is released under the MIT License.

## Contact information

If you have any questions or want to report a bug, please contact me at bestasyusuf8@gmail.com.


## Important Rest API Examples->

http://localhost:8080/actuator

http://localhost:8080/v1/api/posts/top-ten
