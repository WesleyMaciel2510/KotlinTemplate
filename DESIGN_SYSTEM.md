# Color Design System

The app uses Material 3 through `AppColors.kt` and `TemplateAppTheme`. Steel Blue is the primary brand anchor; status colors are semantic and never substitute for brand roles.

## Brand roles

| Token | Light | Dark | Usage |
|---|---|---|---|
| `primary` | `#1B334B` | `#B0CCE8` | Primary actions, active navigation, key financial figures |
| `onPrimary` | `#FFFFFF` | `#1B334B` | Content on primary |
| `primaryContainer` | `#D1E4F7` | `#344D65` | Brand-tinted surfaces |
| `onPrimaryContainer` | `#001D33` | `#D1E4F7` | Content on primary containers |
| `secondary` | `#23615E` | `#86D5CD` | Secondary actions and supporting chart series |
| `onSecondary` | `#FFFFFF` | `#003735` | Content on secondary |
| `secondaryContainer` | `#B9E6E0` | `#1A4F4C` | Supporting surfaces |
| `onSecondaryContainer` | `#003735` | `#B9E6E0` | Content on secondary containers |
| `tertiary` | `#865300` | `#FFB95C` | Distinct highlights, callouts, chart differentiation |
| `onTertiary` | `#FFFFFF` | `#472A00` | Content on tertiary |
| `tertiaryContainer` | `#FFDEA6` | `#654000` | Highlight surfaces |
| `onTertiaryContainer` | `#2A1800` | `#FFDEA6` | Content on tertiary containers |

Primary reads as stable and trustworthy for financial interactions. Secondary is a muted teal that sits close to Steel Blue while adding a positive, lower-emphasis counterpoint. Tertiary is warm amber, providing clear contrast beside cool blues without becoming a second primary.

## Semantic statuses

| Token family | Light container / content | Dark container / content | Usage |
|---|---|---|---|
| `pending` | `#FFDEA6` / `#2A1800` | `#654000` / `#FFDEA6` | Pendente; awaiting action |
| `paid` | `#B6F2C3` / `#00210D` | `#005326` / `#B6F2C3` | Pago; successfully received |
| `overdue` | `#FFDAD6` / `#410002` | `#BA1A1A` / `#FFDAD6` | Vencido; requires attention |

Use the corresponding foreground token (`pending`, `paid`, or `overdue`) for icons and text on the semantic container. These families are independent from Secondary and Tertiary.

## Chart palette

Charts use `LocalAppColors.current.chartPalette` in this order:

- Light: `#1B334B`, `#23615E`, `#865300`, `#5B6470`, `#3D7EA6`
- Dark: `#B0CCE8`, `#86D5CD`, `#FFB95C`, `#C4C6C9`, `#3D7EA6`

Donut charts and legends use the same indexed color. Legends, labels, or accessible descriptions must identify each series so meaning does not depend on hue alone.

## Contrast checks

Foreground/background pairs were checked against WCAG AA's 4.5:1 normal-text threshold. The measured ratios are:

| Pair group | Light | Dark |
|---|---:|---:|
| Primary / onPrimary | 12.95:1 | 7.80:1 |
| Primary container / onPrimaryContainer | 13.20:1 | 6.74:1 |
| Secondary / onSecondary | 7.14:1 | 7.77:1 |
| Secondary container / onSecondaryContainer | 9.67:1 | 6.82:1 |
| Tertiary / onTertiary | 6.46:1 | 7.72:1 |
| Tertiary container / onTertiaryContainer | 13.22:1 | 7.11:1 |
| Pending | 6.46:1 | 7.72:1 |
| Paid | 6.54:1 | 10.06:1 |
| Overdue | 6.46:1 | 10.09:1 |

All listed pairs meet WCAG AA for normal text. The overdue dark container pair is `#BA1A1A` with `#FFDAD6` and measures 5.00:1.
