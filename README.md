# SimpleRedditFeed

Thank you for checking out my Simple Reddit Feed! 

After cloning the repository, place ensure the following in Android Studio.

Kotlin Version: 1.4.20
Compiled SDK Version: 29 (minimum support for 23)
JVM Target is 1.8

With those settings configured, you can build and run the app from Android Studio.

My goals in developing this app were to hit every requirement and showcase my knowledge as an Android developer, by using some Jetpack and third-party libraries, all while writing clean code, with a solid backing arhictecture.

After first opening the app, you'll be brought to a screen with 2 tabs. The default tab (HOT) lists the first 25 items found at https://www.reddit.com/hot/.json, sorted by date, newest first (requirement 1).

Items in the HOT list display the title, subreddit, date created, thumbnail, and a button to pin/unpin items. (requirement 2A-E)

Each Item can be clicked on and you'll be brought to another screen, where the same information (plus author) is displayed (requirement 3). You can navigate back to the list screen with the device back button, or the back button on the toolbar (requirement 3).

On the list screen, the second tab (PINNED) contains a list of all items that were pinned from the HOT list. Items in this list are stored on the device and persist the app closing and device restarting. The items in this list contain the same information as the HOT list (requirement 4).

The HOT list can be refreshed by pulling down on the list. The HOT list also contains pinned items that are no longer part of the HOT list from Reddit. If you unpin an item that is no longer part of Reddit's HOT list, the item will be removed from the list (requirement 5).

Some notes about the app:

The category field was always returning null from Reddit (even in a browser), so I used the subreddit field in its place.

I made the decision to not include any unit testing. Given the nature of the project, I thought it would be more appropriate to showcase my ability to build and architect an Android app. I have designed the code in such a way that unit testing would be a natural progression if development were to be continued. The views are completely decoupled from the ViewModels, the repository interface could be easily extracted to be mocked and the classes and functions follow the single reponsiblity principle.

If this app were to be continued, I would have used the jetpack Paging library to implement an infinite scroll. 

The Feed Item page could contain a lot more information - but UIs take time to design and I don't think would have showcased much more technical ability. Getting more information to display from Reddit, would have just been a matter of adding more fields to the Data Transfer Objects and Moshi would have done the conversions.

And finally, libraries that I chose to use:

Retrofit: for the HTTP client to do the GET request from Reddit.
Moshi: for parsing and converting the JSON objects to Kotlin objects.
Glide: for loading images from the web, and to sync loading images with the lists's RecyclerViews
Room: for offline persistent storage.
Jetpack Navigation: for fragment transitions and passing arguments between fragments.
Coroutines and LiveData: for asynchronous IO and binding data to their views.

