## Purpose

Provides one documented, theme-aware color contract for brand identity, semantic financial states, and data visualization so future screens do not invent incompatible colors.

## ADDED Requirements

### Requirement: Brand color roles are complete and theme-aware
The application SHALL expose Primary, Secondary, and Tertiary brand roles with `role`, `onRole`, `roleContainer`, and `onRoleContainer` variants for both light and dark themes. Steel Blue SHALL remain the Primary anchor for primary actions, active navigation, and key financial figures; Secondary SHALL support lower-emphasis actions and chart accents; Tertiary SHALL provide a distinct highlight role without replacing Primary.

#### Scenario: Rendering the light theme
- **WHEN** the app uses the light color scheme
- **THEN** all three brand roles and their corresponding foreground/container roles resolve to the documented light values and each foreground/background pair meets WCAG AA contrast for normal text

#### Scenario: Rendering the dark theme
- **WHEN** the app uses the dark color scheme
- **THEN** all three brand roles and their corresponding foreground/container roles resolve to the documented dark values and each foreground/background pair meets WCAG AA contrast for normal text

### Requirement: Semantic financial statuses are independent from brand accents
The application SHALL provide distinct semantic tokens for pending, paid, and overdue states, including readable foreground/container combinations for light and dark themes. Status tokens MUST communicate state consistently regardless of screen or component and MUST NOT be aliases of Secondary or Tertiary solely because their hues are similar.

#### Scenario: Showing a receivable status
- **WHEN** a receivable is Pendente, Pago, or Vencido
- **THEN** its badge and any equivalent state indicator use the matching pending, paid, or overdue semantic token family in both themes

#### Scenario: Avoiding status ambiguity
- **WHEN** a screen also displays a brand accent or chart series
- **THEN** the status indicator retains its semantic token and does not change meaning based on the surrounding component

### Requirement: Visualization colors are ordered and distinguishable
The application SHALL provide an ordered chart palette containing Primary, Secondary, Tertiary, and neutral-derived series colors for donut segments and multi-line charts. Adjacent series SHALL remain visually distinguishable in light and dark themes and the palette SHALL include a non-color distinction strategy, such as labels, legends, or patterns, for users who cannot distinguish hue.

#### Scenario: Rendering adjacent donut segments
- **WHEN** a donut chart contains two or more adjacent data segments
- **THEN** the chart assigns successive palette entries without reusing a color until the palette is exhausted, and the legend identifies each segment by text/value as well as color

#### Scenario: Rendering multiple lines
- **WHEN** a line chart contains multiple series
- **THEN** each series receives a stable palette entry for the chart lifetime and remains identifiable through its legend or accessible description

### Requirement: Screens and shared components consume centralized tokens
Home, Financeiro/Favorites, Search, Profile, KPI cards, line charts, donut charts, and status badges SHALL use the centralized color contract for branded, semantic, and visualization styling. Literal color values or locally redefined status colors SHALL NOT be used for these purposes.

#### Scenario: Adding a new themed screen
- **WHEN** a screen renders a primary action, active navigation state, status, or chart data
- **THEN** it can obtain the required color from the shared color system and remains consistent when the theme changes

#### Scenario: Auditing existing screen usage
- **WHEN** the color-system refactor is complete
- **THEN** Home and Financeiro render with the same semantic meanings and no conflicting duplicate status definitions remain across the audited screens and shared components
