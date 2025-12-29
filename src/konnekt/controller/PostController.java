package konnekt.controller;

import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;
import konnekt.manager.SessionManager;

import javax.swing.*;
import java.awt.*;

public class PostController {

    private final PostDao postDao;

    public PostController() {
        this.postDao = new PostDao();
    }

    public void createPost(Component parent, String content) {
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Post cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PostPojo post = new PostPojo();
        post.setUserId(SessionManager.getCurrentUserId());
        post.setContent(content);
        post.setLikes(0);

        if (postDao.addPost(post)) {
            JOptionPane.showMessageDialog(parent, "Post created successfully!");
        } else {
            JOptionPane.showMessageDialog(parent, "Failed to create post.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void likePost(int postId) {
        postDao.likePost(postId);
    }
}
