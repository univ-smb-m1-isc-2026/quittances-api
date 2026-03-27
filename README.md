# Quittances API

API backend Spring Boot pour la gestion des proprietaires et locataires.

## Demarrage

### Option 1 : avec Docker Compose (depuis la racine du projet)

```bash
docker compose up -d --build
```

### Option 2 : en local (depuis quittances-api)

```bash
./mvnw spring-boot:run
```

Base URL locale :

```text
http://localhost:8080
```

## Routes disponibles

### Sante de l'API

- Methode : GET
- Route : /api/health

Exemple :

```bash
curl -i http://localhost:8080/api/health
```

### Proprietaires

#### Lister les proprietaires

- Methode : GET
- Route : /api/proprios

```bash
curl -i http://localhost:8080/api/proprios
```

#### Creer un proprietaire

- Methode : POST
- Route : /api/proprios
- Content-Type : application/json

Payload attendu :

```json
{
	"nom": "Dupont",
	"prenom": "Jean",
	"email": "jean.dupont@example.com",
	"telephone": "0612345678",
	"password": "secret123"
}
```

Exemple curl :

```bash
curl -i -X POST http://localhost:8080/api/proprios \
	-H "Content-Type: application/json" \
	-d '{
		"nom": "Dupont",
		"prenom": "Jean",
		"email": "jean.dupont@example.com",
		"telephone": "0612345678",
		"password": "secret123"
	}'
```

#### Supprimer un proprietaire

- Methode : DELETE
- Route : /api/proprios/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/proprios/1
```

### Locataires

#### Lister les locataires

- Methode : GET
- Route : /api/locataires

```bash
curl -i http://localhost:8080/api/locataires
```

#### Creer un locataire

- Methode : POST
- Route : /api/locataires
- Content-Type : application/json

Payload attendu :

```json
{
	"nom": "Martin",
	"prenom": "Alice",
	"email": "alice.martin@example.com",
	"telephone": "0698765432"
}
```

Exemple curl :

```bash
curl -i -X POST http://localhost:8080/api/locataires \
	-H "Content-Type: application/json" \
	-d '{
		"nom": "Martin",
		"prenom": "Alice",
		"email": "alice.martin@example.com",
		"telephone": "0698765432"
	}'
```

#### Supprimer un locataire

- Methode : DELETE
- Route : /api/locataires/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/locataires/1
```

## Notes

- Les champs `email` et `telephone` sont uniques.
- Si un id n'existe pas lors d'une suppression, Spring peut retourner une erreur selon le comportement JPA.
