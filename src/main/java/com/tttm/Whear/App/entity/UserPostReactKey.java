package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.Post;
import com.tttm.Whear.App.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class UserPostReactKey implements Serializable {

    @Column(name = "userID")
    private String userID;
    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "postID")
    private Integer postID;
    @ManyToOne
    @JoinColumn(name = "postID", referencedColumnName = "postID", nullable = false, insertable = false, updatable = false)
    private Post post;
}