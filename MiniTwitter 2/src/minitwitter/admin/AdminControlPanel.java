package minitwitter.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import minitwitter.model.User;
import minitwitter.model.UserGroupComponent;
import minitwitter.util.PositiveWords;



//Singleton pattern for Admin Control Panel
public class AdminControlPanel    {
    private static AdminControlPanel instance = null;
    private List<UserGroupComponent> userGroups;
    private List<User> users;
	private String lastUpdatedUserId;

    private AdminControlPanel() {
        userGroups = new ArrayList<>();
        users = new ArrayList<>();
    }

    public static AdminControlPanel getInstance() {
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getUserById(String userId) {
    	
    	for(User user: users) {
    		if(user.getId().equals(userId))
    			return user;
    	}
    	return null;
    } 
    
    
    public void addUserGroup(UserGroupComponent group) {
        userGroups.add(group);
    }

    public int getTotalUsers() {
        return users.size();
    }

    public int getTotalGroups() {
        return userGroups.size();
    }

    public int getTotalTweets() {
        int totalTweets = 0;
        for (User user : users) {
            totalTweets += user.getTweets().size();
        }
        return totalTweets;
    }

    public double getPositiveTweetPercentage() {
        String[] positiveWords = PositiveWords.getPositiveWords();
        int totalTweets = 0;
        int positiveTweets = 0;

        for (User user : users) {
            for (String tweet : user.getTweets()) {
                totalTweets++;
                for (String word : positiveWords) {
                    if (tweet.toLowerCase().contains(word)) {
                        positiveTweets++;
                        break;
                    }
                }
            }
        }
        return totalTweets > 0 ? (double) positiveTweets / totalTweets * 100 : 0;
    }
    
    public boolean validateIDs() {
        Set<String> idSet = new HashSet<>();
        
        // Check for Users
        for (User user : users) {
            String userId = user.getId();
           
            if (userId.contains(" ") || !idSet.add(userId)) {
                return false;
            }
        }

        // Check for User Groups
        System.out.println("groups");
        for (UserGroupComponent group : userGroups) {
            String groupId = group.getId();
           
            if (groupId.contains(" ") || !idSet.add(groupId)) {
                return false;
            }
        }

        return true;
    }

	public String getLastUpdatedUserId() {
		   String userId="";
		   long lastUpdatedTime= 0;
	
		   for (User user : users) {
			
			if(user.getLastUpdateTime()>lastUpdatedTime) {
				
				lastUpdatedTime = user.getLastUpdateTime();
				userId = user.getId();
			}
			           
        }
		
		return userId;
	}

	
}