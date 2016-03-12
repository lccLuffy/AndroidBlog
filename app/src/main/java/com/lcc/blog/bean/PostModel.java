package com.lcc.blog.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class PostModel extends Model<List<Post>> {
    public int currentPage;
    public int perPage;
}
