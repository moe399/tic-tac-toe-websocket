Backend for tic-tac-toe websocket app

Left to do:
- Code polishing
- Testing


Upcoming features

- Guest players
- Invite using mobile number/email - return link that allows user to join as guest or make an account, same logic with redis pub/sub for creator would apply.
- Barcode creation upon game completion returning game details eg: winner, loser, totals wins etc.
  


A tic-tac-toe application built using a microservice architecture with Spring Boot. The system utilises Redis Pub/Sub for matchmaking and client notifications—when players want to join a game, their requests are published to a Redis channel, and once matched,  the creator of the game receives a notification that someone joined their game and returns a url. Clients then connect to the game service, which handles the game logic and facilitates real-time communication via WebSockets, allowing for interactive gameplay between two users. Authentication is managed by a dedicated user service, and an API Gateway routes requests to the appropriate services, the authentication is done with cookies and the api gateway utilises this through a filter when the auth service (user service) creates sessions once a user logs in. OpenFeign is used for inter-service communication.
