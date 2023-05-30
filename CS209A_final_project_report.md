# CS209A final project report

> group members:
>
> 2126 白楚焓
>
> 2114 侯芳旻 

## data collection and storage

### data collection

#### framework

- We use Apache HttpClient library to make HTTP requests in Java. It provides a set of classes and methods that simplify the process of sending HTTP requests, handling responses, and managing connections.
- We use Alibaba FastJSON library to parse and generate JSON (JavaScript Object Notation) data. FastJSON is a high-performance JSON processing library that offers fast serialization and deserialization of JSON objects.

#### implementation ideas

- Creating an `HttpClient` instance and executes an HTTP GET request to the API endpoint using the constructed URL.
- The response is obtained as an `HttpEntity` object, and the response body is extracted as a string using `EntityUtils.toString()`.
- The response body, containing JSON data, is parsed using the `JSON.parseObject()` method from Alibaba FastJSON. 
- The parsed JSON data is processed to extract relevant information and store it in various lists (`questionList`, `answerList`, `commentList`, etc.).
- The extracted data is then passed to a `databaseService` object, where it is inserted into corresponding database tables using methods like `insertQuestionRecord()`, `insertAnswerRecord()`, and `insertCommentRecord()`.

### data storage

- We stored the data in a postgresql database.  

- Methods and functionality related to interacting with PostgreSQL database are provided in a Java class named `databaseService`  . The class provides functionality to connect to a PostgreSQL database, create tables, and insert records into those tables based on JSON data.
- We use 5 tables to store the data colleted for stackoverflow, including a table to link properties of question and tag. These 5 tables are connected by the id of question, answer, comment and tag. 

![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.34.24.png)

## project design

### architecture

Our project are consist of these following packages:

- `model` package is used to store data models of out project. As it mirrors the structure of the database tables, each class in this package represents an entity we are working with.
- `controller` handles the presentation layer, accepts input, interacting with the client-side, and returning appropriate responses. The controller acts as an intermediary between the client and the backend services.
- `repository` package offer a simplified and consistent approach for performing common CRUD (Create, Read, Update, Delete) operations  on database entities. In our project, we create a repository for each of the entity: answer, comment, question and tag. 
- `service` package includes classes that define the operations related to the requirements. It encapsulates the logic for processing and manipulating the data from the repositories and performs any additional business-specific operations. The service layer acts as an intermediary between the controllers (or presentation layer) and the repositories.
- `dataCollect` package is used for data collection and data storage. Three classes in the package provide the implementation logic of collecting data from stackoverflow and storing them in the specified postgresql database. 
- `utils` package stores frequently used method when analysising data. It can be considered as a separate part of the project architecture. 
- `templates` package stores the HTML web pages, including a index page(the home page) and 4 subpages showing the data analysis result of each part. 

![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.28.31.png)

### important parts

- In `dataCollection` class, use Alibaba FastJSON library to parse and generate JSON (JavaScript Object Notation) data. 

```java
String url = "https://api.stackexchange.com/2.3/questions";
            String params = String.format("page=%d&pagesize=%d&order=desc&sort=activity" +
                    "&tagged=java&filter=withbody&site=stackoverflow&" +
                    "key=X5JIQM2Vlsnh85sg*BQ6OA((", i * pageStep, pageSize);
            String apiURL = url + "?" + params;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(apiURL);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBody = EntityUtils.toString(entity);
                JSONObject data = JSON.parseObject(responseBody);
                if (data.getJSONArray("items") == null) continue;
                for (int j = 0; j < pageSize; j++) {
                    JSONObject item = data.getJSONArray("items").getJSONObject(j);
                    questionList.add(item);
                }
            }
            response.close();
            httpClient.close();
```

- Controllers use annotations  `@RequestMapping`, `@GetMapping` to map the incoming requests to specific methods within the controller. 

- `RestController` class implemented several `@GetMapping` annotated methods defined the endpoints for handling specific HTTP GET requests.

  - `getQuestions`: Handles the request to retrieve questions based on specified tags. It accepts a list of tags as a request parameter and returns a `ResponseEntity` containing a pair of the number of questions (`sum`) and the list of questions (`questions`).
  - `getQuestionrById`: Retrieves a specific question by its ID (`questionId`) and returns it as a `ResponseEntity`. If the question is not found, it returns a "not found" response.
  - `getAnswers`: Retrieves the answers for a specific question identified by the `questionId` request parameter. If the question is found, it returns a `ResponseEntity` containing the list of answers. Otherwise, it returns a "not found" response.
  - `getAnswerById`: Retrieves a specific answer by its ID (`answerId`) and returns it as a `ResponseEntity`. If the answer is not found, it returns a "not found" response.
  - `getComments`: Retrieves the comments for a specific answer identified by the `answerId` request parameter. If the answer is found, it returns a `ResponseEntity` containing the list of comments. Otherwise, it returns a "not found" response.
  - `getCommentById`: Retrieves a specific comment by its ID (`commentId`) and returns it as a `ResponseEntity`. If the comment is not found, it returns a "not found" response.
  - `getTop40Tag`: Retrieves the top 40 tags along with their frequencies as a list of pairs (`Pair<String, Integer>`) and returns them as a `ResponseEntity`.

