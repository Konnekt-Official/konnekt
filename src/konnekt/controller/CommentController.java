package konnekt.controller;

import konnekt.model.dao.CommentDao;
import konnekt.manager.SessionManager;

public class CommentController {

    private final CommentDao dao = new CommentDao();

    public void addComment(int postId, String content) {
        int userId = SessionManager.getCurrentUserId();
        dao.addComment(postId, userId, content);
    }
}
