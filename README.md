
# Social Network Post

In the project you can create,update,delete,read post and moreover you can
You can find the top 10 views posts
## Features

- Redis Cache:It caches the 10 posts with the highest view count.It also checks every post that has a view update. If a new update view count is higher than the lowest post view count in the cache, it swaps
- Flyway: Also does something cool
- Feature 3: Does something else that's cool

## Getting started

To download and run this project locally, you'll need to have JAVA 17,Maven and Redis installed. 
Then, you can follow these steps:

1. Clone this repository
2. Run [docker-compose.yml](docker-compose.yml) to install dependencies
3. Run [PostApplication.java](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2FPostApplication.java) to start the server

## Modules
[CACHE](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fcache) : This module contains the caching logic for the top 10 most popular social network posts.

[Unit/Integration Tests](src%2Ftest)

[Config](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fconfig)

[Controller](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fcontroller) : This module contains the RESTful API endpoints for managing social network posts and comments.

[Dto](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fdto)

[Exception](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fexception)

[Mapper](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fmapper)

[Model](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fmodel)

[Repository](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Frepository) This module contains the JPA repositories for persisting social network posts and comments to a database

[Service](src%2Fmain%2Fjava%2Fcom%2Fsocialnetwork%2Fpost%2Fservice)

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
