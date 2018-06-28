# Movies

NOTES:
- In case of error, the app shows a dialog with the error message and displays a Retry button at the bottom of the list. Reload by scroll down won't work if the Retry button is displayed,
but changing the search text will start a new search.
- SearchMoviesPresenterTest should've hold unit tests for this presenter, but there's just one test as I couldn't manage to pass this one. Subscribing to an observable with a TestScheduler
instance resulted in NullPointerException. I've tried the architecture-blueprints from Google, wich uses a custom SchedulerProvider, but with the same results.
- The project contains UI test. Please remember to switch to mockDebug flavor in order to run these tests.

Package structure
.
├── com.alexbarcelo.commons -> Classes and interfaces that may be used across different projects
|   ├── di 	-> Contains custom scopes for activity and fragment dependencies
|   ├── mvp -> Base interfaces for presenters and views in any MVP project
|   ├── rxjava -> Module that provides dependencies for Reactive schedulers
|   └── network -> Contains a dagger module that provides networking dependencies and a type adapter factory to register all type adapters
|
├── com.alexbarcelo.movies
|   ├── android -> Android classes not exclusively related to a single feature
|   ├── data -> Contains model classes, an interface to a movie repository and its TMDB implementation
|   |   └── model -> Classes that represent data from The Movie DataBase API
|   ├── di -> Contains global dependency injection classes and interfaces
|   ├── glide -> Contains a Glide module, required to work with Glide library.
|   ├── popularMovies -> Classes and interfaces related to popular movies feature: Activity, Fragment, Contract, Presenter, Module
|   └── searchMovies -> Classes and interfaces related to movie search feature: Activity, Fragment, Contract, Presenter, Module
|
├── com.sqisland.espresso.idling_resource.elapsed_time -> Contains an implementation for Idling Resource made by Chiu-Ki Chan
|
├── com.alexbarcelo.movies (androidTestMock) -> Contains UI tests that will use classes from the mock flavor
|
├── com.alexbarcelo.movies (androidTestMock) -> Contains UI tests that will use classes from the mock flavor
|
└── com.alexbarcelo.movies (mock) -> Contains a fake repository for testing purposes.