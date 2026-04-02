# Quittances API

Base URL locale :

```text
http://localhost:8080
```

## Restriction d'acces API (CORS + Origin)

L'API peut etre protegee pour n'accepter que les appels venant du frontend.

Regles appliquees :

- CORS autorise uniquement les origines de `app.security.allowed-origins`.
- Le filtre `FrontendOriginFilter` peut exiger un en-tete `Origin` valide sur `/api/**`.
- Exception : `/api/health` reste accessible (pas de blocage Origin).

Configuration disponible :

- `app.security.allowed-origins` : liste des origines frontend autorisees.
- `app.security.enforce-origin-header` :
	- `true` : bloque les appels API sans `Origin` valide.
	- `false` : mode debug local, appels API acceptes sans `Origin`.

Priorite de configuration :

- Les variables d'environnement Docker (`APP_SECURITY_*`) ecrasent les valeurs du `application.yaml`.

Exemples Docker Compose :

```yaml
environment:
	- APP_SECURITY_ALLOWED_ORIGINS=http://localhost:5173,https://quittances.oups.net
	- APP_SECURITY_ENFORCE_ORIGIN_HEADER=true
```

Modes d'utilisation :

- Debug local sans frontend :
	- `APP_SECURITY_ENFORCE_ORIGIN_HEADER=false`
	- ou `app.security.enforce-origin-header: false`

- Mode protege (frontend uniquement) :
	- `APP_SECURITY_ENFORCE_ORIGIN_HEADER=true`

Tests rapides :

```bash
# health (toujours accessible)
curl -i http://localhost:8080/api/health

# route API en mode protege (Origin requis)
curl -i http://localhost:8080/api/proprios \
	-H "Origin: http://localhost:5173"
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
| PUT     | [/api/proprios/{id}](#route-put-api-proprios-id) | Modifier un proprietaire |
| POST    | [/api/proprios/login](#route-post-api-proprios-login) | Connexion proprietaire |
| DELETE  | [/api/proprios/{id}](#route-delete-api-proprios-id) | Supprimer un proprietaire |

### Acces table `admins`

| Methode | Route              | Description               |
|---------|--------------------|---------------------------|
| GET     | [/api/admins](#route-get-api-admins)      | Lister les admins  |
| POST    | [/api/admins](#route-post-api-admins)      | Creer un admin     |
| PUT     | [/api/admins/{id}](#route-put-api-admins-id) | Modifier un admin |
| DELETE  | [/api/admins/{id}](#route-delete-api-admins-id) | Supprimer un admin |

### Acces table `locataires`

| Methode | Route                | Description             |
|---------|----------------------|-------------------------|
| GET     | [/api/locataires](#route-get-api-locataires)      | Lister les locataires   |
| POST    | [/api/locataires](#route-post-api-locataires)      | Creer un locataire      |
| PUT     | [/api/locataires/{id}](#route-put-api-locataires-id) | Modifier un locataire |
| DELETE  | [/api/locataires/{id}](#route-delete-api-locataires-id) | Supprimer un locataire  |

### Acces table `proprietes`

| Methode | Route                | Description             |
|---------|----------------------|-------------------------|
| GET     | [/api/proprietes](#route-get-api-proprietes)      | Lister les proprietes   |
| GET     | [/api/proprietes/{proprio_id}](#route-get-api-proprietes-id-proprios) | Lister les proprietes d'un proprietaire |
| POST    | [/api/proprietes](#route-post-api-proprietes)      | Creer une propriete     |
| PUT     | [/api/proprietes/{id}](#route-put-api-proprietes-id) | Modifier une propriete |
| DELETE  | [/api/proprietes/{id}](#route-delete-api-proprietes-id) | Supprimer une propriete |

### Acces table `quittances`

| Methode | Route                | Description             |
|---------|----------------------|-------------------------|
| GET     | [/api/quittances](#route-get-api-quittances)      | Lister les quittances   |
| GET     | [/api/quittances/{proprio_id}](#route-get-api-quittances-proprio-id) | Lister les quittances d'un proprietaire |
| POST    | [/api/quittances](#route-post-api-quittances)      | Creer une quittance     |
| PUT     | [/api/quittances/{id}](#route-put-api-quittances-id) | Modifier une quittance |
| DELETE  | [/api/quittances/{id}](#route-delete-api-quittances-id) | Supprimer une quittance |

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

<a id="route-put-api-proprios-id"></a>
### Modifier un proprietaire

- Methode : PUT
- Route : /api/proprios/{id}
- Content-Type : application/json

Payload partiel accepte (exemple) :

```json
{
	"nom": "Dupont",
	"prenom": "Jean",
	"telephone": "0611111111"
}
```

Exemple curl :

```bash
curl -i -X PUT http://localhost:8080/api/proprios/1 \
	-H "Content-Type: application/json" \
	-d '{
		"nom": "Dupont",
		"prenom": "Jean",
		"telephone": "0611111111"
	}'
