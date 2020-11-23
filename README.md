git statgit gtii# Unattended-library-programming-exercise

This exercise completes acceptance criteria for the implementation of book service interface. The book service can also be called from the api and uses in-memory H2 db for the book repository with pre-loaded data from data.sql. 

## Get up and running

1. Unzip the package

2. Ensure that you have:
   * java installed on your system (1.8)
   * gradle

3. Run application using the following:

        ./gradlew clean bootRun

   With the backend running you can access the application at: `http://localhost:8080`

## API:

   1. Book API: `GET /books/book?bookReference=<bookReference>`
   2. Book summary API: `GET /books/summary?bookReference=<bookReference>`

 Response status code
`200 - OK`

* Produces
 1. `application/json`
 2.  `text`

* Response object
    * Example value of response | Book

    ```
    {
        "reference": "BOOK-GRUFF472",
        "title": "The Gruffalo",
        "review": "A mouse taking a walk in the woods."
    }
    ```
    
    * Example value of response | Summary text
       
       ```BOOK-GRUFF472 The Gruffalo A mouse taking a walk in the woods.```

* Errors:

    `400` - Bad request 
        ```{
               "error": "Book reference format is invalid. It should begin with 'BOOK-'"
           }```

    `404` - Not found
      ```{
             "error": "BookNotFoundException. Not found book with the given reference"
         }```
      
