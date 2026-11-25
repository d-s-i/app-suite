# QA Report — MONOREPO-001

## Status

**PASS — périmètre ciblé explicitement demandé : S02 par l'interface publique, revue documentaire S04/S05 et revue des preuves existantes pour les autres contrôles.** Ce verdict n'est pas une réexécution indépendante intégrale de S01 à S07. Les scénarios volontairement non réexécutés sont marqués NOT_RUN, pas PASS par déduction.

Résultat structuré : `result.json`. Aucune défaillance produit observée dans le périmètre exécuté. La QA ciblée est terminée, sans prétendre avoir réalisé une campagne CAL-001 complète, une QA GrapheneOS ou une nouvelle vérification du fichier publié à la place du propriétaire.

## QA scenarios

| ID | Scenario | Result | Evidence |
|---|---|---|---|
| QA-MONOREPO-001-S01 | Suite complète de construction/vérification depuis le monorepo | **NOT_RUN** en exécution complète fraîche ; preuves examinées, conformément au périmètre humain | `../checks/architect-repeat-build.log` et `architect-repeat-acceptance.log` : véritables builds/lint/tests/signatures antérieurs. Les seuls unitaires/générateur/acceptance et la compilation debug ont été fraîchement rejoués comme régression séparée, pas toute S01. |
| QA-MONOREPO-001-S02 | Mise à jour en place et navigation hors ligne par l'interface publique | **PASS** | `evidence/in-place-update.log`, Settings/XML/PNG Month/Day/Back/Week, `public-observations.log`, `week-and-offline.log` |
| QA-MONOREPO-001-S03 | Refus d'une release sans secrets | **NOT_RUN** fraîchement ; preuve antérieure examinée | `../checks/organized-missing-signing.log`, `coder-organized.md` : refus réel, aucune clé/APK de remplacement ; aucune nouvelle invocation négative revendiquée |
| QA-MONOREPO-001-S04 | Trouver et consommer le design partagé sans copie | **PASS**, contrôle documentaire direct | README → guide → définition publique et module versionnés, liens résolus ; `evidence/documentation-review.json` |
| QA-MONOREPO-001-S05 | Parcours d'ajout/livraison d'une app indépendante | **PASS**, contrôle documentaire direct | Guide couvre identité, dépendance design, commandes/APK, versions/données/permissions/signature séparées et régression Calendar, sans seconde app créée |
| QA-MONOREPO-001-S06 | Publication du code/historique et conservation de l'ancien dépôt | **NOT_RUN** fraîchement ; preuve de publication examinée | `../checks/github-release.md`, `coder-organized.md` ; aucun nouveau contrôle distant/historique/ancien dépôt ni audit général de secrets revendiqué |
| QA-MONOREPO-001-S07 | Contrôle du bon APK publié par le propriétaire | **NOT_RUN par l'agent**, contrôle réservé au propriétaire | Retour « oui installé » via Obtainium déjà consigné dans `github-release.md` et l'état ; aucune comparaison SHA du propriétaire ni nouveau téléchargement/install téléphone par l'agent inventé |

Les NOT_RUN ci-dessus ne masquent pas des fixtures manquantes : le demandeur a explicitement limité la nouvelle exécution externe à S02 et demandé une revue des preuves pour le reste. Les preuves de ces revues étaient présentes avant la décision de ne pas réexécuter ces workflows. Le préflight et le setup nécessaires au **périmètre autorisé** sont complets ; les routes de rejeu HTTP/oracles de réponses réseau ne s'appliquent pas à ce workflow Android public. Les attentes UI proviennent de la procédure approuvée, pas de fonctions privées du produit.

## S02 — setup et observations indépendantes

### Baseline identifiée, pas fabriquée

