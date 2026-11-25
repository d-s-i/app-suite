# Local quality checks

Run from the app-suite repository root. These checks reuse the canonical Nix-managed `crap`, `dryer`, `mutate`, `uml` and `uml-quality`; do not vendor or replace their readers. They do not publish APKs or execute independent public-GUI QA.

## Coverage

```sh
nix develop ~/nixos-config#android --command bash -c './gradlew :calendar:coverage --rerun-tasks'
```

This task uses the standard Gradle JaCoCo plugin (the locally available version 0.8.13) and Node 24's built-in test coverage reporter, not a custom counter/report converter:

- Existing 18 JVM tests produce real JVM instruction counters for **all** compiled Calendar and shared-design classes, with no production class exclusions. XML: `apps/calendar-app/build/reports/jacoco/debug/jacoco.xml`; HTML under the same report directory tree.
- Existing generator tests produce native LCOV at `coverage/lcov.info`.
- `--rerun-tasks` is required for fresh test execution rather than regenerating a report from cached test execution data.
- This does **not** collect Android UI coverage. Classes/functions unexecuted by the JVM tests have real zero counters, even though separate generated UI acceptance tests exercise their behavior. Do not describe this as total app-test coverage.
- The private CLI helpers in the generator have zero hits in the generator unit-test report. Their real CLI execution is separately checked by `:calendar:generateAcceptance` and UI acceptance, not included in that LCOV measurement.

No application/runtime dependency or Android permission is added. JaCoCo instruments JVM tests only; normal debug/release APKs are not changed into coverage builds.

## Canonical reports

The latest Cleaner recheck verified the corrected reader in the deployed canonical commands. From the repository root:

```sh
crap "nix develop ~/nixos-config#android --command bash -c './gradlew :calendar:coverage --rerun-tasks'" \
  apps/calendar-app/src/main shared/design/src/main \
  apps/calendar-app/tools/generate-acceptance.mjs --threshold 6

dryer apps/calendar-app/src/main shared/design/src/main \
  apps/calendar-app/tools/generate-acceptance.mjs

mutate --scan apps/calendar-app/src/main shared/design/src/main \
  apps/calendar-app/tools/generate-acceptance.mjs
```

On another machine still using the older reader, the corrected suite can be realized ephemerally without activating the system:

```sh
quality_tools=$(cd "$HOME/nixos-config" && nix build --no-link --print-out-paths --impure \
  --expr 'let p=import ./common/locked-pkgs.nix {};in (p.callPackage ./config/uml-viewer/quality-tools.nix {}).suite')
# Use "$quality_tools/bin/crap", dryer and mutate for that invocation.
```

Reports are `.metrics/crap.edn` and `.metrics/dry.edn`; scan output is a site inventory, **not** a mutation score. Cleaner runs no full mutation campaign. Raw reports/coverage/viewer outputs are ignored generated artifacts; task evidence belongs in `.ai/tasks/MONOREPO-001/checks/`.

`SuiteColors.kt` only declares palette properties; the Kotlin reader has no named source functions to score there (JVM getters still appear in JaCoCo). Gradle `.kts` scripts, manifests and resources are reviewed through configured builds/lint and source review, not assigned invented function-level CRAP scores.

## Kotlin mapping fix and remaining threshold findings

With owner approval, `~/nixos-config/config/uml-viewer/crapper-kotlin-jacoco.py` now recognizes the seven-character value-class hash suffix in JVM names such as `TimeGrid-1FqWxJw`. File identity, source span and uniqueness remain mandatory; ambiguous overloads/multiple methods remain unknown. Regression tests check real zero/partial/full counters, local functions, unrelated names, wrong spans, overloads, wrong files and freshness. No source name or report counter was changed to bypass the reader.

The original fix was validated ephemerally without NixOS/Home Manager activation or pin changes. The latest Cleaner pass independently verified that the default deployed `crapper` now contains the fix and reran the default canonical commands successfully as measurement tools. No installation, activation or pin change was performed by this recheck.

The corrected full-scope run produces a **complete report, exit 2 (threshold findings)** rather than exit 3 (incomplete). `TimeGrid` now has the real JVM-test coverage 0%, CC 10 and CRAP 110. This is not Android UI coverage: all 49 separate UI acceptance cases pass. The wrapper itself cleared its incomplete marker after successful measurement; no manual marker deletion or exclusion occurred.

Other pre-existing UI/parser violations remain visible; all new/modified generator CLI functions score 6. Do not describe the global threshold command as PASS. Cleaner validates the changed-function scope under the workflow's explicit rule that documented unchanged/pre-existing violations do not block that scope; it does not require an incidental rewrite of the whole application. Scope and findings are documented in [the Cleaner report](../.ai/tasks/MONOREPO-001/checks/cleaner.md) and [tool-fix recheck](../.ai/tasks/MONOREPO-001/checks/kotlin-reader-fix.md).

## Diagram and overlay

`uml` refreshes `.uml-viewer/diagram.json` and runs the local viewer until stopped. Run `uml-quality` after measurements to refresh its quality overlay. A partial CRAP marker correctly blocks overlay promotion; complete reports with threshold findings remain usable measurements. After the reader fix, `uml-quality` refreshed the overlay successfully. No old/missing mutation data is presented as current; DRY/scan are not mutation scores shown in UML.
