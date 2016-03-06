package com.lcc.blog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.blog.R;
import com.lcc.blog.activity.WebActivity;
import com.lcc.blog.model.Post;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.NiceViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class PostAdapter extends NiceAdapter<Post> {
    public PostAdapter(final Context context) {
        super(context);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url", data.get(position).id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected NiceViewHolder onCreateNiceViewHolder(ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    class H extends NiceViewHolder<Post> {
        @Bind(R.id.title)
        TextView title;

        @Bind(R.id.username)
        TextView username;

        @Bind(R.id.created_at)
        TextView created_at;

        public H(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindData(Post data) {
            title.setText(data.title);
            username.setText(data.username);
            created_at.setText(data.created_at);
        }
    }
}
