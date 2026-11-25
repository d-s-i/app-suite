# CAL-001 — 0.1.1 hour-label and month-heading correction

## Owner request

The owner reported `04:00` wrapping its last zero on the GrapheneOS phone, approved matching Proton's hour sizing in Day and Week, and requested capital initials for localized month headings while retaining French.

## Verified cause and reference

- Reproduced the old Day rendering on API 35 at 440 dpi: `01:00` occupied two lines. The test was written before the production fix.
- The previous Day width rounded a total 20dp padding, whereas Compose rounds each 10dp side independently. At density 2.75 this removes one available text pixel. The previous Week column was fixed at 52dp, unrelated to enlarged font widths. Wrapping was also allowed by BasicText.
- Pinned Proton 2.34.2 `TimeColumnRenderer.updateTimeLabels` measures displayed hour labels without width constraint; `ViewState.updateTimeColumnBounds` adds both side paddings. Its 12sp hour typography and centering apply to both timed views.
- `DateTimeUtilsImpl.formatMonth(capitalize=true)` capitalizes localized month headings. The global month toolbar uses that option.

## Approved behavior / traceability

- Existing AC-CAL-05 → CAL-001-S05 → QA-CAL-001-S05: capitalize the locale-derived month heading, retaining language, accents and year rules. French Examples now explicitly expect `Février` and `Décembre 2027`; this is the owner-approved presentation revision, not weakened assertions.
- AC-CAL-23 → CAL-001-S23 → QA-CAL-001-S23: all displayed hour labels are complete on one line, without clipping/ellipsis, in Day/Week, 12h/24h, density 2.75 and font scales 1.0/1.6. Eight Examples rows exercise real rendered text layouts.

Implementation measures single-line text and adds separately pixel-rounded paddings, enforces one line, and aligns hour labels on their lines in both views. Normal labels `4 AM` and `04:00` continue to follow device settings. No forced language/hour format, new dependency, event behavior or permission.

The dedicated test viewport is 1080x2400 at 440 dpi; scaled-display tests fail with a clear setup error on an undersized viewport. This is a regression fixture requirement, not a product minimum screen size. Density/font scales are injected at the same UI's composition boundary; no production test flags or private data APIs are added. Also inspected the signed release with actual test-device Settings at 24h/enlarged font.

Version: 0.1.1 / versionCode 2. Same application ID and existing release signing key. The current suite is 21 scenarios / 49 expanded cases. Previous 41-case/0.1.0 reports are historical. Detailed actual results and upgrade evidence are in checks/coder-v011.md. The owner's phone must still revalidate the correction after updating through Obtainium.
