package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class User {
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser;
    private static HashMap<Integer, String> avatarsMap = new HashMap<>() {{
        put(0, "file:/C:/Users/A/IdeaProjects/APGraphic/target/classes/images/avatar1.png");
        put(1, "file:/C:/Users/A/IdeaProjects/APGraphic/target/classes/images/avatar2.png");
        put(2, "file:/C:/Users/A/IdeaProjects/APGraphic/target/classes/images/avatar3.png");
        put(3, "file:/C:/Users/A/IdeaProjects/APGraphic/target/classes/images/avatar4.png");
    }};

    private static ArrayList<String> soundtracks = new ArrayList<>() {{
        add("/media/soundtrack1.mp3");
        add("/media/soundtrack2.mp3");
        add("/media/soundtrack3.mp3");
    }};

    private String username;
    private String password;
    private String image;
    private int level;
    private int map;
    private int ballsCount;
    private int[][] scores;
    private boolean mute;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

        Random random = new Random();
        int randomKey = Math.abs(random.nextInt() % 4);
        this.image = avatarsMap.get(randomKey);

        this.ballsCount = 8;
        this.level = 1;
        this.map = 1;
        this.mute = false;
        scores = new int[][]{{0, 0}, {0, 0}, {0, 0}};
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws IOException {
        this.username = username;
        updateDatabase();
    }

    public boolean isPasswordCorrect(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public void setPassword(String password) throws IOException {
        this.password = password;
        updateDatabase();
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        User.users = users;
    }

    public static void addUser(User user) throws IOException {
        users.add(user);
        updateDatabase();
    }

    public static void removeUser(User user) throws IOException {
        users.remove(user);
        updateDatabase();
    }

    public static User getUserByName(String username) {
        if(users.size() > 0) {
            for (User user : users)
                if(user.getUsername().equals(username))
                    return user;
        }
        return null;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) throws IOException {
        this.image = image;
        updateDatabase();
    }

    public static void initializeUsersFromDatabase() throws FileNotFoundException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        JsonReader reader = new JsonReader(new FileReader("usersDatabase.json"));
        User[] usersTempArray = gson.fromJson(reader, User[].class);
        if(usersTempArray != null) {
            ArrayList<User> usersTempList = new ArrayList<>(Arrays.asList(usersTempArray));
            User.setUsers(usersTempList);
        }
    }

    public static void updateDatabase() throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        FileWriter writer = new FileWriter("usersDatabase.json");
        gson.toJson(User.getUsers(), writer);
        writer.close();
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public static HashMap<Integer, String> getAvatarsMap() {
        return avatarsMap;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) throws IOException {
        this.level = level;
        updateDatabase();
    }

    public int getBallsCount() {
        return ballsCount;
    }

    public void setBallsCount(int ballsCount) throws IOException {
        this.ballsCount = ballsCount;
        updateDatabase();
    }

    public int getScore(int level) {
        return scores[level - 1][0];
    }

    public int getTime(int level) {
        return scores[level - 1][1];
    }

    public void setScore(int level, int score) throws IOException {
        scores[level - 1][0] = score;
        updateDatabase();
    }

    public void setTime(int level, int time) throws IOException {
        scores[level - 1][1] = time;
        updateDatabase();
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public static ArrayList<User> sortUsers(int level) {
        ArrayList<User> sortedUsers = new ArrayList<>();
        sortedUsers.addAll(users);
        for(int i = 0 ; i < sortedUsers.size() - 1 ; i++)
            for(int j = 0 ; j < sortedUsers.size() - i - 1 ; j++) {
                if(!compareUser(sortedUsers.get(j), sortedUsers.get(j + 1), level))
                    Collections.swap(sortedUsers, j, j + 1);
            }

        return sortedUsers;
    }

    public static boolean compareUser(User user1, User user2, int level) {
        if(user1.getScore(level) > user2.getScore(level)) return true;
        else if(user1.getScore(level) < user2.getScore(level)) return false;

        if(user1.getTime(level) < user2.getTime(level)) return true;
        else if(user1.getTime(level) > user2.getTime(level)) return false;

        if(user2.getUsername().compareTo(user1.getUsername()) > 0) return true;
        else return false;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public static ArrayList<String> getSoundtracks() {
        return soundtracks;
    }
}
