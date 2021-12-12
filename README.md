# JPA-ID-REPRODUCER ISSUES
[Cockroach Labs Ticket](https://support.cockroachlabs.com/hc/en-us/requests/10751)

## Letting Hibernate create the SCHEMA (dev.quarkus.hibernate-orm.database.generation=drop-and-create)
IDs work as expected against all db (H2, PostgreSQL, CockroachDB)

## Using Flyway to Create the SCHEMA causes
> 2021-12-12 17:09:21,524 DEBUG [org.hib.res.jdb.int.LogicalConnectionManagedImpl] (executor-thread-0) `hibernate.connection.provider_disables_autocommit` was enabled.  This setting should only be enabled when you are certain that the Connections given to Hibernate by the ConnectionProvider have auto-commit disabled.  Enabling this setting when the Connections do not have auto-commit disabled will lead to Hibernate executing SQL operations outside of any JDBC/SQL transaction.
> 2021-12-12 17:09:21,525 DEBUG [org.hib.res.tra.bac.jta.int.JtaTransactionCoordinatorImpl] (executor-thread-0) Hibernate RegisteredSynchronization successfully registered with JTA platform
> 2021-12-12 17:09:21,526 DEBUG [org.hib.eve.int.EntityCopyObserverFactoryInitiator] (executor-thread-0) Configured EntityCopyObserver strategy: disallow
> 2021-12-12 17:09:21,527 DEBUG [org.hib.SQL] (executor-thread-0) 
>    select
>        nextval ('long_integer_seq')
> Hibernate:
>    select
>        nextval ('long_integer_seq')
> 2021-12-12 17:09:21,645 DEBUG [org.hib.id.enh.SequenceStructure] (executor-thread-0) Sequence value obtained: 5
> 2021-12-12 17:09:21,646 DEBUG [org.hib.res.jdb.int.ResourceRegistryStandardImpl] (executor-thread-0) HHH000387: ResultSet's statement was not registered
### 2021-12-12 17:09:21,647 DEBUG [org.hib.eve.int.AbstractSaveEventListener] (executor-thread-0) Generated identifier: -44, using strategy: org.hibernate.id.enhanced.SequenceStyleGenerator
> 2021-12-12 17:09:21,649 DEBUG [org.hib.eve.int.AbstractFlushingEventListener] (executor-thread-0) Processing flush-time cascades
> 2021-12-12 17:09:21,650 DEBUG [org.hib.eve.int.AbstractFlushingEventListener] (executor-thread-0) Dirty checking collections
> 2021-12-12 17:09:21,650 DEBUG [org.hib.eve.int.AbstractFlushingEventListener] (executor-thread-0) Flushed: 1 insertions, 0 updates, 0 deletions to 1 objects
> 2021-12-12 17:09:21,651 DEBUG [org.hib.eve.int.AbstractFlushingEventListener] (executor-thread-0) Flushed: 0 (re)creations, 0 updates, 0 removals to 0 collections
> 2021-12-12 17:09:21,652 DEBUG [org.hib.int.uti.EntityPrinter] (executor-thread-0) Listing entities:
> 2021-12-12 17:09:21,653 DEBUG [org.hib.int.uti.EntityPrinter] (executor-thread-0) alliancecoder.sequences.entity.EntityUsingLong{nonUniqueText=FIFTH RECORD, longAsId=-44, otherUniqueItem=5}
> 2021-12-12 17:09:21,654 DEBUG [org.hib.SQL] (executor-thread-0) 
>     insert 
>     into
>         entities_using_long
>         (non_unique_text, other_unique_item, long_as_id)
>     values
>         (?, ?, ?)
> Hibernate: 
>     insert
>     into
>         entities_using_long
>         (non_unique_text, other_unique_item, long_as_id)
>     values
>         (?, ?, ?)

## Generated SQL for Sequence
show create table long_integer_seq;
     table_name    |                                           create_statement
-------------------+-------------------------------------------------------------------------------------------------------
  long_integer_seq | CREATE SEQUENCE public.long_integer_seq MINVALUE 1 MAXVALUE 9223372036854775807 INCREMENT 50 START 5

# RELEVANT LINKS
- [UUID JPA Implementation Patterns](https://dzone.com/articles/jpa-implementation-patterns-6)
- [CockroachDB Forum with Underlying Issue](https://forum.cockroachlabs.com/t/hibernate-sequence-generator-returns-negative-number-and-ignore-unique-rowid/1885)
#### RELEVANT RESPONSE
> knz
> Raphael 'kena' PossRoacher
> Aug '18

> Hello!
> Thank you for your interest in CockroachDB.

> There are two aspects in your answer that I’d like to follow-up on.

>     we strongly recommend against using sequences to generate row IDs, because they cause contention and limit scalability. We recommend using UUID keys instead. INT DEFAULT unique_rowid() is second best.

>     Please consult the following two FAQ entries for more details:
- [How do I auto-generate unique row IDs in CockroachDB? 33](https://www.cockroachlabs.com/docs/stable/sql-faqs.html#how-do-i-auto-generate-unique-row-ids-in-cockroachdb)
- [What are the differences between UUID, sequences, and unique_rowid()? 29](https://www.cockroachlabs.com/docs/stable/sql-faqs.html#what-are-the-differences-between-uuid-sequences-and-unique_rowid)

>     regarding your issues when you tried to use sequences.
>         When you use @Id in Hibernate with CockroachDB, this does not automatically create a sequence. This is different from PostgreSQL. As you can understand from my answer above, this difference comes from the fact that we recommend using other key generation instead. This is why the @GeneratedValue spec cannot work as-is.
>         When you generate the sequence manually with CREATE SEQUENCE, and keep @GeneratedValue, Hibernate does not reset the sequence counter between tests of your application. So each time you run your test, you will get different values. This explains your observation.
>         If you really want to use a sequence, then the solution you chose at the end (combine @GeneratedValue with @SequenceGenerator) is correct.

> Does this clarify? Feel free to follow up with additional questions or comments.