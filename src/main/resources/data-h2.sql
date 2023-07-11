SET @TEASER = '<p class="blog-content">As I stepped into the wilderness, I could feel the fresh breeze caressing my face, and the soothing sounds of birds singing in the distance. The trail ahead was filled with lush greenery and colorful wildflowers. It was a perfect escape from the chaos of the city, a chance to reconnect with nature.</p>';
SET @BODY = '<p class="blog-content">As I stepped into the wilderness, I could feel the fresh breeze caressing my face, and the soothing sounds of birds singing in the distance. The trail ahead was filled with lush greenery and colorful wildflowers. It was a perfect escape from the chaos of the city, a chance to reconnect with nature.</p>'
    || '<p class="blog-content">The winding path led me deeper into the heart of the forest. Tall trees towered above, creating a natural canopy that filtered the sunlight, casting mesmerizing shadows on the ground. The air was filled with the earthy scent of moss and damp soil, a scent that instantly brought a sense of tranquility.</p>'
    || '<p class="blog-content">Along the way, I encountered a gentle stream, its crystal-clear waters meandering through the rocks. I sat on a moss-covered stone, mesmerized by the rhythmic flow of water. The symphony of nature embraced me, and for a moment, I lost track of time.</p>'
    || '<p class="blog-content">As the sun began its descent, painting the sky with hues of orange and pink, I knew it was time to bid farewell to this enchanting paradise. But I carried with me the memories of this serene journey, a reminder of the beauty and peace that nature generously bestows upon us.</p>';

SET @DanId = select next value for author_seq;
SET @JohnId = select next value for author_seq;
SET @JaneId = select next value for author_seq;

insert into author (id, first_name, last_name, email) values (@DanId, 'Dan', 'Vega', 'therealvega@gmail.com');
insert into author (id, first_name, last_name, email) values (@JohnId, 'John', 'Doe', 'johndoe@gmail.com');
insert into author (id, first_name, last_name, email) values (@JaneId, 'Jane', 'Smith', 'janesmith@gmail.com');

insert into post (id, title, slug, teaser, body, active, author_id, posted_on) values (2, 'A Journey Through Nature', 'a-journey-through-nature', @TEASER, @BODY, TRUE, @JohnId, DATE'2023-03-12');
insert into post_keywords (post_id, keywords)
values
    (2, 'nature'), (2, 'wildlife'), (2, 'biodiversity'), (2, 'ecosystems'),
    (2, 'natural beauty'), (2, 'environmental conservation'), (2, 'outdoor activities'),
    (2, 'hiking'), (2, 'photography'), (2, 'sustainability'), (2, 'ecological balance');

SET @TEASER = '<p class="blog-content">Space, the final frontier. The vastness of the universe has always fascinated humanity, igniting our curiosity and sense of wonder. As we gaze up at the night sky, we can''t help but feel a sense of awe and humility.</p>';
SET @BODY = '<p class="blog-content">Space, the final frontier. The vastness of the universe has always fascinated humanity, igniting our curiosity and sense of wonder. As we gaze up at the night sky, we can''t help but feel a sense of awe and humility.</p>'
    || '<p class="blog-content">The cosmos holds countless mysteries, from distant galaxies and sparkling nebulae to celestial bodies that defy our understanding. Astronomers and space explorers have dedicated their lives to unraveling these mysteries, pushing the boundaries of human knowledge.</p>'
    || '<p class="blog-content">One of the most awe-inspiring phenomena in space is a black hole. These gravitational beasts, born from the remnants of massive stars, possess such immense gravitational pull that not even light can escape their grasp. They warp the fabric of spacetime itself, presenting a window into the mind-boggling possibilities of the universe.</p>'
    || '<p class="blog-content">Another fascinating aspect of space exploration is the search for extraterrestrial life. With the discovery of exoplanets in the habitable zone of distant star systems, the question of whether we are alone in the universe becomes even more compelling. Scientists are actively seeking signs of life beyond Earth, hoping to unlock the secrets of our cosmic neighbors.</p>';

