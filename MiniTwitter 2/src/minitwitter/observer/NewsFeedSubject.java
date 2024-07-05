package minitwitter.observer;

public interface NewsFeedSubject {
    void registerNewsFeedObserver(NewsFeedObserver o);
    void notifyNewsFeedObservers(String tweet, long updatedTime);
   
}