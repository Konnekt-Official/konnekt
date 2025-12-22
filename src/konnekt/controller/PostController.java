package konnekt.controller;

import javax.swing.JOptionPane;
import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;

import konnekt.component.FeedPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Hp
 */
public class PostController {

    private final PostDao postDao;

    public PostController() {
        this.postDao = new PostDao();
    }

    public void createPost(FeedPanel fp, String content) {
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(fp, "Post content cannot be null!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PostPojo post = new PostPojo();
        if (postDao.addPost(post)) {
            // fp.loadAllPosts();
        } else {
            JOptionPane.showMessageDialog(fp, "Failed to create post. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