insert into post (id, title, slug, teaser, body, active, author_id, posted_on) values (3, 'Exploring the Wonders of Space', 'exploring-the-wonders-of-space', @TEASER, @BODY, TRUE, @JaneId, DATE'2023-05-26');
insert into post_keywords (post_id, keywords)
values
    (3, 'space exploration'), (3, 'astronomy'), (3, 'celestial bodies'), (3, 'galaxies'),
    (3, 'planets'), (3, 'stars'), (3, 'universe'), (3, 'space missions'), (3, 'NASA'),
    (3, 'astrophysics'), (3, 'cosmic phenomena');

SET @TEASER = '<p class="blog-content">In the world of Java development, Spring Boot has become a popular framework for building robust and scalable applications. With its convention-over-configuration approach, Spring Boot simplifies the setup and configuration of Spring applications, allowing developers to focus more on writing business logic rather than dealing with boilerplate code.</p>';
SET @BODY = '<p class="blog-content">In the world of Java development, Spring Boot has become a popular framework for building robust and scalable applications. With its convention-over-configuration approach, Spring Boot simplifies the setup and configuration of Spring applications, allowing developers to focus more on writing business logic rather than dealing with boilerplate code.</p>'
    || '<p class="blog-content">Spring Boot provides a wide range of features and benefits. One of its key advantages is the embedded Tomcat, Jetty, or Undertow server, which allows you to run your application as a standalone executable JAR file. This makes deployment and distribution a breeze, as you don''t need to worry about external application servers or complex deployment processes.</p>'
    || '<p class="blog-content">Another noteworthy feature of Spring Boot is its auto-configuration capability. By analyzing your project''s classpath, Spring Boot can automatically configure various components and dependencies, saving you from the tedious task of manual configuration. Additionally, Spring Boot provides a rich ecosystem of starter dependencies that encapsulate common functionality, enabling you to quickly add features like database connectivity, security, and web services to your application.</p>'
    || '<p class="blog-content">With its emphasis on convention over configuration, Spring Boot promotes a highly productive development experience. The framework encourages best practices and provides sensible defaults, allowing developers to rapidly build applications without sacrificing flexibility. Moreover, Spring Boot seamlessly integrates with other Spring projects, such as Spring Data and Spring Security, further enhancing its capabilities.</p>';

insert into post (id, title, slug, teaser, body, active, author_id, posted_on) values (4, 'Introduction to Spring Boot: Simplify Your Java Projects', 'introduction-to-spring-boot-simplify-your-java-projects', @TEASER, @BODY, TRUE, @JohnId, DATE'2023-06-06');
insert into post_keywords (post_id, keywords)
values
    (4, 'Spring Boot'), (4, 'Java'), (4, 'web application'), (4, 'framework'),
    (4, 'microservices'), (4, 'Spring MVC'), (4, 'Spring Data'), (4, 'RESTful API'),
    (4, 'dependency injection'), (4, 'annotations'), (4, 'application configuration');

SET @TEASER = '<p class="blog-content">Spring Data is a powerful module within the Spring Framework that provides a high-level abstraction for data access in Java applications. It simplifies the development of data-driven applications by reducing boilerplate code and providing support for various data stores, including relational databases, NoSQL databases, and more.</p>';
SET @BODY = '<p class="blog-content">Spring Data is a powerful module within the Spring Framework that provides a high-level abstraction for data access in Java applications. It simplifies the development of data-driven applications by reducing boilerplate code and providing support for various data stores, including relational databases, NoSQL databases, and more.</p>'
    || '<p class="blog-content">With Spring Data, developers can leverage the power of object-relational mapping (ORM) frameworks like Hibernate or JPA, or work with NoSQL databases like MongoDB or Redis. Spring Data eliminates the need for manual SQL queries and offers a repository abstraction that allows developers to perform common database operations using simple and expressive interfaces.</p>'
    || '<p class="blog-content">Spring Data also provides advanced querying capabilities, allowing developers to define dynamic queries using criteria queries or specifications. It supports pagination, sorting, and caching out-of-the-box, making it easy to handle large datasets and optimize performance.</p>'
    || '<p class="blog-content">In addition, Spring Data integrates seamlessly with other Spring projects, such as Spring Boot, Spring MVC, and Spring Security, enabling developers to build end-to-end data-driven applications with ease. The framework promotes best practices and follows the principle of "convention over configuration," further enhancing developer productivity.</p>';

