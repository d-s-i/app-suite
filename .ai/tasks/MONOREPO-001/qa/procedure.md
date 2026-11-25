# MONOREPO-001 — Checklist de livraison proportionnée

Pas de nouvelle app, runner, scripts exécutables ou campagne QA lourde. Les opérations sont manuelles ou réutilisent les commandes publiques existantes. Le propriétaire contrôle lui-même le bon APK de la release (S07).

## Règles communes

- Chaque contrôle utilise son identifiant QA, le scénario Gherkin correspondant et le critère AC de specification.md.
- PASS : toutes les observations sont vérifiées ; FAIL : préconditions satisfaites mais résultat contradictoire ; ENVIRONMENT_ERROR : prérequis externes indisponibles ; NOT_RUN : contrôle non exécuté. Le contrôle humain S07 reste NOT_RUN sans retour du propriétaire.
- Consigner révision/diff de livraison, environnement, commandes exactes et codes de sortie, chemins et empreintes publiques des APK, observations et blocages. Jamais de secrets dans les preuves.
- Interface publique : documentation versionnée, commandes développeur documentées, Android/adb public et GitHub. Ne pas appeler de fonction privée, manipuler une base ou injecter des fixtures via API de test.
- Baseline avant migration : garder accessible le checkout/diff existant, les IDs de tâches, l'historique, l'empreinte publique release et l'APK debug installé. Ne pas créer une nouvelle branche/worktree ou modifier les travaux pour obtenir la baseline.
- Les commandes pré-migration sont celles du README : `./gradlew :app:testDebugUnitTest`, `:app:testAcceptanceGenerator`, `:app:acceptanceTest`, `:app:lintDebug`, `:app:assembleDebug`, `:app:assembleRelease`, `:app:testReleaseArtifact`. Utiliser leurs équivalents documentés après migration ; ne pas imposer le nom de module `:app` si l'organisation change. Une commande absente ou incorrecte dans la documentation livrée est un défaut produit, pas un prérequis à inventer.
- Prérequis existants : JDK 17, SDK 35/build-tools 35, Node et parse-gherkin pour la génération, terminal et appareil de test AOSP dédié hors ligne sans Play services pour l'acceptance. Prérequis manquant : ENVIRONMENT_ERROR. Test ou build en échec avec environnement valide : FAIL.
- Horloge, réseau, locale et densité de fixtures : uniquement sur un appareil de test dédié, jamais sur `android-dev` ou le téléphone du propriétaire. La livraison sur l'émulateur propriétaire est une opération distincte, non destructive, suivant AGENTS.md.

# QA-MONOREPO-001-S01 — Construire et vérifier Calendar

## Purpose

Gherkin : MONOREPO-001-F01 / MONOREPO-001-S01. Critère : AC-MONOREPO-01. Réutiliser les contrôles existants depuis le projet migré.

## Preconditions

Prérequis communs disponibles et secrets release existants accessibles hors checkout. Appareil d'acceptance dédié conforme au README ; ne pas substituer le téléphone ou l'émulateur propriétaire.

## Initial state

Racine app-suite avec documentation livrée ; aucune sortie précédente utilisée comme preuve du nouveau build. Sélection explicite du serial de test pour l'acceptance.

## Actions

1. Lire les commandes et chemins de sortie Calendar du README migré.
2. Exécuter les tests unitaires, tests du générateur, acceptance UI existante, lint, builds debug/release et vérification de l'artefact release dans les conditions documentées.
3. Conserver les sorties et vérifier les deux APK aux chemins annoncés. Lire le rapport de contrôle de l'identité/certificat, comparé à l'empreinte publique de baseline.

## Expected observations

Toutes les commandes sortent avec code 0 ; les APK sont issus de cette exécution. Identité app.local.calendar, empreinte release inchangée et certificat debug distinct. Les tests existants de libellés horaires et grandes polices sont inclus, pas remplacés par un smoke test.

## Failure conditions

Commande/documentation cassée, test en échec, APK absent, identité ou certificat changé, contrôle contourné. Absence de secrets fournis : ENVIRONMENT_ERROR pour ce scénario, pas PASS.

## Cleanup

Conserver les rapports utiles. Restaurer uniquement les réglages du dispositif de test modifiés pour la session. Ne pas publier à cette étape.

# QA-MONOREPO-001-S02 — Mettre à jour et naviguer hors ligne

## Purpose

Gherkin : MONOREPO-001-F01 / MONOREPO-001-S02. Critère : AC-MONOREPO-02. Vérifier la continuité de Calendar sans test produit supplémentaire.

## Preconditions

Appareil de test dédié sans Play services, APK debug pré-migration installé et nouvel APK debug vérifié. Fixture autorisée via Android Settings ; ne pas désinstaller en cas de conflit de signature.

## Initial state

2028-02-15 à 12:00 UTC, English United States, 24 h, réseau désactivé. Calendar vide ; noter l'installation app.local.calendar existante avant l'opération. Ces réglages sont réservés au dispositif de test dédié.

## Actions