- For every class in `model` package, it represents a database entity by being annoted with `@Entity` and `@Table` . 

  - Take the `Question ` class as an example, the `Question` class represents a database entity for storing information about questions in the project. It defines the table structure, relationships with other entities, and includes additional methods for calculating user count and total score.
  - The `@Id` annotation marks the `questionId` variable as the primary key of the table.
  - The `@GeneratedValue` annotation with `GenerationType.IDENTITY` strategy indicates that the `questionId` will be automatically generated by the database upon insertion.
  - The `@JsonIgnore` annotation is used to exclude the `answerList` property from serialization/deserialization. It is typically used to avoid circular references or to hide sensitive information from being exposed in the JSON response.
  - The `@OneToMany` annotation is used to define a one-to-many relationship between the `Question` entity and the `Answer` entity. It specifies that a question can have multiple answers. The `mappedBy` attribute refers to the corresponding property in the `Answer` entity that maps the relationship.
  - The `@ManyToMany` annotation is used to define a many-to-many relationship between the `Question` entity and the `Tag` entity. It represents that a question can have multiple tags, and a tag can be associated with multiple questions. The relationship is mapped through a join table named "question_tag".
  - The `@Transient` annotation is used to mark the `getUserCount()` and `getTotalScore()` methods as transient, indicating that they are not persistent properties and should not be mapped to database columns.
  - The `equals()` and `hashCode()` methods are overridden to provide equality comparison based on the `questionId` and other relevant properties of the `Question` object.

  ```java
  package cse.java2.project.model;
  
  import com.fasterxml.jackson.annotation.JsonIgnore;
  import lombok.Data;
  
  import javax.persistence.*;
  import java.sql.Timestamp;
  import java.util.List;
  import java.util.Objects;
  
  @Entity
  @Table(name="question")
  @Data
  public class Question {
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Id
      @Column(name = "question_id")
      private Long questionId;
  
      @Column(name = "score")
      private int score;
  
      @Column(name = "link")
      private String link;
  
      @Column(name = "answer_count")
      private int answerCount;
  
      @Column(name = "view_count")
      private int viewCount;
      
      @Column(name = "content_license")
      private String contentLicense;
  
      @Column(name = "title")
      private String title;
      
      @Column(name = "last_activity_date")
      private Timestamp lastActivityDate;
      
      @Column(name = "last_edit_date")
      private Timestamp lastEditDate;
      
      @Column(name = "creation_date")
      private Timestamp creationDate;
      
      @Column(name = "account_id")
      private Long accountId;
      
      @Column(name = "body")
      private String body;
  
      @JsonIgnore
      @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
      private List<Answer> answerList;
  
      @ManyToMany(cascade = CascadeType.ALL)
      @JoinTable(
              name = "question_tag",
              joinColumns = @JoinColumn(name = "question_id"),
              inverseJoinColumns = @JoinColumn(name = "tag_id"))
      private List<Tag> tagList;
  
      @Transient
      public int getUserCount() {
          int count = 1;
          for(Answer answer : answerList) {
              count+= answer.getUserCount(accountId);
          }
          return count;
      }
  
      @Transient
      public int getTotalScore() {
          int totalScore = score;
          for(Answer answer : answerList) {
              totalScore += answer.getScore();
          }
          return totalScore;
      }
  
      @Override
      public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Question question = (Question) o;
          return Objects.equals(questionId, question.questionId) && score == question.score && answerCount == question.answerCount && viewCount == question.viewCount && Objects.equals(accountId, question.accountId) && Objects.equals(link, question.link) && Objects.equals(contentLicense, question.contentLicense) && Objects.equals(title, question.title) && Objects.equals(lastActivityDate, question.lastActivityDate) && Objects.equals(lastEditDate, question.lastEditDate) && Objects.equals(creationDate, question.creationDate) && Objects.equals(body, question.body);
      }
  
      @Override
      public int hashCode() {
          return Objects.hash(questionId, score, link, answerCount, viewCount, contentLicense, title, lastActivityDate, lastEditDate, creationDate, accountId, body);
      }
  }
  
  
  ```

- Every interface in `repository` package extends the `JpaRepository` interface, which provides generic CRUD (Create, Read, Update, Delete) operations for the corresponding entity, as well as additional querying capabilities.

- In `templates` package, the subpages displaying the data analysis result are implemented by HTML file using `Thymeleaf`with embedded JavaScript code that generates various charts using the `ECharts` library. The specific data for the charts is expected to be provided by the server-side and passed to the HTML template using `Thymeleaf` expressions.

  ```html
  <!DOCTYPE html>
  <html xmlns:th="http://www.thymeleaf.org" lang="en">
  <head>
      <meta charset="UTF-8">
      <title>AcceptedAnswers</title>
      <script src="https://cdn.jsdelivr.net/npm/echarts@5.4.0/dist/echarts.min.js"></script>
  </head>
  ```

## insights

![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.39.06.png)



![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.45.17.png)

![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.45.38.png)

![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.46.10.png)

![](/Users/slina/Library/CloudStorage/OneDrive-南方科技大学/university/2023spring/CS209A/final_project/Screenshot 2023-05-30 at 05.46.28.png)