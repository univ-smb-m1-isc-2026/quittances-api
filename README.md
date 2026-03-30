# Quittances API

Base URL locale :

```text
http://localhost:8080
```

## Routes disponibles

### Acces sante

| Methode | Route       | Description    |
|---------|-------------|----------------|
| GET     | [/api/health](#route-get-api-health) | Sante de l'API |

### Acces table `proprietaires`

| Methode | Route              | Description               |
|---------|--------------------|---------------------------|
| GET     | [/api/proprios](#route-get-api-proprios)      | Lister les proprietaires  |
| POST    | [/api/proprios](#route-post-api-proprios)      | Creer un proprietaire     |
| DELETE  | [/api/proprios/{id}](#route-delete-api-proprios-id) | Supprimer un proprietaire |

### Acces table `locataires`

| Methode | Route                | Description             |
|---------|----------------------|-------------------------|
| GET     | [/api/locataires](#route-get-api-locataires)      | Lister les locataires   |
| POST    | [/api/locataires](#route-post-api-locataires)      | Creer un locataire      |
| DELETE  | [/api/locataires/{id}](#route-delete-api-locataires-id) | Supprimer un locataire  |

<a id="route-get-api-health"></a>
## Sante de l'API

- Methode : GET
- Route : /api/health

Exemple :

```bash
curl -i http://localhost:8080/api/health
```

## Proprietaires

<a id="route-get-api-proprios"></a>
### Lister les proprietaires

- Methode : GET
- Route : /api/proprios

```bash
curl -i http://localhost:8080/api/proprios
```

<a id="route-post-api-proprios"></a>
### Creer un proprietaire

- Methode : POST
- Route : /api/proprios
- Content-Type : application/json

Schema des entrees (`POST /api/proprios`) - table `proprietaires` :

| Nom       | Type Java | Nullable | Unique |
|-----------|-----------|----------|--------|
| nom       | String    | Non      | Non    |
| prenom    | String    | Non      | Non    |
| email     | String    | Non      | Oui    |
| telephone | String    | Non      | Oui    |
| password  | String    | Non      | Oui    |

Note : le champ `id` est genere automatiquement et ne doit pas etre fourni dans le payload.

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

<a id="route-delete-api-proprios-id"></a>
### Supprimer un proprietaire

- Methode : DELETE
- Route : /api/proprios/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/proprios/1
```

## Locataires

<a id="route-get-api-locataires"></a>
### Lister les locataires

- Methode : GET
- Route : /api/locataires

```bash
curl -i http://localhost:8080/api/locataires
```

<a id="route-post-api-locataires"></a>
### Creer un locataire

- Methode : POST
- Route : /api/locataires
- Content-Type : application/json

Schema des entrees (`POST /api/locataires`) - table `locataires` :

| Nom       | Type Java | Nullable | Unique |
|-----------|-----------|----------|--------|
| nom       | String    | Non      | Non    |
| prenom    | String    | Non      | Non    |
| email     | String    | Non      | Oui    |
| telephone | String    | Non      | Oui    |

Note : le champ `id` est genere automatiquement et ne doit pas etre fourni dans le payload.

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

<a id="route-delete-api-locataires-id"></a>
### Supprimer un locataire

- Methode : DELETE
- Route : /api/locataires/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/locataires/1
```

## Notes

- Les champs `email` et `telephone` sont uniques.
- Si un id n'existe pas lors d'une suppression, Spring peut retourner une erreur selon le comportement JPA.
