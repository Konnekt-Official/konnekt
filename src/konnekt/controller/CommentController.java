package konnekt.controller;

import konnekt.manager.SessionManager;
import konnekt.model.dao.CommentDao;
import konnekt.view.NavigatorView;

public class CommentController {

    private final CommentDao commentDao = new CommentDao();

    public void addComment(int postId, String content) {
        int userId = SessionManager.getCurrentUserId();
        commentDao.addComment(postId, userId, content);
    }

    public void openComments(int postId) {
        NavigatorView.showComments(postId);
    }
}