# MONOREPO-001 — Transformer Calendar en monorepo multi-apps avec un design réutilisable

## Demande originale

> créer moi la tache de tout mettre en monorepo et optimiser pour la création de plusieurs apps avec une réutilisation du design

## Décisions humaines confirmées

- Créer un monorepo nommé `app-suite` pour développer plusieurs applications avec réutilisation du design.
- Inclure la publication de Calendar sur GitHub dans le cadre de ce monorepo : une livraison locale/documentaire ne suffit pas.
- Créer un nouveau dépôt `d-s-i/app-suite`, et non renommer le dépôt GitHub existant. Le propriétaire a indiqué pouvoir garder `calendar`, puis confirmé explicitement « on créera un nouveau github d-s-i/app-suite ».
- Le propriétaire vérifiera lui-même que la release GitHub contient le bon APK. Ne pas imposer une QA agent sur ce contrôle ni présumer son résultat.
- Aucune application temporaire de QA n'est exigée ou approuvée. Vérifications proportionnées, sans nouveau dispositif de QA.
- Le rôle specifier ne met pas en œuvre la migration ni la publication. Les commits/pushes restent à la charge du propriétaire suivant AGENTS.md.

## Objectif

Faire évoluer Calendar en base multi-apps permettant de réutiliser son design sans le recopier. Préserver Calendar et ses installations, rendre l'ajout d'une future application documenté et publier le code ainsi qu'une release Calendar dans le nouveau dépôt.

Acteurs : propriétaire développeur et éditeur des APK ; utilisateurs de Calendar qui doivent pouvoir continuer à l'utiliser et le mettre à jour.

## Contexte vérifié avant migration

- Racine Git actuelle : `/home/anon/Documents/Workspace/calendar-app` ; remote `git@github.com:d-s-i/calendar.git`.
- Un module applicatif `:app`, Kotlin et Compose UI/Foundation, sans Material3 ni services Google Play.
- Calendar utilise l'identifiant `app.local.calendar` ; version locale actuelle `0.1.1`, versionCode `2`. Ces valeurs observées ne prouvent pas quelle version est publiée ou installée chez le propriétaire.
- Design existant dans les interfaces Calendar ; palette `CalendarColors` dans `CalendarApp.kt`.
- Tests unitaires, génération d'acceptance, tests UI, lint, builds debug/release et contrôle de signature existants, documentés dans README.md.
- Identités CAL-001, EVENTS-001, PERSISTENCE-001 et REMINDERS-001 à conserver, avec leurs artefacts et travaux en cours.
- Signature release existante ; secrets hors Git et Nix store, empreinte publique versionnée. Publication et suivi Obtainium non vérifiés par l'agent.
- Workspace déjà modifié : ne pas perdre les changements utilisateur ni les fichiers non suivis.

## Périmètre

- Migration du projet existant vers `app-suite`, sans refonte fonctionnelle ou visuelle de Calendar.
- Mutualisation du design existant pertinent et documentation de son usage par une future app, sans imposer ici la disposition des dossiers, les modules ou les interfaces techniques.
- Préservation de l'identité, des signatures, des permissions et du fonctionnement hors ligne de Calendar ; pas d'effacement des installations.
- Adaptation des commandes, outils, instructions et références affectés. Réutilisation des tests et du générateur d'acceptance existants.
- Documentation de l'ajout, de la construction et de la distribution indépendante d'applications.
- Création du nouveau dépôt `d-s-i/app-suite`, publication du code et d'une release Calendar avec APK signé ; contrôle de cet APK par le propriétaire.
- Ancien dépôt `d-s-i/calendar` conservé, sans suppression ni renommage. Ne pas maintenir deux copies actives du code dans cette tâche.

## Hors périmètre

- Deuxième application produit ou de QA, import d'autres projets non identifiés.
- Événements, persistance, rappels, synchronisation et refonte Calendar.
- Framework universel, catalogue de composants futurs, prise en charge multiplateforme.
- CI, automatisation de publication ou installation/test Obtainium par l'agent. Une mise à jour de l'URL de suivi pourra être documentée sans prétendre l'avoir exécutée.
- Réorganisation des releases de futures applications : à décider quand elles existent.
- Suppression, archivage ou modification du contenu de l'ancien dépôt sans demande supplémentaire.

## Contraintes permanentes

- Accord explicite avant implémentation et avant nouvelle dépendance. Aucun commit ni push par l'agent.
- Pas de nouvelles permissions ou dépendances Google Play, ni de code/assets Proton.
- Garder l'identifiant `app.local.calendar`, la clé release existante et la séparation debug/release. VersionCode de publication supérieur aux versions que la release doit remplacer.
- Secrets hors Git et Nix store ; échec explicite si signature indisponible, jamais de repli debug pour publier.
- Conserver les IDs et sources de vérité des tâches existantes ; ne pas éditer l'acceptance générée à la main.
- Livrer l'implémentation future sur l'émulateur persistant `android-dev` suivant AGENTS.md, sans effacement ni changement de signature de contournement.

## Hypothèses explicites de spécification

- Les futures applications concernées à court terme sont Android Kotlin/Compose ; seule Calendar existe à migrer.
- Le design de départ est celui de Calendar, pas une nouvelle norme visuelle fondée sur des suggestions antérieures non approuvées.
- Palette, styles typographiques et composants génériques existants pertinents sont des candidats à la mutualisation ; le choix technique exact relève de l'implémentation/revue.
- La première release de cette migration sera hébergée dans `d-s-i/app-suite`, conformément à la demande de publication dans le cadre du monorepo. Pas de double publication requise dans `calendar`.
- Les builds/tests et une courte checklist de livraison suffisent pour cette tâche ; la réutilisation par une deuxième app ne sera pas prétendue testée.

## Questions restantes

Aucune question d'acceptation bloquante après la confirmation de `d-s-i/app-suite`. Le tag de release, la visibilité du nouveau dépôt et la date de publication sont des paramètres de livraison à fixer avec le propriétaire sans accès externe non autorisé. Les droits GitHub et les secrets disponibles sont des préconditions d'exécution, pas des succès présumés.

Les choix de structure, de factorisation et de gestion des remotes relèvent des rôles ultérieurs, sans décision architecturale ici.

## Livrables et prochain rôle

Voir `specification.md`, `features/` et `qa/procedure.md`. Prochaine étape : revue humaine de la spécification avant toute implémentation. La publication reste dans le périmètre même si son contrôle final appartient au propriétaire.