1. Installer le nouvel APK debug par mise à jour en place (`adb -s <serial-test> install -r <apk-debug-documenté>`), sans désinstallation ou effacement.
2. Ouvrir Calendar et Month, activer Today si nécessaire. Observer février et les 42 cellules.
3. Toucher le 20 février ; observer Day. Appuyer sur Back système ; observer Month.
4. Choisir Week dans le tiroir de navigation ; observer ses dates.

## Expected observations

Une seule installation Calendar mise à jour, pas d'erreur de signature. Février : 7 colonnes, 6 lignes, du 31 janvier au 12 mars 2028 sans trou ni doublon. Day affiche le 20 février ; Back revient à février en Month ; Week affiche les dates du 14 au 20 février. Pas de demande de compte ou réseau.

## Failure conditions

Échec de mise à jour avec préconditions valides, deuxième installation, effacement nécessaire, mauvais intervalle/navigation, crash ou blocage réseau/compte. Dispositif/fixture indisponible : ENVIRONMENT_ERROR.

## Cleanup

Restaurer les réglages originaux du dispositif de test. Ne pas effacer ses données pour contourner un échec. Livrer séparément le debug sur `android-dev` et laisser Calendar ouvert suivant AGENTS.md, sans lui appliquer cette fixture.

# QA-MONOREPO-001-S03 — Refuser une release sans secrets

## Purpose

Gherkin : MONOREPO-001-F01 / MONOREPO-001-S03. Critère : AC-MONOREPO-03. Vérifier que la migration n'introduit pas de signature de contournement.

## Preconditions

Prérequis de build disponibles ; documentation de signature et possibilité de sélectionner un emplacement vide par invocation. Ne jamais déplacer, renommer ou supprimer les vraies clés.

## Initial state

Répertoire temporaire vide, hors checkout et Nix store ; inventaire des APK release préexistants pour distinguer un ancien fichier d'une nouvelle production.

## Actions

1. Sélectionner ce répertoire uniquement pour l'invocation de build release via le mécanisme documenté (`CALENDAR_SIGNING_DIR` actuellement).
2. Exécuter la commande release documentée ; noter code de sortie et explication affichée.
3. Examiner l'emplacement vide et les sorties du build pour identifier toute nouvelle clé ou APK présenté comme publiable.

## Expected observations

Code non nul et explication de secrets manquants. Aucune clé créée ; aucun nouvel APK debug ou non signé présenté comme release prête. Un APK ancien ne constitue pas une nouvelle production ni une réussite.

## Failure conditions

Build réussi sans secrets, repli debug, génération de clé ou message prétendant disposer d'une release publiable. Incapacité à établir la fixture sans toucher aux secrets : ENVIRONMENT_ERROR.

## Cleanup

Rétablir l'environnement d'invocation et retirer uniquement le répertoire temporaire créé pour ce contrôle. Ne pas toucher aux secrets existants.

# QA-MONOREPO-001-S04 — Consulter le design réutilisable

## Purpose

Gherkin : MONOREPO-001-F02 / MONOREPO-001-S04. Critère : AC-MONOREPO-04. Contrôler l'interface documentaire développeur ; pas prétendre tester deux apps.

## Preconditions

Checkout migré et README livrés, accessibles en lecture. Aucun deuxième produit requis.

## Initial state

README ouvert à la racine app-suite ; inventaire des documents et références consultés vide.

## Actions

1. Suivre le lien du README vers le guide de réutilisation du design.
2. Pour chaque élément présenté comme partagé, suivre les références à ses définitions publiques versionnées ou exemples documentés ; relever les chemins et noms.
3. Lire comment le futur développeur les utilise sans recopier les définitions, comment Calendar les consomme et ce qui reste spécifique au calendrier.

## Expected observations

Pas de lien cassé. Éléments partagés existants identifiés, usage sans copie décrit et Calendar identifié comme consommateur. Date/navigation propres à Calendar distinguées de la présentation réutilisable. Le guide ne prétend pas qu'une intégration dans deux applications a été exécutée.

## Failure conditions

Guide introuvable, lien cassé, instructions exigeant de recopier les définitions, absence de séparation explicite ou seule promesse sans référence utilisable. Checkout inaccessible pour raison externe : ENVIRONMENT_ERROR.

## Cleanup

Aucun : lecture seule. La revue de code ultérieure vérifie la factorisation réelle ; elle ne se transforme pas en app QA factice.

# QA-MONOREPO-001-S05 — Consulter le parcours d'ajout d'app

## Purpose

Gherkin : MONOREPO-001-F02 / MONOREPO-001-S05. Critère : AC-MONOREPO-05. Vérifier que le développeur dispose d'un parcours explicite pour la prochaine app.

## Preconditions

Checkout migré, README et guide d'ajout livrés. Il n'est pas demandé d'exécuter ces instructions pour créer un deuxième produit.

## Initial state

README ouvert ; aucune application ajoutée pour le contrôle.

## Actions

