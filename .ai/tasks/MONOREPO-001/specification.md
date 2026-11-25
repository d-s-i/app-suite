# MONOREPO-001 — Spécification à soumettre au propriétaire

Statut : ready_for_review. Ce document n'autorise ni implémentation, ni commit/push, ni publication. La présente phase produit uniquement des artefacts de spécification.

## Sources et exigences

- R1 — Demande initiale : monorepo permettant plusieurs applications et réutilisation du design.
- R2 — Décision humaine : nom `app-suite`, nouveau dépôt `d-s-i/app-suite`, ancien `calendar` conservé.
- R3 — Décision humaine : Calendar doit être publié dans ce cadre ; pas seulement documenter une publication future.
- R4 — Décision humaine : le propriétaire vérifie lui-même le bon APK dans la release. Pas de QA agent de cette vérification, pas d'app factice imposée.
- C1 — Contraintes AGENTS.md : préserver comportement, lisibilité, identité et signatures Calendar, fonctionnement hors ligne, travaux existants ; pas de secrets publiés, dépendance ajoutée sans accord, commit/push par l'agent ou effacement d'installation.

Les préférences suggérées précédemment par l'assistant ne sont pas des exigences supplémentaires : pas de nouvelle palette, refonte, architecture, CI, framework ou catalogue de composants imposés.

## Découpage séquentiel

1. MONOREPO-001-F01 — Conserver Calendar et ses vérifications dans app-suite.
2. MONOREPO-001-F02 — Rendre le design existant partageable et son usage documenté pour une future application.
3. MONOREPO-001-F03 — Publier le code et une release Calendar dans le nouveau dépôt ; contrôle APK par le propriétaire.

Aucune arborescence, nom de module, classe interne, mécanisme d'exposition du design ou solution de CI n'est imposé. La structure relève de l'implémentation et de la revue d'architecture.

## Critères d'acceptation et traçabilité

| Source | Critère | Feature / scénario | Checklist QA |
|---|---|---|---|
| R1, C1 | AC-MONOREPO-01 : les commandes documentées de test unitaire, générateur, acceptance UI, lint, build debug/release et contrôle d'APK fonctionnent depuis app-suite ; APK Calendar identifiable et signature existante conservée | F01 / MONOREPO-001-S01 | QA-MONOREPO-001-S01 |
| R1, C1 | AC-MONOREPO-02 : mise à jour debug en place et navigation Month/Day/Week hors ligne sans seconde installation ni effacement | F01 / MONOREPO-001-S02 | QA-MONOREPO-001-S02 |
| C1 | AC-MONOREPO-03 : une invocation sans secrets release échoue explicitement, sans création de clé ni repli debug | F01 / MONOREPO-001-S03 | QA-MONOREPO-001-S03 |
| R1 | AC-MONOREPO-04 : le guide de design mène aux définitions partagées versionnées, décrit leur consommation sans copie et distingue présentation partagée et comportements Calendar | F02 / MONOREPO-001-S04 | QA-MONOREPO-001-S04 |
| R1 | AC-MONOREPO-05 : le guide d'ajout d'application couvre identité indépendante, usage du design, construction/tests, APK, données, permissions, versions, signatures et contrôles après modification commune | F02 / MONOREPO-001-S05 | QA-MONOREPO-001-S05 |
| R2, C1 | AC-MONOREPO-06 : le code migré et son historique sont publiés dans le nouveau dépôt, tâches conservées et sans secrets ; calendar n'est ni supprimé ni renommé | F03 / MONOREPO-001-S06 | QA-MONOREPO-001-S06 |
| R3, R4, C1 | AC-MONOREPO-07 : release Calendar publiée dans app-suite avec l'APK signé vérifié et versionné pour mise à jour ; URL et contrôle du propriétaire consignés | F03 / MONOREPO-001-S07 | QA-MONOREPO-001-S07 — propriétaire uniquement |

## Limites de la preuve

- Les vérifications existantes de CAL-001 restent la référence pour les détails visuels, horaires, densités fractionnaires et polices agrandies. S01 les réutilise, sans recopier les scénarios ni transformer leur réussite en preuve de QA GrapheneOS.
- La factorisation du design est un changement structurel : la revue du code doit vérifier que Calendar utilise réellement les éléments partagés, sans copie concurrente. S04 contrôle l'interface développeur documentée et ses références, pas une intégration réelle dans deux apps. On n'invente pas une deuxième app pour convertir cette revue en test produit.
- Conservation des travaux en cours, sources des tâches et historique : revue de migration et vérification de publication S06. Aucun test ne doit détruire les travaux pour établir une fixture.
- La réussite des tests UI de développement ne vaut pas exécution des anciennes procédures QA externes, qui ne sont pas imposées intégralement par cette migration.
- Aucun nouveau runner, handler, script QA, benchmark ou campagne de test multi-apps n'est demandé. QA signifie ici checklist de livraison, avec réutilisation des commandes publiques existantes.
- Le contrôle du bon APK sur GitHub appartient exclusivement au propriétaire. L'agent peut produire l'APK et sa vérification locale de signature, mais ne marque pas S07 PASS sans retour explicite du propriétaire.

## Hypothèses non bloquantes

- Futures applications Android Kotlin/Compose ; seule Calendar est migrée.
- Le partage porte sur les éléments existants réellement réutilisables ; ni composants futurs spéculatifs ni partage de logique métier n'est requis.
- Première publication issue de cette tâche dans app-suite, sans double publication obligatoire dans calendar. La distribution de futures applications et son organisation Obtainium seront décidées ultérieurement.
- Le projet ne change pas de design, de permissions, d'identité Android ou de clé en raison de sa migration.
- Le nom app-suite ne requiert pas de déplacer immédiatement le chemin physique du workspace ouvert ; l'identité du projet et du dépôt doit être mise à jour. Un déplacement du répertoire local, si souhaité, sera coordonné sans perdre la session/travail.

## Questions ouvertes et approbations

Aucune question d'acceptation bloquante restante. Avant exécution : propriétaire approuve cette spécification et l'implémentation, fixe la visibilité du nouveau dépôt et le tag de release, fournit/valide les droits nécessaires et effectue les commits/pushes. La versionCode est choisie à partir des versions réellement destinées à être remplacées, pas de la seule version observée dans le checkout.

Conserver l'ancien dépôt ne signifie pas l'archiver, modifier son contenu ou le maintenir en parallèle. Ces actions ne sont pas demandées. Les remotes et les chemins de build exacts seront documentés après le choix d'organisation ; les commandes ci-dessous ne sont pas inventées à l'avance.

## Résultats à produire ultérieurement

- Résultats des commandes et des contrôles de livraison, sans simulation de succès.
- Diff/revue de factorisation, préservation des tâches et absence de secrets.
- Installation et lancement sur l'émulateur propriétaire `android-dev` suivant AGENTS.md.
- URL du nouveau dépôt, URL de release et contrôle APK par le propriétaire. Tant que publication ou contrôle humain manque, ne pas annoncer la livraison complète.
