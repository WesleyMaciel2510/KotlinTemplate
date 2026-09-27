## Why

The app's Steel Blue foundation needs a single, documented Material 3 token system before additional screens are built. Today, screen and chart colors can drift into ad hoc values or duplicate status meanings, making light/dark theme behavior and visual consistency harder to maintain.

## What Changes

- Audit the existing `AppColors.kt` tokens and their use in Home, Financeiro/Favorites, Search, Profile, and shared KPI/chart/status components.
- Establish Steel Blue as the Primary brand role and add complementary Secondary and distinct Tertiary roles, each with light and dark Material 3 role pairs.
- Add contrast-checked `on*` and `*Container` roles for Primary, Secondary, and Tertiary.
- Formalize pending, paid, and overdue semantic status colors independently from brand roles.
- Define an ordered, colorblind-conscious chart palette for donut segments and multi-line charts.
- Refactor Home and Financeiro color references to use the centralized tokens and remove inconsistent/ad hoc color usage.
- Document the resulting light/dark hex values and intended usage in `DESIGN_SYSTEM.md`.
- Verify the refactor with debug assembly and JVM unit tests, including light and dark theme coverage where existing UI tests support it.

## Capabilities

### New Capabilities

- `material3-color-system`: Defines the app-wide brand, semantic status, and visualization color contracts used by every screen and shared component.

### Modified Capabilities

- `ui-components`: Shared KPI, chart, and status components must consume the centralized color roles and preserve readable state/data differentiation across themes.

## Impact

- `AppColors.kt` and the existing `Theme.kt` integration will gain the complete token set without introducing a second theming mechanism.
- Home, Financeiro/Favorites, and any existing shared components that currently use literal or inconsistent colors will be updated.
- A new design-system reference document will be added at the project documentation level.
- No public API, persistence, or dependency changes are expected.
