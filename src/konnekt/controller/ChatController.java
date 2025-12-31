package konnekt.controller;

import java.util.ArrayList;
import konnekt.model.dao.ChatDao;
import konnekt.model.dao.UserDao;
import konnekt.model.pojo.ChatPojo;
import konnekt.model.pojo.UserPojo;

import java.util.List;

public class ChatController {

    private final ChatDao chatDao = new ChatDao();
    private final UserDao userDao = new UserDao();

    // send a message
    public boolean sendMessage(int senderId, int receiverId, String content) {
        return chatDao.sendMessage(senderId, receiverId, content);
    }

    // get messages **between two users**
    public List<ChatPojo> getMessagesBetween(int userId1, int userId2) {
        return chatDao.getMessagesBetween(userId1, userId2);
    }

    // mark all messages between two users as read
    public void markMessagesAsRead(int senderId, int receiverId) {
        chatDao.markMessagesAsRead(senderId, receiverId);
    }

    // get user by id
    public UserPojo getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    public List<UserPojo> getChatUsers(int userId) {
        List<UserPojo> list = new ArrayList<>();
        List<Integer> ids = chatDao.getChatUserIds(userId);
        for (int id : ids) {
            UserPojo u = userDao.getUserById(id);
            if (u != null) {
                list.add(u);
            }
        }
        return list;
    }

    public ChatPojo getLatestMessageBetween(int user1Id, int user2Id) {
        return chatDao.getLatestMessageBetween(user1Id, user2Id);
    }

    public List<UserPojo> getAllUsers() {
        return userDao.getAllUsers(); // uses UserDao.getAllUsers()
    }

}
