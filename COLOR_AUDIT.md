# Color Audit

Audit performed before migrating the Material 3 color system.

| Area | Previous usage | Classification | Resolution |
|---|---|---|---|
| `AppColors.kt` / `Theme.kt` | Steel Blue `primary`, duplicated blue `secondary`, steel `tertiary`, neutrals, and Material error roles | Brand, surface/content, semantic error | Primary remains Steel Blue; Secondary is teal; Tertiary is amber; neutrals remain theme surfaces |
| `Color.kt` | A second unused light/dark `ColorScheme` duplicated the Steel Blue definitions | Duplicate theme mechanism | Removed; `AppColors.kt` is the sole color-scheme source |
| Home content | Primary action, KPI values/icons, primary containers; error for overdue activity and tertiary for client-added activity | Brand plus semantic activity states | Primary/containers remain Material roles; overdue remains error semantics and brand tertiary remains highlight |
| Financeiro content | Primary balance card and revenue line; Material error for delinquency; secondary payment-method container | Brand, semantic risk, supporting accent | Brand roles remain centralized; donut colors now use the ordered chart palette |
| `KpiCard` | Primary icon/value and Material error for negative trend | Brand plus trend feedback | Continues to use theme roles; no literal colors found |
| `StatusBadge` | Pending used `secondaryContainer`, paid used `primaryContainer`, overdue used `errorContainer` | Inconsistent semantic meaning | Uses independent pending, paid, and overdue token families |
| `DonutChartCanvas` | Primary, secondary, tertiary, outline with primary fallback | Visualization | Uses the ordered `chartPalette`, wraps only after the palette is exhausted, and retains the text legend |
| `LineChartCanvas` | Caller-provided Material primary/error colors and themed grid/text colors | Visualization plus semantic risk | Uses theme roles; callers retain explicit semantic intent and no literals are introduced |
| Search/Profile | Only default Material text/icon styling; no ad hoc brand/status values | Surface/content | No migration required |

No conflicting duplicate Pendente/Pago/Vencido definitions remain after the audit. Charts and statuses also retain labels/content descriptions so hue is not the only identification mechanism.
