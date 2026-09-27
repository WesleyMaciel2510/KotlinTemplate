## Context

The app already has an `AppColors.kt` Steel Blue foundation and an existing `Theme.kt` path that supplies Compose colors. The requested change spans screen content and reusable chart/status components, so the design must preserve that integration point while making all role decisions explicit. See `proposal.md` and the capability specs for the motivation and behavioral contract.

## Goals / Non-Goals

**Goals:**

- Keep one source of truth for brand, semantic status, and chart colors.
- Preserve Steel Blue as Primary while adding restrained Secondary and Tertiary roles.
- Make light/dark values and contrast expectations reviewable in code and `DESIGN_SYSTEM.md`.
- Give charts stable, ordered colors and accessible textual identification.
- Audit and migrate existing Home/Financeiro and shared-component usages without changing screen behavior.

**Non-Goals:**

- Introducing a second theme provider, runtime color customization, or a new dependency.
- Reworking typography, spacing, component layout, chart geometry, or navigation behavior.
- Treating semantic status colors as interchangeable brand colors.

## Decisions

### Keep the existing AppColors/Theme integration

Extend the current `AppColors.kt` token object and the existing `Theme.kt` light/dark scheme construction. This minimizes migration risk and keeps future screens on the same Compose theme path. A separate design-token library or parallel theme object was considered, but would create two sources of truth.

### Use a restrained three-role palette

- Primary: Steel Blue, approximately `#2F5D7C` in light surfaces and `#9CC9E8` as the dark-theme prominent value. It communicates trust and stability for financial actions and remains legible against both warm and cool neutrals.
- Secondary: muted teal, approximately `#2F7470` / `#82D3C9`. It is adjacent enough to feel coherent with Steel Blue, while its green bias supports positive financial movement and secondary chart series without competing with the anchor.
- Tertiary: amber, approximately `#9A5B00` / `#FFB95C`. It supplies a warm, high-salience highlight against the cool blues in both themes, useful for callouts and chart differentiation without implying that amber is the main brand.

For each role, define light and dark `role`, `onRole`, `roleContainer`, and `onRoleContainer` values. The implementation will verify every text/background pair with a WCAG contrast checker; if a proposed hex value fails AA, adjust the foreground shade while retaining the hue family and documented intent.

### Keep statuses domain-semantic

Use explicit Pending, Paid, and Overdue token families, with success/warning/error aliases only if the existing naming convention benefits from them. Paid uses green, Pending uses a readable amber/orange, and Overdue uses red. Each state gets foreground and container values for both themes. This prevents a chart or brand accent from changing the meaning of a badge.

### Derive a stable chart palette

Expose an ordered list for data series: Primary, Secondary, Tertiary, then a small set of neutral-derived blue/teal/slate variants. Choose entries by perceptual separation rather than raw hue rotation, keep the order stable, and pair every chart with labels/legend or accessible descriptions. Do not encode meaning such as paid or overdue solely through chart color.

### Audit before replacing usages

Inventory declarations and call sites in `AppColors.kt`, Home, Financeiro/Favorites, Search, Profile, KPI cards, line charts, donut charts, and StatusBadge. Map each literal or duplicate token to a brand, semantic, surface, content, or chart role before editing. This makes the migration reviewable and ensures a value is not removed while still serving a distinct purpose.

### Document tokens beside implementation

Add `DESIGN_SYSTEM.md` with token name, light hex, dark hex, contrast pairing, and intended usage. The document is the human-facing reference; Kotlin token names remain the compile-time source of truth.

## Risks / Trade-offs

- [Risk] Existing source files may use names or literals not anticipated by the initial audit → Mitigation: search declarations and call sites first and preserve compatibility aliases temporarily when a rename would cause broad churn.
- [Risk] A hue that looks distinct may fail contrast or color-vision differentiation → Mitigation: calculate WCAG ratios for text pairs and validate adjacent chart entries with a colorblindness-aware visual check plus labels/legends.
- [Risk] Theme wiring may accidentally leave one screen on old values → Mitigation: inspect every audited call site and verify both light/dark builds/previews or tests where available.
- [Risk] Renaming tokens can create unnecessary implementation breakage → Mitigation: prefer additive names or a focused migration, removing obsolete aliases only after all references are migrated.

## Migration Plan

1. Audit current definitions and usages and record the mapping in the implementation review.
2. Add the complete token set and wire it through the existing theme construction.
3. Migrate Home, Financeiro, and shared components; check Search/Profile for consistency and migrate any relevant usages found.
4. Add/update `DESIGN_SYSTEM.md`.
5. Run debug assembly and JVM unit tests; inspect theme-specific UI coverage available in the project.

Rollback is a source change rollback: restore the prior `AppColors.kt`, theme references, and migrated call sites together. No data migration or release-time flag is required.
