package es.uvigo.esei.dgss.exercises.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import es.uvigo.esei.dgss.exercises.domain.User;
import es.uvigo.esei.dgss.exercises.domain.Video;
import es.uvigo.esei.dgss.exercise.service.PostEJB;
import es.uvigo.esei.dgss.exercise.service.StatisticsEJB;
import es.uvigo.esei.dgss.exercise.service.UserEJB;
import es.uvigo.esei.dgss.exercises.domain.Comment;
import es.uvigo.esei.dgss.exercises.domain.Friendship;
import es.uvigo.esei.dgss.exercises.domain.Link;
import es.uvigo.esei.dgss.exercises.domain.Photo;
import es.uvigo.esei.dgss.exercises.domain.Post;

@WebServlet("/SimpleServlet")
public class SimpleServlet extends HttpServlet {

    @Inject
    private Facade facade;
    
    @EJB
    private UserEJB userEJB;
    
    @EJB
    private PostEJB postEJB;
    
    @EJB
    private StatisticsEJB statisticsEJB;

    @Resource
    private UserTransaction transaction;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();

        writer.println("<html>");
        writer.println("<body>");
        writer.println("<h1>Facade tests</h1>");

        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='1'>Task 1. Create a new user given its login, name, password and picture"
                + "</button></form>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='2'>Task 2. Create a friendship between two given users"
                + "</button></form>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='3'>Task 3. Get all friends of a given user"
                + "</button></form>");
                
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='4'>Task 4. Get all posts of the friends of a given user"
                + "</button></form>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='5'>Task 5. Get the posts that have been commented by the friends of a given user after a given date"
                + "</button></form>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='6'>Task 6. Get the users which are friends of a given user who like a given post"
                + "</button></form>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='7'>Task 7. Give me all the pictures a given user likes"
                + "</button></form>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='8'>Task 8. Create a list of potential friends for a given user (feel free to create you own 'algorithm')"
                + "</button></form>");

        writer.println("<h1>EJB tests</h1>");
        writer.println("<h2>Task 1</h2>");
        
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='9'>- UserEJB, for retrieving, creating updating and removing users, as well as to create friendships between them and to like posts.e <br>"
        		+ "- PostEJB, for retrieving, creating updating and retrieving posts, as well as to add comments to them."        	
                + "</button></form>");
        
        writer.println("<h2>Task 2</h2>");
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='10'>- Create a StatisticsEJB, allowing you to retrieve the number of users and posts in the social network."        	
                + "</button></form>");
        
        writer.println("<h2>Task 3</h2>");
        writer.println("<form method='POST'>"
                + "<button type='submit' name='task' value='11'>- Add an EmailServiceEJB. This service should send emails asynchronously. In order to use this EJB, send an email to the posts' author everytime a user likes his post."        	
                + "</button></form>");
        
        writer.println("</body>");
        writer.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("<html><body>");
        if (req.getParameter("task").equals("1")) {
            task1(req, resp, writer);
        } else {
        	if (req.getParameter("task").equals("2")) {
        		task2(req,resp,writer);
        	} else {
				if (req.getParameter("task").equals("3")) {
        			task3(req,resp,writer);
        		} else {
        			if (req.getParameter("task").equals("4")) {
        				task4(req,resp,writer);
        			} else {
        				if (req.getParameter("task").equals("5")) {
            				task5(req,resp,writer);
            			} else {
            				if (req.getParameter("task").equals("6")) {
                				task6(req,resp,writer);
                			} else {
                				if (req.getParameter("task").equals("7")) {
                    				task7(req,resp,writer);
                    			} else {
                    				if (req.getParameter("task").equals("8")) {
                        				task8(req,resp,writer);
                        			} else {
                        				if (req.getParameter("task").equals("9")) {
                        					taskEJB1(req,resp,writer);
                            			} else {
                            				if (req.getParameter("task").equals("10")) {
                            					taskEJB2(req,resp,writer);
                                			} else {
                                				if (req.getParameter("task").equals("11")) {
                                					taskEJB3(req,resp,writer);
                                    			}
                                			}
                            			}
                        			}                    				
                    			}
                			}
            			}
        			}
        		}        	
        	}
        }
        writer.println("</body></html>");
    }

    private void task1(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.1 Create a new user given its login, name, password and picture
        	
            transaction.begin();
            
            User u = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            
            writer.println("<h2>Task 1</h2>");
            writer.println("User...<br>-Login: " + u.getLogin() + "<br>-Name: " + u.getName() +
                           "<br>-Picture: " + u.getPicture() + "<br> ...created successfully!<br>");
            writer.println("<a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
    private void task2(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	 
        	// Task 2.2 Create a friendship between two given users
        	
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            Friendship fs = facade.addFriendship(user1, user2, new Date(), true);
            
            writer.println("<h2>Task 2</h2>");
            writer.println("Friendship between <br>- User1: " + user1.getLogin() + " (name: " + user1.getName() + ") <br>- User2: " + user2.getLogin() + " (name: " + user2.getName() +  ") <br>(date: " + fs.getDate() + ")<br>...created successfully!<br>");
            writer.println("<a href='SimpleServlet'>Go to menu</a>");

            transaction.commit();

        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
    private void task3(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.3 Get all friends of a given user
        	
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            User user3 = facade.addUser(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3");
            facade.addFriendship(user1, user2, new Date(), true);
            facade.addFriendship(user3, user1, new Date(), true);
            
            transaction.commit();
            
            transaction.begin();
            
            Collection<User> users = facade.getFriends(user1);
            
            Iterator<User> usersIterator = users.iterator();
            
            writer.println("<h2>Task 3</h2>");
            writer.println("Friends of User1 " + user1.getLogin() + " :<br>");      
            while(usersIterator.hasNext()) {
            	User u = usersIterator.next();
            	writer.println("- " + u.getLogin() + " (name: " + u.getName() + ")<br>" );
            }
            writer.println("Friends of User1 " + user1.getLogin() + " obtained successfully!<br>");
            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			transaction.commit();
			
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

	private void task4(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.4 Get all posts of the friends of a given user
        	
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            User user3 = facade.addUser(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3");
            User user4 = facade.addUser(UUID.randomUUID().toString(), "user", "name4", "password", "picturePath4");
            facade.addFriendship(user1, user2, new Date(), true);
            facade.addFriendship(user3, user1, new Date(), true);
            facade.addLink(UUID.randomUUID().toString(), new Date(), user1, "link1");
            facade.addPhoto(UUID.randomUUID().toString(), new Date(), user2, "photo1");
            facade.addVideo(UUID.randomUUID().toString(), new Date(), user3, 2.0);
            facade.addVideo(UUID.randomUUID().toString(), new Date(), user4, 10.0);
            
            transaction.commit();
            
            transaction.begin();
            
            Collection<Post> posts = facade.getPostFriends(user1);
            
            Iterator<Post> postsIterator = posts.iterator();
            
            writer.println("<h2>Task 4</h2>");
            writer.println("Posts of friends of User1 " + user1.getLogin() + " :<br>");           
            while(postsIterator.hasNext()) { 
            	Post p = postsIterator.next();
            	writer.println("- Post " + p.getId() +" <br>" );
            }
            writer.println("Posts of friends of User1 " + user1.getLogin() + " obtained successfully!<br>");
            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			transaction.commit();
			
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

	private void task5(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.5 Get the posts that have been commented by the friends of a given user after a given date
        	
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            User user3 = facade.addUser(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3");
            User user4 = facade.addUser(UUID.randomUUID().toString(), "user", "name4", "password", "picturePath4");
            
            facade.addFriendship(user1, user2, new Date(), true);
            facade.addFriendship(user3, user1, new Date(), true);
            facade.addFriendship(user4, user1, new Date(), true);
            facade.addLink("link1", new Date(2100,3,2), user1, "link1");
            
            Photo p1 = facade.addPhoto(UUID.randomUUID().toString(), new Date(2900, 1, 1), user2, "photo1");
            Video v1 = facade.addVideo(UUID.randomUUID().toString(), new Date(2020, 1, 1), user3, 2.0);
            Link l1 = facade.addLink(UUID.randomUUID().toString(), new Date(2000, 2, 1), user4, "link2");
            
            facade.addComment(UUID.randomUUID().toString(), "comment1", new Date(1997, 12, 31), user1, p1);
            facade.addComment(UUID.randomUUID().toString(), "comment2", new Date(1997, 12, 31), user2, p1);
            facade.addComment(UUID.randomUUID().toString(), "comment3", new Date(2000, 12, 20), user2, v1);
            facade.addComment(UUID.randomUUID().toString(), "comment4", new Date(2010, 12, 31), user3, p1);
            facade.addComment(UUID.randomUUID().toString(), "comment5", new Date(2010, 12, 31), user3, v1);
            facade.addComment(UUID.randomUUID().toString(), "comment6", new Date(1997, 12, 31), user4, l1);
            
            transaction.commit();
            
            transaction.begin();
            
            Collection<Post> posts = facade.getPostCommentedFriendsAfterDate(user1, new Date(2000, 1, 1));
            Iterator<Post> postsIterator = posts.iterator();
            
            writer.println("<h2>Task 5</h2>");
            writer.println("Posts commented by friends of User1 " + user1.getLogin() + " after 1/1/2000 :<br>");                    
            while(postsIterator.hasNext()) { 
            	writer.println("- Post " + postsIterator.next().getId() +" <br>" );
            }
            writer.println("Posts commented by friends of User1 " + user1.getLogin() + " after 1/1/2000 obtained successfully!<br>");
            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			transaction.commit();
			
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

	private void task6(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.6 Get the users which are friends of a given user who like a given post
        	
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            User user3 = facade.addUser(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3");
            facade.addFriendship(user1, user2, new Date(), true);
            facade.addFriendship(user3, user1, new Date(), true);
            Link l1 = facade.addLink(UUID.randomUUID().toString(), new Date(), user1, "link1");
            Photo p1 = facade.addPhoto(UUID.randomUUID().toString(), new Date(), user2, "photo1");
            Video v1 = facade.addVideo(UUID.randomUUID().toString(), new Date(), user3, 2.0);
            
            facade.addComment(UUID.randomUUID().toString(), "comment", new Date(1997, 12, 31), user1, p1);
            facade.addComment(UUID.randomUUID().toString(), "comment", new Date(2000, 12, 20), user2, v1);
            facade.addComment(UUID.randomUUID().toString(), "comment", new Date(2010, 12, 31), user3, v1);
            
            facade.addLikePost(l1, user2);
            facade.addLikePost(p1, user2);
            facade.addLikePost(l1, user3);
            
            transaction.commit();
            transaction.begin();
            
            Collection<User> users = facade.getFriendsUserPostLiked(user1, l1);
            Iterator<User> usersIterator = users.iterator();
            
            writer.println("<h2>Task 6</h2>");
            writer.println("Friends of User1 " + user1.getLogin() + " gave likes to post " + l1.getId() + " :<br>");                      
            while(usersIterator.hasNext()) { 
            	User u = usersIterator.next();
            	writer.println("- User " + u.getLogin() + " (name: " + u.getName() + ")<br>" );
            }
            writer.println("Friends of User1 " + user1.getLogin() + " gave likes to post " + l1.getId() + " obtained successfully!<br>");

            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			transaction.commit();
			
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

	private void task7(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.7 Give me all the pictures a given user likes
        	
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            User user3 = facade.addUser(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3");
            facade.addFriendship(user1, user2, new Date(), true);
            facade.addFriendship(user1, user3, new Date(), true);
            Link l1 = facade.addLink(UUID.randomUUID().toString(), new Date(), user1, "link1");
            Photo p1 = facade.addPhoto(UUID.randomUUID().toString(), new Date(), user2, "photo1");
            Photo p2 = facade.addPhoto(UUID.randomUUID().toString(), new Date(), user2, "photo2");
            Video v1 = facade.addVideo(UUID.randomUUID().toString(), new Date(), user3, 2.0);
            
            facade.addComment(UUID.randomUUID().toString(), "comment1", new Date(1997,12,31), user1, p1);
            facade.addComment(UUID.randomUUID().toString(), "comment2", new Date(2000,12,20), user2, v1);
            facade.addComment(UUID.randomUUID().toString(), "comment3", new Date(2010,12,31), user3, v1);
            
            facade.addLikePost(l1, user2);
            facade.addLikePost(p1, user1);
            facade.addLikePost(p2, user3);
                    
            transaction.commit();
            transaction.begin();
            
            Collection<Photo> photos = facade.getPhotosLikedUser(user1);
            Iterator<Photo> photosIterator = photos.iterator();
            
            writer.println("<h2>Task 7</h2>");
            writer.println("Photos liked of User1 " + user1.getLogin() + " :<br>");                      
            while(photosIterator.hasNext()) { 
            	writer.println("- Photo " + photosIterator.next().getId() + " <br>" );
            }
            writer.println("Photos liked of User1 " + user1.getLogin() + " obtained successfully!<br>");

            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			transaction.commit();
			
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

	private void task8(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with Facade

        try {
        	
        	// Task 2.8 Create a list of potential friends for a given user (feel free to create you own "algorithm")
        	// People who have liked posts that the User has liked
            transaction.begin();
            
            User user1 = facade.addUser(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1");
            User user2 = facade.addUser(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2");
            User user3 = facade.addUser(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3");
            User user4 = facade.addUser(UUID.randomUUID().toString(), "user", "name4", "password", "picturePath4");
            User user5 = facade.addUser(UUID.randomUUID().toString(), "user", "name5", "password", "picturePath5");
            facade.addFriendship(user1, user2, new Date(), true);
            facade.addFriendship(user1, user3, new Date(), true);
            
            Link l1 = facade.addLink(UUID.randomUUID().toString(), new Date(), user1, "link1");
            Photo p1 = facade.addPhoto(UUID.randomUUID().toString(), new Date(), user2, "photo1");
            Photo p2 = facade.addPhoto(UUID.randomUUID().toString(), new Date(), user1, "photo2");
            Video v1 = facade.addVideo(UUID.randomUUID().toString(), new Date(), user3, 2.0);
            
            facade.addComment(UUID.randomUUID().toString(), "comment1", new Date(1997,12,31), user1, p1);
            facade.addComment(UUID.randomUUID().toString(), "comment2", new Date(2000,12,20), user2, v1);
            facade.addComment(UUID.randomUUID().toString(), "comment3", new Date(2010,12,31), user3, v1);
            
            facade.addLikePost(l1, user2);
            facade.addLikePost(p1, user1);
            facade.addLikePost(p1, user2);
            facade.addLikePost(p1, user3);
            
            facade.addLikePost(l1, user4);
            facade.addLikePost(l1, user5);
            
            facade.addLikePost(p1, user5);
            
            facade.addLikePost(p2, user2);
            
            transaction.commit();
            transaction.begin();
            
            Collection<User> users = facade.getPotentialFriends(user1);
            Iterator<User> usersIterator = users.iterator();
            
            writer.println("<h2>Task 8</h2>");
            writer.println("Potential friends -> People who have liked posts that the User has liked. <br>");
            writer.println("Potential friends of User1 " + user1.getLogin() + " :<br>");                                              
            while(usersIterator.hasNext()) { 
            	writer.println("- User " + usersIterator.next().getName() +" <br>" );
            }
            writer.println("Potential friends of User1 " + user1.getLogin() + " obtained successfully!<br>");

            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			transaction.commit();
			
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
	
	private void taskEJB1(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with EJB

        try {
        	
        	// Task 1. Create two EJB for general management of the social network.
        	// - UserEJB, for retrieving, creating updating and removing users, as well as to create friendships between them and to like posts.
        	// - PostEJB, for retrieving, creating updating and retrieving posts, as well as to add comments to them.
        	// In order to test your EJBs, you can re-use your SimpleServlet. Inject your EJBs inside the Servlet with the @EJB annotation.       	        	
            
            User user1 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1"));
            User user2 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2"));
            userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3"));                                  
            
            Collection<User> users = userEJB.findAllUsers();
            Iterator<User> usersIterator = users.iterator();
            
            writer.println("<h2>Task 1 (EJB)</h2>");
            writer.println("All users:<br>");                                              
            while(usersIterator.hasNext()) { 
            	writer.println("- User " + usersIterator.next().getLogin() + " <br>");
            }
            writer.println("<hr>");                        
            
            writer.println("Create new User:<br>");  
            User user4 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name4", "password", "picturePath4"));
            
            users = userEJB.findAllUsers();
            usersIterator = users.iterator();
            
            writer.println("All users after user creation:<br>");                                              
            while(usersIterator.hasNext()) { 
            	writer.println("- User " + usersIterator.next().getLogin() +" <br>");
            }
            writer.println("<hr>");   
            
            writer.println("Remove User:<br>");  
            userEJB.removeUser(user4);
            
            users = userEJB.findAllUsers();
            usersIterator = users.iterator();
            
            writer.println("All users after user creation:<br>");                                              
            while(usersIterator.hasNext()) { 
            	writer.println("- User " + usersIterator.next().getLogin() +" <br>");
            }
            writer.println("<hr>");        
            
            transaction.begin();
            
            userEJB.addFriendship(new Friendship(userEJB.findUserById(user1.getLogin()), userEJB.findUserById(user2.getLogin()), new Date(), true));            
            writer.println("Add friendship between user1 and user2<br>");
            
            transaction.commit();
            
            Link l1 = postEJB.createLink(new Link(UUID.randomUUID().toString(), new Date(), user1, "link1")); 
            
            transaction.begin();
                        
            userEJB.addLikePost(postEJB.findPostById(l1.getId()), userEJB.findUserById(user1.getLogin())); 
            writer.println("Add user1 like to link1<br>");
            
            transaction.commit();
            
            transaction.begin();
            Photo p1 = postEJB.createPhoto(new Photo(UUID.randomUUID().toString(), new Date(), userEJB.findUserById(user2.getLogin()), "photo1"));
            transaction.commit();
            
            Collection<Post> posts = postEJB.findAllPosts();
            Iterator<Post> postsIterator = posts.iterator();
            
            writer.println("All posts:<br>");                                              
            while(postsIterator.hasNext()) { 
            	writer.println("- Post " + postsIterator.next().getId() +" <br>");
            }
            writer.println("<hr>");
            
            transaction.begin();
              
            Link l2 = postEJB.createLink(new Link(UUID.randomUUID().toString(), new Date(), userEJB.findUserById(user1.getLogin()), "link2"));
            
            writer.println("Create new Link:<br>");
            
            transaction.commit();
            
            posts = postEJB.findAllPosts();
            postsIterator = posts.iterator();
            
            writer.println("All posts after link creation:<br>");                                              
            while(postsIterator.hasNext()) { 
            	writer.println("- Post " + postsIterator.next().getId() +" <br>");
            }
            writer.println("<hr>");   
            
            writer.println("Remove Link:<br>");  
            postEJB.removePost(l2);
            
            posts = postEJB.findAllPosts();
            postsIterator = posts.iterator();
            
            writer.println("All posts after link removed:<br>");                                              
            while(postsIterator.hasNext()) { 
            	writer.println("- Post " + postsIterator.next().getId() +" <br>");
            }
            writer.println("<hr>");                           
            
            Comment c1 = postEJB.addComment(new Comment(UUID.randomUUID().toString(), "comment1", new Date(1997,12,31), user1, p1));
            
            transaction.begin();
            
            postEJB.addComment(c1);
            writer.println("Add Comment to Post<br>");
            
            transaction.commit();
            
            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
        } catch (NotSupportedException | SystemException | SecurityException | IllegalStateException | RollbackException
                | HeuristicMixedException | HeuristicRollbackException e) {
            try {
                transaction.rollback();
            } catch (IllegalStateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SystemException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
	
	private void taskEJB2(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with EJB

        try {
        	
        	// Task 2. Create a StatisticsEJB, allowing you to retrieve the number of users and posts in the social network. 
        	// It should be very efficient (do not access to the DB everytime it is queried) and shared for all users of the 
        	//system (think in Singleton). That is:
        	// - Create a singleton EJB, which ONLY when it is started accesses the database and counts users and posts to a private variable.
        	// - When a user or a post is added, removed, you should call a singleton method to notify this. The singleton updates its internal count.
        	// - Give getter methods for the user and post counts
        	
        	//Note: The statistical module will not take into account users or posts created in the previous JPA exercise
            
            User user1 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name1", "password", "picturePath1"));
            User user2 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name2", "password", "picturePath2"));
            userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name3", "password", "picturePath3"));
            
            writer.println("<h2>Task 2 (EJB)</h2>");
            writer.println("Nº users: " + statisticsEJB.getNumUsers() + "<br>");
            
            Collection<User> users = userEJB.findAllUsers();
            Iterator<User> usersIterator = users.iterator();                      
            
            writer.println("All users:<br>");                                              
            while(usersIterator.hasNext()) { 
            	writer.println("- User " + usersIterator.next().getLogin() +" <br>" );
            }
            writer.println("<hr>");                        
            
            writer.println("Create new User:<br>");  
            User user4 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name", "password", "picturePath4"));
            
            writer.println("Nº users: " + statisticsEJB.getNumUsers() + "<br>");
            
            writer.println("Remove User:<br>");  
            userEJB.removeUser(user4);
            
            writer.println("Nº users: " + statisticsEJB.getNumUsers() + "<br>");
            
            writer.println("<hr>");            
            
            postEJB.createLink(new Link(UUID.randomUUID().toString(), new Date(), user1, "link1"));
            postEJB.createPhoto(new Photo(UUID.randomUUID().toString(), new Date(), user2, "photo1"));                
            
            writer.println("Nº posts: " + statisticsEJB.getNumPosts() + "<br>");
            
            Collection<Post> posts = postEJB.findAllPosts();
            Iterator<Post> postsIterator = posts.iterator();
            
            writer.println("All posts:<br>");                                              
            while(postsIterator.hasNext()) { 
            	writer.println("- Post " + postsIterator.next().getId() +" <br>" );
            }
            writer.println("<hr>");
            
            writer.println("Create new Post:<br>");  
            Link l2 = postEJB.createLink(new Link(UUID.randomUUID().toString(), new Date(), user1, "link2"));
            
            writer.println("Nº posts: " + statisticsEJB.getNumPosts() + "<br>");
            
            writer.println("Remove Post:<br>");  
            postEJB.removePost(l2);
            
            writer.println("Nº posts: " + statisticsEJB.getNumPosts() + "<br>");
            
            writer.println("<hr>");
            
            writer.println("<a href='SimpleServlet'>Go to menu</a>");
            
			
        } catch (SecurityException | IllegalStateException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
    }
	
	private void taskEJB3(HttpServletRequest req, HttpServletResponse resp, PrintWriter writer) 
            throws IOException {
        // work with EJB

        try {
        	
        	// Task 3. Add an EmailService EJB. This EJB allow you to send an email to a given User: sendEmail(User u, String subject, String body).
        	// - This service should send emails asynchronously.
        	// - In order to use this EJB, send an email to the post's author everytime a user likes his post.
        	// - Implement this service using Java Mail inside Wildfly.
            
            User user1 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name", "password", "picturePath1"));
            User user2 = userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name", "password", "picturePath2"));
            userEJB.createUser(new User(UUID.randomUUID().toString(), "user", "name", "password", "picturePath3"));                                    
            
            Link l1 = postEJB.createLink(new Link(UUID.randomUUID().toString(), new Date(),user1, "link1"));
            Photo p1 = postEJB.createPhoto(new Photo(UUID.randomUUID().toString(), new Date(),user2, "photo1"));        
            
            writer.println("<h2>Task 3 (EJB)</h2>");
            writer.println("Sending email to " + l1.getId() + " (author: " + l1.getUser().getLogin() + " ...<br>");
            
            l1 = (Link) userEJB.addLikePost(l1, user2);            
            
            writer.println("Email was sent...<br>");
            writer.println("Sending email to " + p1.getId() + " (author: " + p1.getUser().getLogin() + " ...<br>");
            
            p1 = (Photo) userEJB.addLikePost(p1, user2);
            
            writer.println("Email was sent...<br>");
            
            writer.println("<a href='SimpleServlet'>Go to menu</a>");         
			
        } catch (SecurityException | IllegalStateException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
    }
}
