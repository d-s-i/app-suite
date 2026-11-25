# MONOREPO-001 — Décisions de clarification

## D1 — Vérifications proportionnées

Le propriétaire a questionné la nécessité d'une QA et demandé ce qu'elle vérifierait. Aucune application temporaire ou deuxième produit n'est exigé. La spécification réutilise les commandes existantes et une courte checklist de livraison, sans runner ni scripts supplémentaires. La réutilisation réelle dans deux apps n'est pas présentée comme démontrée.

Décision explicite : « la release github avec le bon apk je vérifie moi même ». Le contrôle de la release et de son APK est attribué au propriétaire ; son résultat restera NOT_RUN jusqu'à son retour.

## D2 — Destination de publication

Décisions successives : nom `app-suite` ; publication GitHub incluse et non reportée à une tâche documentaire ; ancien `calendar` peut être gardé ; confirmation finale : « non on créera un nouveau github d-s-i/app-suite ».

Résultat retenu : nouveau dépôt `d-s-i/app-suite` pour le monorepo et sa publication Calendar, ancien dépôt `d-s-i/calendar` conservé sans renommage ou suppression. La proposition antérieure de simplement renommer le dépôt distant, comme celle d'exclure toute publication, est dépassée.

## Limites et préconditions

- Aucun changement GitHub n'a été effectué dans cette phase.
- Les commits/pushes restent à la charge du propriétaire suivant AGENTS.md. L'objectif de publication n'autorise pas l'agent à les effectuer à sa place.
- Tag, visibilité et droits d'accès sont à convenir lors de la livraison. Aucune exigence de visibilité publique ou de CI n'est inventée.
- Pas d'installation Obtainium ni de contrôle agent du bon APK imposés.
- Aucune question d'acceptation bloquante restante ; hypothèses et limites dans task.md et specification.md, sous réserve de revue humaine.
