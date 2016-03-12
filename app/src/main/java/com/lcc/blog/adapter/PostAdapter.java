package com.lcc.blog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.blog.R;
import com.lcc.blog.bean.Post;
import com.lcc.blog.ui.user.UserCenterActivity;
import com.lcc.blog.ui.web.WebActivity;
import com.lcc.blog.utils.URLGenerate;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.NiceViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/6.
 */
public class PostAdapter extends NiceAdapter<Post>
{
    public PostAdapter(final Context context) {
        super(context);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                context.startActivity(WebActivity.newIntent(context, URLGenerate.postId2Url(data.get(position).id)));
            }
        });
        setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemClick(int position) {
                context.startActivity(UserCenterActivity.newIntent(context, data.get(position).user_id));
                return true;
            }
        });
    }

    @Override
    protected NiceViewHolder<Post> onCreateNiceViewHolder(ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
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
            created_at.setText(simpleDateFormat.format(data.created_at));
        }
    }
}
