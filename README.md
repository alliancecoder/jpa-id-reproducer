# jpa-id-reproducer Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/jpa-id-reproducer-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

[UUID JPA Implementation Patterns](https://dzone.com/articles/jpa-implementation-patterns-6)


[CockroachDB Forum with Underlying Issue](https://forum.cockroachlabs.com/t/hibernate-sequence-generator-returns-negative-number-and-ignore-unique-rowid/1885)
#### RELEVANT RESPONSE
knz
Raphael 'kena' PossRoacher
Aug '18

Hello!
Thank you for your interest in CockroachDB.

There are two aspects in your answer that I’d like to follow-up on.

    we strongly recommend against using sequences to generate row IDs, because they cause contention and limit scalability. We recommend using UUID keys instead. INT DEFAULT unique_rowid() is second best.

    Please consult the following two FAQ entries for more details:
        How do I auto-generate unique row IDs in CockroachDB? 33
        What are the differences between UUID, sequences, and unique_rowid()? 29

    regarding your issues when you tried to use sequences.
        When you use @Id in Hibernate with CockroachDB, this does not automatically create a sequence. This is different from PostgreSQL. As you can understand from my answer above, this difference comes from the fact that we recommend using other key generation instead. This is why the @GeneratedValue spec cannot work as-is.
        When you generate the sequence manually with CREATE SEQUENCE, and keep @GeneratedValue, Hibernate does not reset the sequence counter between tests of your application. So each time you run your test, you will get different values. This explains your observation.
        If you really want to use a sequence, then the solution you chose at the end (combine @GeneratedValue with @SequenceGenerator) is correct.

Does this clarify? Feel free to follow up with additional questions or comments.