```

<a id="route-post-api-proprios-login"></a>
### Connexion proprietaire

- Methode : POST
- Route : /api/proprios/login
- Content-Type : application/json

Schema des entrees (`POST /api/proprios/login`) :

| Nom      | Type Java | Nullable |
|----------|-----------|----------|
| email    | String    | Non      |
| password | String    | Non      |

Payload attendu :

```json
{
	"email": "jean.dupont@example.com",
	"password": "secret123"
}
```

Reponse en cas de succes :

```json
{
	"token": "<jwt>"
}
```

Exemple curl :

```bash
curl -i -X POST http://localhost:8080/api/proprios/login \
	-H "Content-Type: application/json" \
	-d '{
		"email": "jean.dupont@example.com",
		"password": "secret123"
	}'
```

## Admins

<a id="route-get-api-admins"></a>
### Lister les admins

- Methode : GET
- Route : /api/admins

```bash
curl -i http://localhost:8080/api/admins
```

<a id="route-post-api-admins"></a>
### Creer un admin

- Methode : POST
- Route : /api/admins
- Content-Type : application/json

Schema des entrees (`POST /api/admins`) - table `admins` :

| Nom      | Type Java | Nullable | Unique |
|----------|-----------|----------|--------|
| login    | String    | Non      | Oui    |
| password | String    | Non      | Non    |

Notes :
- Le champ `id` est genere automatiquement.
- Le champ `password` est hache par le backend.

Payload attendu :

```json
{
	"login": "admin",
	"password": "secret123"
}
```

Exemple curl :

```bash
curl -i -X POST http://localhost:8080/api/admins \
	-H "Content-Type: application/json" \
	-d '{
		"login": "admin",
		"password": "secret123"
	}'
```

<a id="route-put-api-admins-id"></a>
### Modifier un admin

- Methode : PUT
- Route : /api/admins/{id}
- Content-Type : application/json

Payload partiel accepte (exemple) :

```json
{
	"login": "admin2",
	"password": "newSecret123"
}
```

Exemple curl :

```bash
curl -i -X PUT http://localhost:8080/api/admins/1 \
	-H "Content-Type: application/json" \
	-d '{
		"login": "admin2",
		"password": "newSecret123"
	}'
```

<a id="route-delete-api-admins-id"></a>
### Supprimer un admin

- Methode : DELETE
- Route : /api/admins/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/admins/1
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

<a id="route-put-api-locataires-id"></a>
### Modifier un locataire

- Methode : PUT
- Route : /api/locataires/{id}
- Content-Type : application/json

Payload partiel accepte (exemple) :

```json
{
	"nom": "Martin",
	"prenom": "Alice"
}
```

Exemple curl :

```bash
curl -i -X PUT http://localhost:8080/api/locataires/1 \
	-H "Content-Type: application/json" \
	-d '{
		"nom": "Martin",
		"prenom": "Alice"
	}'
```

## Proprietes

<a id="route-get-api-proprietes"></a>
### Lister les proprietes

- Methode : GET
- Route : /api/proprietes

```bash
curl -i http://localhost:8080/api/proprietes
```

<a id="route-get-api-proprietes-id-proprios"></a>
### Lister les proprietes d'un proprietaire

- Methode : GET
- Route : /api/proprietes/{proprio_id}

```bash
curl -i http://localhost:8080/api/proprietes/1
```

<a id="route-post-api-proprietes"></a>
### Creer une propriete

- Methode : POST
- Route : /api/proprietes
- Content-Type : application/json

Schema des entrees (`POST /api/proprietes`) - table `proprietes` :

| Nom                 | Type Java | Nullable | Unique |
|---------------------|-----------|----------|--------|
| adresse             | String    | Non      | Non    |
| ville               | String    | Non      | Non    |
| pays                | String    | Non      | Non    |
| surfaceM2           | Double    | Non      | Non    |
| type                | String    | Non      | Non    |
| loyer               | Double    | Non      | Non    |
| charges             | Double    | Non      | Non    |
| idProprio           | Long      | Non      | Non    |
| idLocataire         | Long      | Non      | Non    |
| dureeBail           | Integer   | Non      | Non    |
| periodicite         | Integer   | Oui      | Non    |
| infosComplementaires| String(TEXT) | Oui   | Non    |
| image               | String    | Oui      | Non    |

Notes :
- Le champ `id` est genere automatiquement et ne doit pas etre fourni dans le payload.
- `idProprio` est une cle etrangere vers `proprietaires.id`.
- `idLocataire` est une cle etrangere vers `locataires.id`.
- Le champ `type` est normalise en majuscules et doit etre une des valeurs autorisees :
	- `STUDIO`
	- `T1`, `T2`, `T3`, `T4`, `T5`
	- `DUPLEX`, `TRIPLEX`, `SOUPLEX`
	- `LOFT`

Erreurs de validation possibles (HTTP 400) :
- `type invalide: ...`
- `idProprio invalide: proprietaire introuvable`
- `idLocataire invalide: locataire introuvable`

Payload attendu :

```json
{
	"adresse": "12 rue Victor Hugo, Paris",
	"ville": "Paris",
	"pays": "France",
	"surfaceM2": 54.5,
	"type": "T2",
	"loyer": 1250.0,
	"charges": 120.0,
	"idProprio": 1,
	"idLocataire": 1,
	"dureeBail": 36,
	"periodicite": 1,
	"infosComplementaires": "Appartement renove, 3eme etage, proche metro.",
	"image": "https://example.com/images/propriete-1.jpg"
}
```

Exemple curl :

```bash
curl -i -X POST http://localhost:8080/api/proprietes \
	-H "Content-Type: application/json" \
	-d '{
		"adresse": "12 rue Victor Hugo",
		"ville": "Paris",
		"pays": "France",
		"surfaceM2": 54.5,
		"type": "DUPLEX",
		"loyer": 1250.0,
		"charges": 120.0,
		"idProprio": 1,
		"idLocataire": 1,
		"dureeBail": 36,
		"periodicite": 1,
		"infosComplementaires": "Appartement renove, 3eme etage, proche metro.",
		"image": "https://example.com/images/propriete-1.jpg"
	}'
