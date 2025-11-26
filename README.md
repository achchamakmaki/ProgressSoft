# ProgressSoft – FX Deals Importer
**Java 17 + Spring Boot 3 + PostgreSQL + Docker**

Solution 100 % fonctionnelle et prête à être livrée pour le test technique Java Developer.

## Fonctionnalités implémentées
- Validation complète des champs
- Code devise ISO 4217 (exactement 3 lettres majuscules)
- `dealUniqueId` obligatoire et unique en base
- Timestamp au format ISO instant
- Montant strictement positif
- **Idempotence totale** → même `dealUniqueId` = rejeté avec message clair
- **No rollback** → les lignes valides sont toujours sauvegardées
- Retour détaillé des erreurs par ligne
- Logging complet
- PostgreSQL + migrations Flyway
- Docker Compose + build multi-stage (zéro configuration)

## Lancement (1 seule commande)
```bash
make run
# ou
docker compose up --build# ProgressSoft
