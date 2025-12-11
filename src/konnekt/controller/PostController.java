package konnekt.controller;

import konnekt.model.dao.PostDao;
import konnekt.model.pojo.PostPojo;

public class PostController {

    private final PostDao postDao = new PostDao();

    public boolean createPost(int userId, String content) {
        PostPojo post = new PostPojo();
        post.setUserId(userId);
        post.setContent(content);
        post.setLikes(0);
        return postDao.addPost(post);
    }

    public void likePost(int postId) {
        postDao.incrementLike(postId);
    }

    public java.util.List<PostPojo> getAllPosts() {
        return postDao.getAllPosts();
    }

    public PostPojo getPostById(int postId) {
        return postDao.getPostById(postId);
    }

    public java.util.List<PostPojo> searchPosts(String keyword) {
        return postDao.searchPosts(keyword);
    }

    public java.util.List<PostPojo> getPostsByUser(int userId) {
        return postDao.getPostsByUser(userId);
    }

    public boolean deletePost(int postId) {
        return postDao.deletePost(postId);
    }
}