- Archive réelle existante `/tmp/monorepo-f01-debug.apk`, sauvegardée lors de F01 **avant extraction de la palette et déplacement du module applicatif**. Le nom Gradle racine avait déjà été changé ; aucun ancien checkout ni nouvel APK de baseline n'a été reconstruit pour QA. C'est la baseline de l'application avant migration de production, pas une affirmation de hash indépendant enregistré avant toute modification du nom du checkout.
- SHA-256 : `8b4e9a504828b19e9435d24bd4613e8df406c18061532af578926b434f224222`. Copie inchangée conservée dans `evidence/baseline-debug.apk`, ignorée par Git (`*.apk`).
- APK migré : `apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk`, SHA-256 `0c254b5b3c3c2b64159dafd394ad0478f513a72e8b3ba3e032715edd82ffc2cd`.
- Tous deux : `app.local.calendar`, debug 0.1.1 / code 2, certificat `24324e7bff5dd16cb5548385e58829fa9c9486bbd2446e39574953b1e76da89a`, vérifié avec les outils SDK publics. C'est une mise à jour compatible à version égale à travers la migration, pas un nouveau versionCode supérieur à 2.
- Le dispositif n'avait initialement aucun package Calendar ; la vraie baseline a été installée comme setup, puis remplacée en place par l'APK migré, sans désinstallation/effacement entre les deux.

### Fixture publique et appareil isolé

AVD existant dédié `monorepo-001-test`, API 35, serial `emulator-5580`, rendu SwiftShader, écran observé 1080×1920 / 440 dpi. AVD et liste des appareils vérifiés ; `android-dev` et le téléphone propriétaire n'ont reçu aucune action/fixture.

Date, fuseau et format réglés **par les contrôles visibles d'Android Settings** : 15 février 2028, 12:00, UTC, English US, 24 h. Lecture immédiatement avant mise à jour : `Tue Feb 15 12:00:03 UTC 2028`, `Etc/UTC`, config `en-rUS`, format `24`. `settings get`, `getprop`, `am get-config` et `date` sont uniquement des lectures de préflight/vérification : aucun `settings put`, setter d'horloge, écriture DB ou injection du runner.

Airplane mode=1 / Wi-Fi=0 avant/après, ni Play services ni Play Store. État original enregistré avant fixture : Europe/Paris, auto time/zone=1/1, format null (locale par défaut), en-rUS, font scale 1.0, même état réseau.

### Mise à jour et navigation

Commandes publiques exactes et séquence des taps/pickers : `evidence/interactions.md` ; sorties critiques complètes dans les logs de préflight/mise à jour/nettoyage et captures XML/PNG.

1. Baseline installée avec `adb -s emulator-5580 install -r /tmp/monorepo-f01-debug.apk`.
2. Snapshot UID et firstInstallTime, puis `install -r apps/calendar-app/build/outputs/apk/debug/calendar-debug.apk` : **Success**.
3. Un seul package principal Calendar, UID **10148** et firstInstallTime **2026-11-25 17:10:51 UTC** identiques avant/après. L'affichage CET antérieur 18:10:51 représente le même instant, pas une modification de données.
4. Lancement normal `am start -W -n app.local.calendar/.MainActivity` : **Status ok**, aucune instrumentation ni entrée spéciale.
5. Month : screenshot et 42 descriptions de dates complètes dans l'arbre d'accessibilité, exactement de lundi 31 janvier à dimanche 12 mars 2028 ; 7 origines horizontales / 6 origines verticales, aucune date manquante/dupliquée.
6. Tap sur la cellule publique du 20 février : Day affiche **Sun 20**, une seule colonne de date et la grille horaire.
7. Back système : retour à la même grille Month de février et aux 42 dates.
8. Tiroir public → Week : **Mon14 à Sun20**, sept dates alignées avec les sept jours ; aucun écran de compte/réseau, appareil toujours hors ligne.

Les attentes exécutées de S02 concordent avec la feature F01 et la procédure QA : aucune contradiction constatée sur ce périmètre. Les autres scénarios ne sont pas silencieusement réinterprétés ou promus en PASS frais.

Aucun appel de méthode privée, classe de step handler, tag de test Compose ou API de fixture pour ces observations. Les assertions auxiliaires lisent les captures publiques, avec des attentes issues de la procédure/du calendrier standard. Les captures visuelles ont également été examinées.

