package com.lcc.blog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcc.blog.R;
import com.lcc.blog.model.Tag;
import com.lcc.state_refresh_recyclerview.Recycler.NiceAdapter;
import com.lcc.state_refresh_recyclerview.Recycler.NiceViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lcc_luffy on 2016/3/5.
 */
public class TestAdapter extends NiceAdapter<Tag>{
    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    protected NiceViewHolder onCreateNiceViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_text,parent,false));
    }
    class Holder extends NiceViewHolder<Tag>
    {
        @Bind(R.id.text)
        TextView textView;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onBindData(Tag data) {
            textView.setText(data.name);
        }
    }
}