```

<a id="route-delete-api-proprietes-id"></a>
### Supprimer une propriete

- Methode : DELETE
- Route : /api/proprietes/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/proprietes/1
```

<a id="route-put-api-proprietes-id"></a>
### Modifier une propriete

- Methode : PUT
- Route : /api/proprietes/{id}
- Content-Type : application/json

Payload partiel accepte (exemple) :

```json
{
	"adresse": "14 rue Victor Hugo",
	"ville": "Paris",
	"loyer": 1300.0,
	"charges": 140.0
}
```

Exemple curl :

```bash
curl -i -X PUT http://localhost:8080/api/proprietes/1 \
	-H "Content-Type: application/json" \
	-d '{
		"adresse": "14 rue Victor Hugo",
		"ville": "Paris",
		"loyer": 1300.0,
		"charges": 140.0
	}'
```

## Quittances

<a id="route-get-api-quittances"></a>
### Lister les quittances

- Methode : GET
- Route : /api/quittances

```bash
curl -i http://localhost:8080/api/quittances
```

<a id="route-get-api-quittances-proprio-id"></a>
### Lister les quittances d'un proprietaire

- Methode : GET
- Route : /api/quittances/{proprio_id}

```bash
curl -i http://localhost:8080/api/quittances/1
```

<a id="route-post-api-quittances"></a>
### Creer une quittance

- Methode : POST
- Route : /api/quittances
- Content-Type : application/json

Schema des entrees (`POST /api/quittances`) - table `quittances` :

| Nom             | Type Java | Nullable |
|-----------------|-----------|----------|
| proprio          | Object    | Oui      |
| locataire          | Object    | Oui      |
| propriete       | Object    | Oui      |
| period          | String    | Oui      |
| paymentDate     | String    | Oui      |
| signatureCity   | String    | Oui      |
| signatureImage  | String    | Oui      |

Notes :
- Le champ `id` est genere automatiquement.
- L'objet `proprio` requiert au minimum son `id` (ex: `{"id": 1}`).
- L'objet `locataire` requiert au minimum son `id` (ex: `{"id": 1}`).
- L'objet `propriete` requiert au minimum son `id` (ex: `{"id": 1}`).

Payload attendu :

```json
{
        "proprio": {
                "id": 1
        },
        "locataire": {
                "id": 1
        },
        "propriete": {
                "id": 1
        },
        "period": "février 2026",
        "paymentDate": "01/02/2026",
        "signatureCity": "Paris",
        "signatureImage": "data:image/png;base64,iVBORw0K..."
}
```

Exemple curl :

```bash
curl -i -X POST http://localhost:8080/api/quittances \
        -H "Content-Type: application/json" \
        -d '{
        "proprio": {
                "id": 1
        },
        "locataire": {
                "id": 1
        },
        "propriete": {
                "id": 1
        },
        "period": "février 2026",
        "paymentDate": "01/02/2026",
        "signatureCity": "Paris",
        "signatureImage": "data:image/png;base64,iVBORw0K..."
}'
```

<a id="route-delete-api-quittances-id"></a>
### Supprimer une quittance

- Methode : DELETE
- Route : /api/quittances/{id}

```bash
curl -i -X DELETE http://localhost:8080/api/quittances/1
```

<a id="route-put-api-quittances-id"></a>
### Modifier une quittance

- Methode : PUT
- Route : /api/quittances/{id}
- Content-Type : application/json

Payload partiel accepte (exemple) :

```json
{
	"period": "mars 2026",
	"paymentDate": "01/03/2026",
	"signatureCity": "Paris"
}
```

Exemple curl :

```bash
curl -i -X PUT http://localhost:8080/api/quittances/1 \
	-H "Content-Type: application/json" \
	-d '{
		"period": "mars 2026",
		"paymentDate": "01/03/2026",
		"signatureCity": "Paris"
	}'
```

## Notes

- Les champs `email` et `telephone` sont uniques.
- Si un id n'existe pas lors d'une suppression, Spring peut retourner une erreur selon le comportement JPA.
