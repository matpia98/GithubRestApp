# GithubRestApp

## Overview

GithubRestApp is a Spring Boot application that provides an API to fetch GitHub repositories for a given user and list the branches along with the latest commit SHA for each branch. The application also handles errors gracefully, including cases where the user is not found.

## Features

- List all GitHub repositories of a user excluding forks.
- Fetch branches and their latest commit SHA for each repository.
- Handle user not found errors with a proper 404 response.
- Handle API rate limit exceeded errors with a proper 403 response.

## Prerequisites

- Java 21 or higher
- Maven
- An IDE of your choice (e.g., IntelliJ IDEA, Eclipse)
- A GitHub personal access token (if required)

## Getting Started

### Clone the Repository

Clone the repository to your local machine:
```bash
git clone https://github.com/matpia98/GithubRestApp.git
```

Navigate to the project directory:
```bash
cd githubrestapp
```

Compile and package the application using Maven:
```bash
mvn clean install
```

Run the application:
```bash
java -jar target/githubrestapp-0.0.1-SNAPSHOT.jar
```

## Running the Project with IntelliJ IDEA
**Clone the Repository:**
1. Open IntelliJ IDEA.
2. Go to File > New > Project from Version Control.
3. Enter the repository URL: https://github.com/matpia98/GithubRestApp.git.
4. Click Clone.

**Import the Project:**
After cloning, IntelliJ IDEA will automatically detect the project and prompt to import it as a Maven project. Click Yes to import.

**Configure github token if necessary:**
1. Go to src/main/resources/application.properties
2. Replace your_github_token with your actual Github access token
```properties
github.api.token=your_github_token
```
3. Go to the FeignConfig class and uncomment the following lines:
```java
    //    @Bean
    //    public RequestInterceptor requestInterceptor() {
    //        return requestTemplate -> {
    //            if (!githubToken.isEmpty()) {
    //                requestTemplate.header("Authorization", "token " + githubToken);
    //            }
    //        };
    //    }
```
**Run the Application:**
1. Navigate to src/main/java/com/example/githubrestapp/GithubRestAppApplication.java.

2. Right-click on the GithubRestAppApplication class and select Run 'GithubRestAppApplication'.

3. The application will start, and you can access it at http://localhost:8080.

## Usage

To retrieve all repositories from a user, send a GET request to the endpoint with the username:

```curl
GET /user/{username}
```

For example, using curl:

```curl
curl -X GET http://localhost:8080/user/{username} -H "Accept: application/json"
```

Replace `{username}` with the actual GitHub username.

## API Endpoints
Get Repositories and Branches

URL: /user/{username}

Method: GET

Headers: Accept: application/json

Response:
``` json
[
  {
    "name": "repo1",
    "login": "username",
    "branches": [
      {
        "name": "main",
        "commit": {
          "sha": "sha123"
        }
      }
    ]
  },
  {
    "name": "repo2",
    "login": "username",
    "branches": [
      {
        "name": "develop",
        "commit": {
          "sha": "sha456"
        }
      }
    ]
  }
]
```
Error Handling
User Not Found (404)

``` json
{
  "status": 404,
  "message": "User not found"
}
```
API Rate Limit Exceeded (403)

``` json
{
  "status": 403,
  "message": "API rate limit exceeded"
}
```

## Running Tests
The project includes unit and integration tests. To run the tests, use the following command:

```bash
mvn test
```
### Project Structure

**domain**: Contains the business logic and service classes.

**infrastructure/controller**: Contains the REST controllers.

**infrastructure/http**: Contains HTTP clients and related configurations.

**infrastructure/handlers**: Contains global exception handlers.

**dto**: Contains Data Transfer Objects (DTOs) for request and response.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue to discuss any changes.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

## Contact

- Developer: [Mateusz Piasecki](https://github.com/matpia98)
- Project Link: [GithubRestApp](https://github.com/matpia98/GithubRestApp)

We welcome your questions and contributions!
