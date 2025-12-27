package konnekt.controller;

import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;
import konnekt.manager.SessionManager;

import javax.swing.*;

public class PostController {

    private final PostDao postDao;

    public PostController() {
        this.postDao = new PostDao();
    }

    public void createPost(JFrame parent, String content, String imageUrl) {
        if (content.isEmpty() && (imageUrl == null || imageUrl.isEmpty())) {
            JOptionPane.showMessageDialog(parent, "Post cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PostPojo post = new PostPojo();
        post.setUserId(SessionManager.getCurrentUserId());
        post.setContent(content);
        post.setImageUrl(imageUrl);

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