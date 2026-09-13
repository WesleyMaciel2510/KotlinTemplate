## MODIFIED Requirements

### Requirement: Type-Safe Navigation
The application navigation in Compose SHALL be type-safe using Kotlin Serialization object/class routes for Home, Details, Profile, and QR Code Scanner destinations with argument passing.

#### Scenario: Navigating to Details screen with argument
- **WHEN** the user triggers navigation from Home to Details with a specific item ID
- **THEN** the Navigation graph safely unpacks the argument and displays the Details screen with the provided ID

#### Scenario: Navigating to QR Code Scanner screen
- **WHEN** the user triggers navigation to QrCodeRoute
- **THEN** the Navigation graph opens the QrCodeReaderScreen with camera permission check