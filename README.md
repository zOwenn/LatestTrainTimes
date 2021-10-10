# LatestTrainTimes

## Description
Just a simple android app to show the latest train times at Sheffield Station.

## Implementation
_Just a quick note, when starting on this task - I had planned to do a simple side project to get more accomodated with the Kotlin language, however this quickly grew into an implementation of the Technical Test app. I took this chance to make a pass over code to clean it up for submission to this repo, rather than drop the entire project files at once. Sadly, this has resulted in a diminished demonstration of my development process_

The app was created in AndroidStudio, starting with a blank activity using Kotlin, with a minimum SDK of API26. This was due to errors that I decided wouldn't be worth investigating in this case, as the app was more for a demonstration of development ability. Ideally, I would have liked to have looked this more to increase potential coverage, but it works for the application.

As recommended, TransportAPI was used to collect the data, and by default it collects information for the current time at Sheffield Station. The API keys were saved locally in project root in _"apikey.properties"_ and are referenced in the app-level gradle build to hide them in the build configurations. Though it may not be an ideal way to handle that, though it was a way I found that works.

Functions were created to parse the data recieved from the API, retrieving a list of objects _[TrainTime(destination, platform, arrival_time, departure_time)]_ which hold the values required for the items listed in the RecyclerView. These functions, and the class, could be expanded to collect more data if the views require it. Ideally, an API would have been found that could parse the JSON data, though I believed it would be easier _(although slightly messier)_ to implement these functions within the time.

The app uses a RecyclerView to display individual items for each train, as well as a floating action button to initiate a refresh of the listed data. This is mainly due to the variable number of trains that could be returned from the API query, with RecyclerView being able to dynamically create elements while recycling views no longer on the screen.

## Next Steps
_What would be focussed on in further development_

Given more time, the overall layout of the app could be adjusted to look better, as well as to be condusive to other additional features. Having an easily accessible navigation bar or menu would allow for additional buttons and details to be added without cluttering the activity. It would also allow for space for a settings button with options that could tailor the app for the user. A main example for settings would be aiming for a more inclusive design, including colour themes that could be chosen, and font size options. 

Other additional features would include those listed on the provided document: 
 - the handling of if the user is offline 
 - allowing a user to search for other stations
 - allowing for a time and date to be specified
