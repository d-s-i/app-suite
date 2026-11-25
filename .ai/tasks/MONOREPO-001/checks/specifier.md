# MONOREPO-001 — Contrôles de spécification

## Commandes exécutées

Pour chacun des noms `calendar-migration`, `reusable-design`, `github-publication` :

```sh
parse-gherkin .ai/tasks/MONOREPO-001/features/<nom>.feature .ai/tasks/MONOREPO-001/acceptance/<nom>.json
check-gherkin-dry .ai/tasks/MONOREPO-001/acceptance/<nom>.json .ai/tasks/MONOREPO-001/checks/<nom>-gherkin-dry.json
```

Six commandes exécutées, toutes avec code 0. Trois features, sept scénarios, aucun Scenario Outline ni paramètre Examples artificiel. IR généré par l'outil existant, sans modification manuelle.

## Revue des avis DRY Gherkin

- F01 : aucun avis.
- F02 : un `possible-synonym` entre l'ouverture du guide de design et celle du guide d'ajout d'app. Conservé : destinations et résultats documentaires distincts, même si les guides peuvent être des sections d'un même document. Ne pas fusionner les comportements sur une simple similarité lexicale.
- F03 : un `possible-synonym` entre l'action de publication du code et la précondition où le code est déjà publié. Conservé : action S06 contre précondition S07, pas de dérive de vocabulaire.

Aucune feature modifiée après cette exécution. Avis relus, pas d'erreur parser ni duplication accidentelle à corriger. Le contrôle DRY Gherkin n'est pas l'analyse DRY du code de production ; `checks.dry` reste `not_run`.

## Revue de complétude

- Sept critères reliés aux sept scénarios et aux sept sections QA stables ; sources humaines/contraintes explicites dans specification.md.
- Aucun scénario optimisé pour le runtime UI Calendar : outils développeur, documentation et GitHub sont aussi des interfaces publiques. Ne pas brancher ces features automatiquement dans les handlers Compose de CAL-001.
- Pas d'architecture, code de production, test unitaire, QA exécutable, runner d'acceptance ou outil qualité source créé.
- Pas de deuxième app imposée ; limites de preuve multi-apps explicites.
- Pas de publication, installation, build ou test Android exécuté pendant la spécification. Leurs résultats restent non vérifiés.
- Contrôle du bon APK dans la release attribué au propriétaire et non marqué PASS.
- Commits/pushes toujours à la charge du propriétaire. Publication future incluse dans le résultat attendu, non simulée par la documentation.

## Revue humaine requise

Spécification prête pour revue uniquement. Attendre l'accord du propriétaire avant de commencer l'implémentation. Visibilité du nouveau dépôt, tag et accès sont des paramètres de livraison à convenir, pas des exigences inventées.
