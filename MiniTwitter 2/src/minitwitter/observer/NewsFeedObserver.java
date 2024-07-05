package minitwitter.observer;

//Observer pattern 
public interface NewsFeedObserver {
	void update(String tweet, long updatedTime);
	
}