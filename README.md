## Endpoints REST - Swagger

Visualizar la documentación de los endpoints en Swagger:

👉 [Abrir Swagger UI](http://localhost:8080/swagger-ui/index.html#/)
## Compilación y ejecución
```bash
docker build -t dux-challenge .
```
```bash
docker run -d -p 8080:8080 dux-challenge
```
Esto inicia el servidor en el puerto 8080
# Datos pre-cargados
## 1. Usuarios

| user_id | username | password |
|---------|----------|----------|
| 1       | test     | 12345    |

## 2. Equipos

| team_id | nombre                    | pais         | liga                 |
|---------|---------------------------|--------------|----------------------|
| 1       | Real Madrid               | España       | La Liga              |
| 2       | FC Barcelona              | España       | La Liga              |
| 3       | Manchester United         | Inglaterra   | Premier League       |
| 4       | Liverpool FC              | Inglaterra   | Premier League       |
| 5       | Juventus FC               | Italia       | Serie A              |
| 6       | AC Milan                  | Italia       | Serie A              |
| 7       | Bayern Munich             | Alemania     | Bundesliga           |
| 8       | Borussia Dortmund         | Alemania     | Bundesliga           |
| 9       | Paris Saint-Germain       | Francia      | Ligue 1              |
| 10      | Olympique de Marseille    | Francia      | Ligue 1              |
| 11      | FC Porto                  | Portugal     | Primeira Liga        |
| 12      | Sporting CP               | Portugal     | Primeira Liga        |
| 13      | Ajax Amsterdam            | Países Bajos | Eredivisie           |
| 14      | Feyenoord                 | Países Bajos | Eredivisie           |
| 15      | Celtic FC                 | Escocia      | Scottish Premiership |
| 16      | Rangers FC                | Escocia      | Scottish Premiership |
| 17      | Galatasaray SK            | Turquía      | Süper Lig            |
| 18      | Fenerbahçe SK             | Turquía      | Süper Lig            |
| 19      | FC Zenit Saint Petersburg | Rusia        | Premier League Rusa  |
| 20      | Spartak Moscow            | Rusia        | Premier League Rusa  |
| 21      | SL Benfica                | Portugal     | Primeira Liga        |
| 22      | Besiktas JK               | Turquía      | Süper Lig            |
| 23      | SSC Napoli                | Italia       | Serie A              |
| 24      | Atlético Madrid           | España       | La Liga              |