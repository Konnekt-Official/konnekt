package konnekt.controller;

import konnekt.model.dao.NotificationDao;
import konnekt.manager.SessionManager;

public class NotificationController {

    private final NotificationDao dao = new NotificationDao();

    public void notifyLike(int postOwnerId, int postId) {
        if (postOwnerId == SessionManager.getCurrentUserId()) return;
        dao.create(postOwnerId, SessionManager.getCurrentUserId(), "LIKE", postId);
    }

    public void notifyComment(int postOwnerId, int postId) {
        if (postOwnerId == SessionManager.getCurrentUserId()) return;
        dao.create(postOwnerId, SessionManager.getCurrentUserId(), "COMMENT", postId);
    }

    public void notifyFollow(int targetUserId) {
        dao.create(targetUserId, SessionManager.getCurrentUserId(), "FOLLOW", null);
    }

    public void notifyMessage(int targetUserId) {
        dao.create(targetUserId, SessionManager.getCurrentUserId(), "MESSAGE", null);
    }
}
