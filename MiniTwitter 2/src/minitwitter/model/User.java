package minitwitter.model;

import java.util.ArrayList;
import java.util.List;

import minitwitter.observer.NewsFeedObserver;
import minitwitter.observer.NewsFeedSubject;
import minitwitter.visitor.UserGroupVisitor;


public class User extends UserGroupComponent implements NewsFeedObserver, NewsFeedSubject  {
    private String id;
    private List<String> followers;
    private List<String> followings;
    private List<String> newsFeed;
    private List<NewsFeedObserver> observers;
   
    private List<String> tweets;
    private long creationTime;
    private long lastUpdateTime;
    
    public User(String id) {
        this.id = id;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.newsFeed = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.tweets = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
        this.lastUpdateTime = this.creationTime; 
            
    }
    
    public String getId() {
        return id;
    }
    
    public List<String> getFollowers() {
        return followers;
    }
    
    public List<String> getFollowings() {
        return followings;
    }
    
    public List<String> getTweets() {
        return tweets;
    }
    
    public List<String> getNewsFeed() {
        return newsFeed;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
        
    public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void follow(User user) {
        if (!followings.contains(user.getId())) {
            followings.add(user.getId());
            user.addFollower(this.id);
            user.registerNewsFeedObserver(this);
        }
    }
    
    public void addFollower(String followerId) {
        if (!followers.contains(followerId)) {
            followers.add(followerId);
        }
    }
    
    public void postTweet(String tweet) {
    	 String tweetWithUser = id + ": " + tweet;
         tweets.add(tweetWithUser);
         newsFeed.add(tweetWithUser);
         lastUpdateTime = System.currentTimeMillis();
         
         notifyNewsFeedObservers(tweetWithUser, lastUpdateTime);
        
       
    }
    
    

	public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    
    @Override
    public void update(String tweet, long lastUpdated) {
        newsFeed.add(tweet);
        setLastUpdateTime(lastUpdated);
    }
    
    @Override
    public void registerNewsFeedObserver(NewsFeedObserver o) {
        observers.add(o);
    }
    
    @Override
    public void accept(UserGroupVisitor visitor) {
        visitor.visitUser(this);
    }
    
    @Override
    public void notifyNewsFeedObservers(String tweet, long updatedTime) {
    	 for (NewsFeedObserver observer : observers) {
             observer.update(tweet, updatedTime);
             
         }
    }

	
	
}