insert into post (id, title, slug, teaser, body, active, author_id, posted_on) values (5, 'Mastering Data Access with Spring Data', 'mastering-data-access-with-spring-data', @TEASER, @BODY, TRUE, @JohnId, DATE'2023-06-08');
insert into post_keywords (post_id, keywords)
values
    (5, 'Spring Data'), (5, 'database access'), (5, 'Spring applications'), (5, 'ORM'),
    (5, 'JPA'), (5, 'Hibernate'), (5, 'repositories'), (5, 'CRUD operations'),
    (5, 'data persistence'), (5, 'query methods'), (5, 'relational databases');

SET @TEASER = '<p class="blog-content">Java is a widely-used programming language known for its versatility and robustness. As a Java developer, following good practices is essential to ensure code quality, maintainability, and efficiency. Here are some key practices that every Java developer should keep in mind:</p>';
SET @BODY = '<p class="blog-content">Java is a widely-used programming language known for its versatility and robustness. As a Java developer, following good practices is essential to ensure code quality, maintainability, and efficiency. Here are some key practices that every Java developer should keep in mind:</p>'
              || '<ul class="blog-content">'
              || '<li>Write clean and readable code by following proper naming conventions, using meaningful variable and method names, and organizing code into logical modules.</li>'
              || '<li>Adhere to object-oriented principles, such as encapsulation, inheritance, and polymorphism, to create modular and reusable code.</li>'
              || '<li>Use appropriate exception handling techniques to gracefully handle errors and prevent application crashes.</li>'
              || '<li>Follow the DRY (Don''t Repeat Yourself) principle to avoid code duplication and promote code reusability.</li>'
              || '<li>Write comprehensive unit tests to ensure the correctness of your code and facilitate future refactoring.</li>'
              || '<li>Utilize design patterns and frameworks to solve common software development problems and enhance code maintainability.</li>'
              || '<li>Regularly update and patch dependencies to leverage the latest features, bug fixes, and security enhancements.</li>'
              || '</ul>'
              || '<p class="blog-content">By following these good practices, Java developers can create high-quality, maintainable code that is easier to understand, debug, and extend. Embracing a clean and disciplined coding style not only benefits individual developers but also contributes to the overall success of software projects.</p>';

insert into post (id, title, slug, teaser, body, active, author_id, posted_on) values (6, 'Good Practices for Java Developers', 'good-practices-for-java-developers', @TEASER, @BODY, TRUE, @JaneId, DATE'2023-06-07');
insert into post_keywords (post_id, keywords)
values
    (6, 'Java developers'), (6, 'programming'), (6, 'coding practices'), (6, 'clean code'),
    (6, 'naming conventions'), (6, 'object-oriented principles'), (6, 'exception handling'),
    (6, 'DRY principle'), (6, 'unit testing'), (6, 'design patterns'), (6, 'dependencies'),
    (6, 'best practices'), (6, 'code readability'), (6, 'code reusability'), (6, 'code optimization'),
    (6, 'error handling'), (6, 'exception management'), (6, 'code documentation');

-- Insert non-JPA bounded table data

-- Insert dummy data into Client table
INSERT INTO "Client" (id, name, email, birthdate)
VALUES
  (4, 'John Doe', 'johndoe@example.com', '1990-05-15'),
  (5, 'Jane Smith', 'janesmith@example.com', '1985-09-20'),
  (6, 'Alex Johnson', 'alexjohnson@example.com', '1995-02-10'),
  (7, 'Sarah Thompson', 'sthompson@example.com', '1992-11-28'),
  (8, 'Michael Brown', 'mbrown@example.com', '1988-07-03');

-- Insert dummy data into Product table
INSERT INTO Product (id, name, price, quantity)
VALUES
  (4, 'iPhone 12', 899.99, 10),
  (5, 'Samsung Galaxy S21', 799.99, 15),
  (6, 'Sony PlayStation 5', 499.99, 5),
  (7, 'MacBook Pro', 1999.99, 8),
  (8, 'Canon EOS R6', 2499.99, 3);

-- Insert dummy data into Order table
INSERT INTO "Order" (id, client_id, product_id, quantity, total_price, status)
VALUES
  (4, 4, 4, 2, 1799.98, 'Pending'),
  (5, 5, 5, 1, 499.99, 'Shipped'),
  (6, 6, 6, 3, 2399.97, 'Pending'),
  (7, 7, 7, 1, 1999.99, 'Shipped'),
  (8, 8, 8, 2, 4999.98, 'Pending');
