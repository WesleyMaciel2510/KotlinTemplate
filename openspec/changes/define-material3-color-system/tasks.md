## 1. Audit Existing Color Usage

- [x] 1.1 Inventory every declaration in `AppColors.kt` and every color call site in Home, Financeiro/Favorites, Search, Profile, KPI cards, line charts, donut charts, and StatusBadge; verify the inventory is recorded in `COLOR_AUDIT.md`.
- [x] 1.2 Classify each audited color as brand, semantic status, surface/content, or visualization and identify duplicate/inconsistent Pendente, Pago, and Vencido definitions; verify no unresolved duplicate meaning remains in the audit.

## 2. Define and Wire the Token System

- [x] 2.1 Add light and dark Primary, Secondary, and Tertiary role/container/on-role tokens to `AppColors.kt`, preserving Steel Blue as Primary; verify all twelve role families are available to the existing theme integration.
- [x] 2.2 Add independent pending, paid, and overdue semantic foreground/container tokens for both themes; verify each state has one canonical token family and does not alias a brand role.
- [x] 2.3 Add the ordered chart palette derived from brand roles and neutrals; verify entries are stable, adjacent entries are distinguishable, and chart consumers can access the palette without literals.
- [x] 2.4 Update `Theme.kt` to expose the new light/dark roles through the existing AppColors/Material 3 setup; verify the app still compiles without a second theming mechanism.
- [x] 2.5 Calculate WCAG contrast ratios for every documented foreground/background text pair and adjust values as needed; verify all normal-text pairs meet AA and the checked ratios are captured in `DESIGN_SYSTEM.md`.

## 3. Migrate Screens and Shared Components

- [x] 3.1 Refactor Home content, primary actions, active navigation, and key financial figures to use the centralized brand tokens; verify light and dark previews/build paths resolve the same semantic roles.
- [x] 3.2 Refactor Financeiro/Favorites content and receivable status rendering to use the canonical pending/paid/overdue tokens; verify Pendente, Pago, and Vencido remain visually distinct in both themes.
- [x] 3.3 Refactor Search, Profile, KPI cards, LineChartCanvas, DonutChartCanvas, and StatusBadge usages found by the audit; verify no branded/status/chart literal colors remain in those call sites.
- [x] 3.4 Add or preserve chart legends, labels, or accessible descriptions alongside the ordered palette; verify every rendered series remains identifiable without relying on hue alone.

## 4. Document and Validate

- [x] 4.1 Create `DESIGN_SYSTEM.md` with every brand, semantic, surface/content, and chart token's light/dark hex values and intended usage; verify the document matches the final Kotlin values.
- [x] 4.2 Add or update unit/preview coverage for token selection and light/dark status/chart behavior where the project supports it; verify the relevant test cases pass.
- [x] 4.3 Run `./gradlew.bat assembleDebug` from the repository root and verify the debug APK assembles successfully.
- [x] 4.4 Run `./gradlew.bat testDebugUnitTest` from the repository root and verify all JVM unit tests pass.
- [x] 4.5 Inspect Home and Financeiro in both light and dark themes using available previews or instrumented coverage; verify layout, contrast, status meaning, and chart differentiation remain correct.
