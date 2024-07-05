package minitwitter.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import minitwitter.admin.AdminControlPanel;
import minitwitter.model.User;
import minitwitter.observer.NewsFeedObserver;
import javax.swing.SwingConstants;

public class UserViewUI implements NewsFeedObserver {

	private AdminControlPanel admin;
	private JFrame frmUserView;
	private User user;

	private JTextArea txtFollowUserId;
	private JTextArea txtTweetMessage;
	private JList<String> listCurrentFollowingUsers;
	private JList<String> listNewsfeed;
	
	private JLabel lblLastUpdateTime;

	public UserViewUI(User user, AdminControlPanel admin) {
		this.user = user;
		this.admin = admin;
		initialize();
		frmUserView.setVisible(true);

	}

	private void initialize() {
		frmUserView = new JFrame();
		frmUserView.setTitle(user.getId());
		frmUserView.setBounds(100, 100, 450, 436);
		frmUserView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmUserView.getContentPane().setLayout(null);

		txtFollowUserId = new JTextArea();
		txtFollowUserId.setBounds(10, 67, 199, 22);
		frmUserView.getContentPane().add(txtFollowUserId);

		JButton btnFollowUser = new JButton("Follow User");
		btnFollowUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				followUser();

			}
		});
		btnFollowUser.setBounds(219, 68, 205, 23);
		frmUserView.getContentPane().add(btnFollowUser);

		txtTweetMessage = new JTextArea();
		txtTweetMessage.setBounds(10, 207, 199, 22);
		frmUserView.getContentPane().add(txtTweetMessage);

		JButton btnPostTweet = new JButton("Post Tweet");
		btnPostTweet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				postTweet();
			}
		});
		btnPostTweet.setBounds(219, 208, 205, 23);
		frmUserView.getContentPane().add(btnPostTweet);

		JScrollPane scrollPaneFollowing = new JScrollPane();
		scrollPaneFollowing.setBounds(10, 102, 414, 100);
		frmUserView.getContentPane().add(scrollPaneFollowing);

		listCurrentFollowingUsers = new JList<String>();
		scrollPaneFollowing.setViewportView(listCurrentFollowingUsers);

		JScrollPane scrollPaneNewsFeed = new JScrollPane();
		scrollPaneNewsFeed.setBounds(10, 240, 414, 145);

		frmUserView.getContentPane().add(scrollPaneNewsFeed);

		listNewsfeed = new JList<String>();
		scrollPaneNewsFeed.setViewportView(listNewsfeed);

		JLabel lblCreationTime = new JLabel("Creation Time: " + user.getCreationTime());
		lblCreationTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCreationTime.setBounds(124, 11, 300, 20);
		frmUserView.getContentPane().add(lblCreationTime);
		
		lblLastUpdateTime = new JLabel();
		displayLastUpdateTime();
		lblLastUpdateTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastUpdateTime.setBounds(124, 36, 300, 20);
		frmUserView.getContentPane().add(lblLastUpdateTime);

		populateCurrentFollowingUsersList();
		populateNewsfeedList();

	}

	protected void postTweet() {
		String tweetMessage = txtTweetMessage.getText();
		if (tweetMessage.length() > 0) {
			user.postTweet(tweetMessage);
			populateNewsfeedList();
			displayLastUpdateTime();
		}

	}

	private void populateNewsfeedList() {
		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("News Feed");
		for (String news : user.getNewsFeed()) {
			listModel.addElement("- " + news);
		}
		listNewsfeed.setModel(listModel);

	}

	private void populateCurrentFollowingUsersList() {

		DefaultListModel<String> listModel = new DefaultListModel<>();
		listModel.addElement("Current Following");
		for (String following : user.getFollowings()) {
			listModel.addElement("- " + following);

		}
		listCurrentFollowingUsers.setModel(listModel);
	}

	public void followUser() {
		String followUserID = txtFollowUserId.getText();
		User followUser = admin.getUserById(followUserID);
		if (followUser != null && followUser != user) {
			user.follow(followUser);
			followUser.registerNewsFeedObserver(this);
			populateCurrentFollowingUsersList();

		}
	}

	@Override
	public void update(String tweet, long updatedTime) {

		populateNewsfeedList();
		displayLastUpdateTime();

	}

	private void displayLastUpdateTime() {
		lblLastUpdateTime.setText("Last Update Time: " + user.getLastUpdateTime());
		
	}
}
