# Anime-Advisor

Anime Advisor is a Java-based comprehensive application that allows users to explore a wide variety of anime, which refers to Japan's animated products, including numerous types and genres.
The application provides detailed information about each anime, including its name, type, source, release year, genres, studio, producers, and licensors. Users can search for anime by name or genre, leave reviews, follow their favorite anime, and interact with other users in a social network-like environment.

---

## Features

- **Anime Search**: Users can search for anime by name or browse by genre.
- **Advanced Search**: Offers statistical research options, such as top 10 anime by studio, producer, or genre, based on score or number of reviews.
- **User Accounts**:
  - **Unregistered Users**: Can browse anime and create an account.
  - **Registered Users**: Can vote, leave reviews, follow anime, and follow other users.
  - **Admin Users**: Can manage anime data and user accounts, including adding, updating, or deleting anime and users.
- **Social Features**:
  - Follow anime and other users.
  - View profiles of followed users, their reviews, and the anime they follow.
  - Receive suggestions for anime and users to follow based on preferences.
- **Data Consistency**: Ensures that all users see the latest version of the data.

---

## Technologies Used

- **Java**: Main programming language for the application.
- **MongoDB**: Used to store and manage anime data. MongoDB's document-based structure allows for efficient data retrieval and aggregation queries.
- **Neo4J**: Used to manage social relationships between users and anime. Neo4J's graph database structure is optimized for complex queries, such as counting followers and suggesting anime or users.
- **Replication and Sharding**: MongoDB replicas ensure data availability and consistency, while sharding distributes data across multiple servers for better performance.

---

## Database Structure

### MongoDB
- Stores anime data in JSON documents.
- Fields include:
  - `name`: Name of the anime.
  - `genre`: List of genres.
  - `premiered`: Year of release.
  - `episodes`: Number of episodes.
  - `source`: Source material (e.g., manga, novel, original).
  - `type`: Type of anime (e.g., TV series, movie, OVA).
  - `studio`: List of studios.
  - `producer`: List of producers.
  - `licensor`: List of licensors.
  - `scored`: Total score based on user votes.
  - `scoredBy`: Number of users who voted.

### Neo4J
- Stores user data, reviews, and social relationships.
- Nodes:
  - **User**: Represents a registered user with attributes like username, password, gender, and admin status.
  - **Anime**: Represents an anime with its title.
  - **Review**: Represents a user's review with a title, text, and last update date.
- Relationships:
  - **FOLLOWS**: User follows another user.
  - **LIKE**: User likes an anime.
  - **WRITE**: User writes a review.
  - **REFERRED_TO**: Review refers to an anime.

---

## Key Queries

### MongoDB
- **Top 10 Anime by Field**: Retrieve the top 10 anime based on score, year, genre, or type.
- **Top 10 Studios/Producers by Average Score**: Find the top studios or producers with the highest average score for a given year.
- **Number of Productions by Entity**: Count the number of productions by studio or producer.

### Neo4J
- **Count Followers/Likes**: Count the number of followers a user has or the number of likes an anime has.
- **Suggested Anime/Users**: Suggest anime or users to follow based on the user's social network.
- **Top 10 Most Reviewed Anime**: Retrieve the top 10 anime with the most reviews.
- **Top 10 Most Active Users**: Retrieve the top 10 users who have written the most reviews.

---

## Installation and Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher.
- MongoDB installed and running.
- Neo4J installed and running.
