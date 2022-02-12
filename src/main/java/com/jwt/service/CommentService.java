package com.jwt.service;

import java.util.List;

import com.jwt.model.Comment;
import com.jwt.model.Employee;

public interface CommentService {
	public void addComment(Comment employee);

	public List<Comment> getAllComment(Employee emp);

	public void deleteComment(Integer commentId);


	public Comment getComment(int commentid);
}
