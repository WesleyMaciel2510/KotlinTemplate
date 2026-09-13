## Purpose

Provides automated dependency injection across application components using Hilt to eliminate manual container wiring and support ViewModel lifecycle injection in Jetpack Compose.

## ADDED Requirements

### Requirement: Dependency injection with Hilt
The application framework SHALL inject dependencies into ViewModels, repositories, data sources, and mappers using Hilt annotations.

#### Scenario: Injecting ViewModels into Composable screens
- **WHEN** a Composable screen requests a ViewModel
- **THEN** Hilt SHALL provide a properly constructed ViewModel instance with all required use cases, repositories, and mappers automatically injected.

#### Scenario: Binding repository interfaces
- **WHEN** a component requests an instance of `ExampleRepository`
- **THEN** Hilt SHALL resolve and provide the `ExampleRepositoryImpl` singleton with its data source and mapper dependencies provided.

### Requirement: Injectable data mapper
The application SHALL provide data mapping utilities as injectable components so that repository implementations can map data transfer objects to domain models without direct instantiation or missing dependency errors.

#### Scenario: Mapping DTOs to domain objects in repository
- **WHEN** the repository fetches data transfer objects from a data source
- **THEN** the injected mapper component SHALL transform the transfer objects into domain entities.
