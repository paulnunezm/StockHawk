## Popular Movies
This a movies application for the Android Developer Nanodegree Course. This app also demonstrates the use of multiple libraries
with a clean architecture for fetching the data from [The Movie Database](https://www.themoviedb.org) API.

![1](/screenshots/Screenshot_1.png?raw=true)

## Setting it up with your own API KEY
In your root folder create a gradle.properties file and inside add the following line:
```
API_KEY = "{with you API key here}"
```
And you're good to go.

###Libraries
- **[OkHttp](https://square.github.io/okhttp/)** As a Http client.
- **[ButterKnife](https://github.com/JakeWharton/butterknife)** For view binding.
- **[Stetho](https://facebook.github.io/stetho/)** For intercepting the data requested and view it with Chrome.
- **[Support Palette](https://developer.android.com/reference/android/support/v7/graphics/Palette.html)** For retrieving the predominant colors in a picture.
- **[Glide](https://github.com/bumptech/glide)** To perform image request and loading it to the views.
- **[Gson](https://github.com/google/gson)** For parsing the network's JSON response.

###Architecture
This app uses the [Model View Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter) architecture as explained in [this article](https://saulmm.github.io/2015/02/02/A%20useful%20stack%20on%20android%20%231,%20architecture/)
written by [Saul Molinero](https://plus.google.com/+SaulMolineroMalvido) at his awesome blog. With a few modifications.

Instead of dividing the entire project into the three main layers of the architecture I opted to divide it
in features:

![Feature](/screenshots/feature.png?raw=true)

Rather of using [Otto](https://github.com/square/otto) for the layers communications right now is still using
callbacks interfaces and instead of using multiples classes for extending the base view, presenter and use cases
are done by creating a contract interface for the feature in which each one is extending the base interface.
I consider is easier to read this way.

```
public interface MovieDetailsContract {

    interface Presenter extends com.nunez.popularmovies.mvp.presenters.Presenter {
        void attachView(View detailsView);
        void setTrailerLink();
        void showViews();
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);
        void showTrailers();
        void showReviews();
    }

    interface View extends MVPView{
        void setTrailerLink(String url);
        void showPoster(String url);
        void showTitle (String title);
        void showDescription(String description);
        void showTrailers(ArrayList<Video> trailers);
        void showReviews(ArrayList<Review> reviews);
    }

     interface MovieDetailsController extends UseCase{
         void requestMovieDetails();
         void sendMovieDetailsToPresenter(Movie movie);
    }
}

```