1. Suivre le lien vers le guide d'ajout d'application.
2. Relever les instructions d'identité Android, consommation du design, build/test ciblé et emplacement APK.
3. Relever les instructions d'indépendance des versions, données, permissions et secrets de signature, puis les contrôles Calendar à refaire après changement commun.
4. Comparer les commandes Calendar mentionnées à celles effectivement exécutées en S01.

## Expected observations

Chaque sujet de l'action 2 et 3 a une instruction explicite, pas seulement « configurer comme nécessaire ». Les commandes Calendar sont cohérentes avec S01 ; Calendar n'a pas besoin d'une deuxième app pour construire. Le guide ne prescrit pas un partage automatique des données, permissions ou clés entre produits.

## Failure conditions

Sujet manquant, lien cassé, commandes contradictoires, obligation de créer un deuxième produit pour construire Calendar. Environnement externe rendant les documents inaccessibles : ENVIRONMENT_ERROR.

## Cleanup

Aucun : ne créer ni application ni clé de démonstration.

# QA-MONOREPO-001-S06 — Publier le code dans le nouveau dépôt

## Purpose

Gherkin : MONOREPO-001-F03 / MONOREPO-001-S06. Critère : AC-MONOREPO-06. Vérifier la destination de publication et la conservation du dépôt d'origine. Publication/commits/pushes réalisés par le propriétaire.

## Preconditions

Spécification et implémentation approuvées, code revu, accès GitHub autorisé. Propriétaire disponible pour créer/publier dans `d-s-i/app-suite`. Visibilité choisie par lui. Ancien `d-s-i/calendar` accessible.

## Initial state

Diff de migration et baseline d'historique/tâches disponibles. Aucun secret identifié dans le contenu à publier ni dans l'historique à transférer ; si un secret est découvert, interrompre la publication et prévenir le propriétaire, sans l'afficher ni réécrire l'historique arbitrairement.

## Actions

1. Le propriétaire crée le nouveau dépôt `d-s-i/app-suite` et publie l'état revu avec l'historique existant ; l'agent ne commit/push pas.
2. Via l'interface GitHub, vérifier l'adresse du dépôt, son code Calendar, les références au design partagé, les guides et l'historique antérieur à la migration.
3. Comparer les IDs de tâches existants à la baseline et examiner le diff publié pour préserver leurs sources de vérité et les travaux attendus.
4. Vérifier que `d-s-i/calendar` est toujours accessible à son adresse d'origine. Consigner les URL et la revue prépublication d'absence de secrets, sans montrer de valeur privée.

## Expected observations

Nouveau dépôt au nom exact, code et documents migrés disponibles, historique antérieur et IDs existants conservés. Ancien dépôt non supprimé/non renommé. Aucun secret de signature publié. Ne pas prétendre que ce seul contrôle constitue un audit général de sécurité.

## Failure conditions

Mauvaise destination, historique perdu, tâche dupliquée/perdue, secret publié, suppression ou renommage de calendar. Droits GitHub indisponibles ou interruption réseau : ENVIRONMENT_ERROR. Propriétaire n'ayant pas encore publié : NOT_RUN.

## Cleanup

Aucune suppression, aucun archivage ou modification de calendar. Ne pas créer un dépôt alternatif pour contourner les droits. Garder les URL de preuve.

# QA-MONOREPO-001-S07 — Contrôle de la release par le propriétaire

## Purpose

Gherkin : MONOREPO-001-F03 / MONOREPO-001-S07. Critère : AC-MONOREPO-07. Point de contrôle humain demandé explicitement ; aucune QA agent du bon APK n'est imposée.

## Preconditions

Code publié dans app-suite ; APK release local vérifié, signé par la clé existante ; versionCode supérieur aux versions destinées à être remplacées. Tag choisi par le propriétaire. Droits de publication disponibles.

## Initial state

Chemin, identité, version, empreinte SHA-256 de l'APK final et vérification du certificat disponibles pour le propriétaire. Aucune release présumée publiée.

## Actions

1. Le propriétaire publie une release dans `d-s-i/app-suite` et y joint l'APK release final, jamais les secrets.
2. Il vérifie lui-même qu'il peut télécharger l'APK et qu'il s'agit du bon fichier. Comparer son empreinte SHA-256 à celle de l'APK local vérifié est une méthode publique déterministe, sans installation imposée.
3. Il transmet l'URL et son résultat ; consigner ce retour sans effectuer ou inventer cette vérification à sa place.

## Expected observations

URL dans `d-s-i/app-suite`, APK app.local.calendar signé par la clé release existante et identique au fichier vérifié. Téléchargement possible pour le propriétaire. Résultat attribué explicitement au propriétaire.

## Failure conditions

Retour propriétaire indiquant APK manquant, mauvais fichier, mauvaise signature ou mauvais dépôt : FAIL. Accès réseau/GitHub défaillant : ENVIRONMENT_ERROR. Aucun retour ou contrôle non effectué : NOT_RUN, jamais PASS par déduction du build local.

## Cleanup

Aucune modification ni suppression de release par l'agent. Aucun test Obtainium ou effacement d'installation requis. La livraison reste incomplète tant que publication ou contrôle propriétaire manque.
