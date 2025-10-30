# ARQSOFT_1240466_1240464

This README has all the documentation for the P1 of ARQSOFT project.

**Group**:
- Ivo Moutinho : 1240464
- João Cordeiro : 1240466

## Index

- [UML Diagrams](#uml-diagrams)
  - [System-as-is](#system-as-is)
    - [Logic View](#logic-view)
      - [Level 1](#level-1)
      - [Level 2](#level-2)
      - [Level 3](#level-3)
      - [Level 4](#level-4)
    - [Process View](#process-view)
      - [Level 1](#level-1-1)
      - [Level 2](#level-2-1)
      - [Level 3](#level-3-1)
      - [Level 4](#level-4-1)
    - [Development View](#development-view)
      - [Level 1](#level-1-2)
      - [Level 2](#level-2-2)
      - [Level 3](#level-3-2)
      - [Level 4](#level-4-2)
    - [Physical View](#physical-view)
      - [Level 1](#level-1-3)
      - [Level 2](#level-2-3)
      - [Level 3](#level-3-3)
      - [Level 4](#level-4-3)
  - [System-to-be](#system-to-be)
    - [Logic View](#logic-view-1)
      - [Level 1](#level-1-4)
      - [Level 2](#level-2-4)
      - [Level 3](#level-3-4)
      - [Level 4](#level-4-4)
    - [Process View](#process-view-1)
      - [Level 1](#level-1-5)
      - [Level 2](#level-2-5)
      - [Level 3](#level-3-5)
      - [Level 4](#level-4-5)
    - [Development View](#development-view-1)
      - [Level 1](#level-1-6)
      - [Level 2](#level-2-6)
      - [Level 3](#level-3-6)
      - [Level 4](#level-4-6)
    - [Physical View](#physical-view-1)
      - [Level 1](#level-1-7)
      - [Level 2](#level-2-7)
      - [Level 3](#level-3-7)
      - [Level 4](#level-4-7)
- [Funcionality 1 - Data Persistence in Multiple Data Models](#funcionality-1---data-persistence-in-multiple-data-models)
  - [Quality Attribute Scenario](#quality-attribute-scenario)
  - [Technical Memo](#technical-memo)
    - [Problem](#problem)
    - [Summary of Solution](#summary-of-solution)
    - [Factors](#factors)
    - [Solution](#solution)
    - [Motivation](#motivation)
    - [Alternatives](#alternatives)
    - [Pending Issues](#pending-issues)
- [Funcionality 2 - Retrieve Book ISBN from External APIs](#funcionality-2---retrieve-book-isbn-from-external-apis)
  - [Quality Attribute Scenario](#quality-attribute-scenario-1)
  - [Technical Memo](#technical-memo-1)
    - [Problem](#problem-1)
    - [Summary of Solution](#summary-of-solution-1)
    - [Factors](#factors-1)
    - [Solution](#solution-1)
    - [Motivation](#motivation-1)
    - [Alternatives](#alternatives-1)
    - [Pending Issues](#pending-issues-1)
- [Funcionality 3 - Generate IDs in Multiple Formats](#funcionality-3---generate-ids-in-multiple-formats)
  - [Quality Attribute Scenario](#quality-attribute-scenario-2)
  - [Technical Memo](#technical-memo-2)
    - [Problem](#problem-2)
    - [Summary of Solution](#summary-of-solution-2)
    - [Factors](#factors-2)
    - [Solution](#solution-2)
    - [Motivation](#motivation-2)
    - [Alternatives](#alternatives-2)
    - [Pending Issues](#pending-issues-2)

---

## UML Diagrams

### System-as-is

**System-as-is** , reverse engineering design, is the practice of understanding and documenting an existing system rather than building a new one from scratch. It's primarily used when original documentation is missing or outdated. The goal is to create an accurate map of the current system, which allows developers to make informed decisions for optimization, bug fixes, or migration without the risk of breaking critical functionalities.

#### Logic View

##### Level 1

![Logic View Level 1](/Documentation/Images/VL/As-Is/VL_AsIs_1.png "Logic View Level 1")

##### Level 2

![Logic View Level 2](/Documentation/Images/VL/As-Is/VL_AsIs_2.png "Logic View Level 2")

##### Level 3

![Logic View Level 3](/Documentation/Images/VL/As-Is/VL_AsIs_3.png "Logic View Level 3")

#### Process View

##### Level 1

![Process View Level 1](/Documentation/Images/VP/As-Is/VP_AsIs_1.png "Process View Level 1")

##### Level 2

![Process View Level 2](/Documentation/Images/VP/As-Is/VP_AsIs_2.png "Process View Level 2")

##### Level 3

![Process View Level 3](/Documentation/Images/VP/As-Is/VP_AsIs_3.png "Process View Level 3")

##### Level 4

![Process View Level 4](/Documentation/Images/VP/As-Is/VP_AsIs_4.png "Process View Level 4")

#### Development View

##### Level 1

![Development View Level 1](/Documentation/Images/VI/As-Is/VI_AsIs_1.png "Development View Level 1")

##### Level 2

![Development View Level 2](/Documentation/Images/VI/As-Is/VI_AsIs_2.png "Development View Level 2")

##### Level 3

![Development View Level 3](/Documentation/Images/VI/As-Is/VI_AsIs_3.png "Development View Level 3")

##### Level 4

![Development View Level 4](/Documentation/Images/VI/As-Is/VI_AsIs_4.png "Development View Level 4")

#### Physical View

##### Level 1

![Physical View Level 1](/Documentation/Images/VF/As-Is/VF_AsIs_1.png "Physical View Level 1")

##### Level 2

![Physical View Level 2](/Documentation/Images/VF/As-Is/VF_AsIs_2.png "Physical View Level 2")


### System-to-be

The **System-to-be** is the term used to describe the vision and design of a new system. It outlines the requirements, functionalities, and improvements that a future system will have, with the goal of replacing or enhancing the current one. Essentially, it's the plan for how things should be in the future, addressing the limitations of the present.

##### Level 1

![Logic View Level 1](/Documentation/Images/VL/To-Be/VL_ToBe_1.png "Logic View Level 1")

##### Level 2

![Logic View Level 2](/Documentation/Images/VL/To-Be/VL_ToBe_2.png "Logic View Level 2")

##### Level 3

![Logic View Level 3](/Documentation/Images/VL/To-Be/VL_ToBe_3.png "Logic View Level 3")


#### Process View

##### Level 1

![Process View Level 1](/Documentation/Images/VP/To-Be/VP_ToBe_1.png "Process View Level 1")

##### Level 2

![Process View Level 2](/Documentation/Images/VP/To-Be/VP_ToBe_2.png "Process View Level 2")

##### Level 3

![Process View Level 3](/Documentation/Images/VP/To-Be/VP_ToBe_3.png "Process View Level 3")

##### Level 4

![Process View Level 4](/Documentation/Images/VP/To-Be/VP_ToBe_4.png "Process View Level 4")

#### Development View

##### Level 1

##### Level 2

##### Level 3

##### Level 4

#### Physical View

##### Level 1

![Physical View Level 1](/Documentation/Images/VF/To-Be/VF_ToBe_1.png "Physical View Level 1")

##### Level 2

![Physical View Level 2](/Documentation/Images/VF/To-Be/VF_ToBe_2.png "Physical View Level 2")


---

## Funcionality 1 - Data Persistence in Multiple Data Models
### Quality Attribute Scenario

| Element | Statement |
| :--- | :--- |
| **Stimulus** | A request is made to store or retrieve data from the system, which may involve both relational (SQL) and non-relational (MongoDB, Redis) databases. |
| **Stimulus Source** | An application service or API endpoint that needs to persist or access data across multiple storage systems. |
| **Environment** | The system is operating under normal load conditions, connected to all configured databases (SQL, MongoDB, Redis), within a distributed microservices architecture. |
| **Artifact** | The data access layer and persistence management components responsible for database communication and transaction handling. |
| **Response** | The system routes the data operation to the correct storage engine (SQL, MongoDB, or Redis), executes it successfully, and returns the result while maintaining data integrity and consistency. |
| **Response Measure** | The data operation completes within &lt;200 ms for cached data (Redis) and &lt;500 ms for persistent storage (SQL/MongoDB); system maintains ACID or eventual consistency depending on the model. |

### Technical Memo

#### Problem

The current system architecture only supports a single data model for persistence, which limits flexibility and performance optimization. As the project evolves, certain data types (e.g., structured transactional data vs. semi-structured documents vs. cached session data) require different persistence strategies to meet performance and scalability requirements. The challenge is to enable seamless integration of multiple storage systems—SQL, MongoDB, and Redis—while ensuring consistency, reliability, and maintainability.

#### Summary of Solution

The proposed solution employs data management and integration tactics such as:
- Repository and abstraction layer for database independence.
- Data partitioning by data model and access pattern.
- Caching with Redis to reduce response times.
- Asynchronous communication between data services to improve scalability.
- Connection pooling and retry mechanisms for fault tolerance.
- Consistency management (ACID for SQL, eventual consistency for MongoDB/Redis).

#### Factors

- **Performance**: Different data models should optimize read/write operations according to the use case (e.g., Redis for high-speed access, SQL for transactions).
- **Scalability**: The system must handle increased data volume by scaling horizontally (MongoDB, Redis) and vertically (SQL).
- **Maintainability**: Abstraction layers and modular data access design ease future database changes.
- **Consistency and integrity**: Transactions in SQL vs. eventual consistency in MongoDB require well-defined data boundaries.
- **Security and access control**: Each DBMS requires secure connection and credential management.

#### Solution

The system will integrate three database systems:

- **SQL (e.g., PostgreSQL or MySQL)** for structured, relational data requiring ACID transactions — because it ensures strong consistency and supports complex queries.
- **MongoDB** for semi-structured or document-oriented data — because it offers flexibility for evolving schemas and easy scalability.
- **Redis** as an in-memory cache — because it significantly improves performance for frequently accessed data, reducing database load.

#### Motivation

Implementing multi-model persistence improves system performance, flexibility, and scalability. It aligns the persistence layer with real data usage patterns:
- Fast in-memory access for volatile data (Redis)
- Rigid consistency for transactional data (SQL)
- Flexible schema for documents and logs (MongoDB)

This approach directly supports project growth, allowing different services to use the most suitable database for their needs without major architectural refactoring.

#### Alternatives

1. Single-database approach (SQL only):
Easier to maintain but lacks flexibility and performance optimization for diverse data types.
2. Polyglot persistence using SQL + MongoDB only:
Good schema flexibility but missing high-speed caching layer for frequent queries.
3. Full NoSQL (MongoDB + Redis):
High scalability but loses strong transactional guarantees needed for some operations.

The selected approach (SQL + MongoDB + Redis) offers the best trade-off among consistency, scalability, and performance.

#### Pending Issues

- Data synchronization between heterogeneous systems may introduce latency or complexity.
- Backup and disaster recovery procedures need to be unified across all databases.
- Monitoring and observability must include tools compatible with multi-database environments.
- Developer training required to ensure consistent data access patterns across technologies.
- Testing strategies need to simulate real-world multi-database interactions.

---

## Functionality 2 - Retrieve Book ISBN from External APIs
### Quality Attribute Scenario

| Element | Statement |
| :--- | :--- |
| **Stimulus** | A user or service requests a book’s ISBN by providing its title **and the preferred external API**. |
| **Stimulus Source** | Application frontend or internal service triggering the lookup. |
| **Environment** | The system is connected to external APIs (Google Books, Open Library API) under normal network conditions. |
| **Artifact** | The service responsible for querying the selected external API. |
| **Response** | The system sends a request to the **specified** external API, retrieves the data, and returns the corresponding ISBN. |
| **Response Measure** | Response time $\leq$ 1.5 seconds (for the selected API); accuracy rate $\geq$ 95% (source-dependent). |

### Technical Memo

#### Problem

The system currently lacks integration with external book information sources. Users cannot obtain ISBNs by title, limiting functionality and data completeness.

#### Summary of Solution

- External service integration via REST APIs
- **User selection of the external API source**
- API abstraction layer for source independence
- Caching (Redis) for frequent titles (optionally per source)
- Timeout and retry mechanisms for reliability

#### Factors

- **Performance**: Fast response (dependent on the chosen external API)
- **Reliability**: Handle downtime of the selected API
- **Maintainability**: Easy to add or replace APIs
- **Accuracy**: Accuracy is dependent on the data source chosen by the user
- **Security**: Secure API key and connection handling
- **Flexibility**: The user has control over the data origin

#### Solution

Implement a service layer that exposes distinct endpoints or accepts a parameter allowing the user (or calling service) to **specify which external API (e.g., Google Books or Open Library Search) should be queried.**

- **API Selection**: The incoming request must indicate the desired source.
- **Routing Logic**: The service routes the request to the correct adapter/client for the selected API.
- **Caching**: Can be implemented to avoid redundant calls to the same API for the same title.
- **Abstraction Layer**: Allows for easily adding new API sources as future options.

This ensures the user receives data from the specific source they requested, giving them full control over the information's origin.

#### Motivation

To provide flexible ISBN lookups by **allowing the user to choose the data provider** they deem most reliable or appropriate, improving user control and system transparency.

#### Alternatives

1.  **Single API (no choice)** $\rightarrow$ simpler, but limiting and unreliable if the API fails.
2.  **Manual database of ISBNs** $\rightarrow$ static and hard to maintain.
3.  **Multiple APIs with parallel calls (Automatic Fallback)** $\rightarrow$ (The previous solution) More robust and faster for the end-user, but more complex to implement and offers no control over the source.
4.  **Multiple APIs with sequential calls (Automatic Fallback)** $\rightarrow$ reliable, but slower than parallel.

#### Pending Issues

- Error handling (what happens if the *chosen* API fails?)
- API rate limits and quota management (now per selected API)
- Monitoring the availability of each external API
- Logging and alerting for external failures
- Secure key rotation and storage

---

## Funcionality 3 - Generate IDs in Multiple Formats
### Quality Attribute Scenario

| Element | Statement |
| :--- | :--- |
| **Stimulus** | A service or process requests a unique ID for a specific entity type (e.g., book, user, transaction). |
| **Stimulus Source** | Internal application service or external system needing ID generation. |
| **Environment** | The system is operational and must generate IDs according to predefined format specifications (e.g., numeric, alphanumeric, UUID). |
| **Artifact** | The ID generation module or service responsible for producing and validating IDs. |
| **Response** | The system generates a valid, unique ID in the required format and returns it to the requesting component. |
| **Response Measure** | ID generated in ≤ 50 ms; zero duplication; compliance rate with format specification ≥ 100%. |

### Technical Memo

#### Problem

The system currently lacks a unified and configurable mechanism for generating unique IDs. Different components require IDs in specific formats, making manual or ad hoc generation error-prone and inconsistent.

#### Summary of Solution

- Centralized ID generation service
- Configurable format templates
- Validation rules per entity type
- UUID, sequential, and pattern-based strategies
- Caching and atomic counters for performance and uniqueness

#### Factors

- **Performance**: Fast ID generation under high request volume
- **Uniqueness**: No collisions across distributed components
- **Configurability**: Flexible format definitions
- **Maintainability**: Easy to add new ID types or formats
- **Scalability**: Support for distributed generation


#### Solution

Implement a dedicated ID generation service with a configuration-driven engine.

- **Centralization** : ensures consistency and avoids duplication.
- **Configurable formats** : supports varying specifications per entity.
- **Atomic counters / UUIDs** : guarantee uniqueness.
- **Caching** : speeds up generation for frequent requests.

This design simplifies maintenance and ensures IDs meet all required specifications efficiently.

#### Motivation

Provide consistent, unique, and format-compliant identifiers across the system, improving data integrity and interoperability between components.

#### Alternatives

1. Local ID generation in each module → risk of collisions, harder to maintain.
2. Database auto-increment → simple but inflexible and hard to scale.
3. External ID service → scalable but adds dependency and latency.

#### Pending Issues

- Defining and storing ID format specifications
- Ensuring thread-safe and distributed uniqueness
- Managing concurrency and cache invalidation
- Monitoring ID generation rate and collisions
- Documenting configuration changes for future entities

---

## Mutation Tests Reports

### First moment without new tests

![First moment without new tests](/Documentation/Images/MutationTestsFirstReport.png "First moment without new tests")

### Before new tests

![Before new tests](/Documentation/Images/ "Before new tests")