Préservation des données limitée à ce qui existe dans cette app actuellement vide : continuité d'installation/sandbox, absence d'action destructive et comportement vide conservé. Pas de prétendue préservation d'événements persistés : ce stockage n'existe pas et aucune donnée privée n'a été injectée pour inventer une fixture.

### Nettoyage et régression séparée

Restauré **via Android Settings** : date courante, Europe/Paris, auto time/zone 1/1 et Use locale default (format null). L'horloge se resynchronise au temps hôte ; état réseau/langue inchangé. `cleanup.log` et `post-regression-cleanup.log` confirment la restauration, y compris après régression.

Puis exécuté le pipeline de développement existant, distinct du contrôle externe S02 :

```sh
ANDROID_SERIAL=emulator-5580 ./gradlew :calendar:testDebugUnitTest :calendar:testAcceptanceGenerator :calendar:acceptanceTest --rerun-tasks
```

**PASS : 18 JVM, 4 générateur et 49 cas UI, zéro échec/skip.** La génération réutilise le parseur existant. Les injections documentées et le cycle des APK de fixture gérés par AGP concernent cette régression ultérieure, pas la preuve de S02. Aucune modification produit ne demandait une nouvelle release/lint/signature ; preuves récentes Architect examinées pour ces contrôles, sans les présenter comme réexécutées ici. CRAP/DRY examinés comme contexte, non relancés ; pas de changement du code mesuré, donc aucune invalidation.

Fichier temporaire possédé `/sdcard/monorepo-qa.xml` retiré ; seul l'AVD dédié arrêté. Aucun wipe, clear-data, contournement de signature ou opération sur GitHub. Aucune nouvelle implémentation applicative à livrer sur l'émulateur propriétaire dans cette QA sans correction produit.

## Failures

| ID | Expected | Observed | Classification | Reproduced |
|---|---|---|---|---|
| Probe préflight S02 | Absence GMS acceptée | `pm path` renvoie 1 et le premier bloc `set -e` s'arrête | Gestion de commande QA : résultat d'absence attendu, pas défaut produit/environnement ; reprise avec traitement explicite | Oui, absence confirmée, préflight complété |
| Probe accessibilité Week S02 | Sept nombres du bandeau de dates | Filtre non borné renvoie aussi Today=15 dans la barre d'outils, soit 8 nombres | Sélection QA trop large, pas défaut produit ; bornage au bandeau public + alignement jours/dates, attendu 14–20 inchangé | Oui, ancien filtre reproduit dans `public-observations.log`, contrôle corrigé PASS sur même capture |

Aucun défaut produit nécessitant correction, aucune assertion du produit/acceptance affaiblie ou supprimée.

## Changes made

- QA scripts created or updated: **aucun**, conformément au périmètre humain et à la procédure manuelle. Pas de runner/parser/test/app supplémentaire.
- Product files changed: **aucun**.
- Test files changed: **aucun**.
- Configuration changed: uniquement réglages transitoires de l'AVD dédié via Settings, restaurés ; aucun réglage source/build/signature/hôte/propriétaire modifié.
- Fichiers ajoutés : `qa/result.json`, ce rapport, captures/logs/observations sous `qa/evidence/` et suivi de tâche. L'APK de baseline conservé est ignoré, jamais commité/poussé.

## Remaining issues

- Pas de nouvelle vérification distante de S06/ancien dépôt ni réexécution complète S01/S03 ; ce sont des exclusions de périmètre approuvées, pas des PASS implicites.
- Contrôle S07 réservé au propriétaire : retour d'installation déjà présent, aucune comparaison SHA du propriétaire rapportée/inventée.
- GrapheneOS/Android 17, sauvegarde externe des clés, anciennes réserves de complexité et disponibilité GPU propriétaire ne sont pas validés par cette QA ciblée.
- Aucun événement stocké actuellement, donc pas de test de conservation d'une base métier non vide.

## Human decisions required

Aucune pour terminer **ce périmètre QA ciblé** : S02 et la documentation observée passent. Le propriétaire décide de la clôture administrative/proportionnée de MONOREPO-001 ; ce rapport ne marque ni une QA externe globale ni Hardener comme exécutés.
