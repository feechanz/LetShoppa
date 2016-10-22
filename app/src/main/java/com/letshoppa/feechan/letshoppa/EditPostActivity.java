package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Post;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class EditPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        setTitle(R.string.edit);

        Button mCancelBtn = (Button) findViewById(R.id.cancelButton);
        Button mSaveBtn = (Button) findViewById(R.id.saveButton);
        mPostEditText = (EditText) findViewById(R.id.postEditText);

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePost();
            }
        });

        Intent i = getIntent();
        currentPost = (Post) i.getSerializableExtra(Post.TAG_POST);
        if(currentPost != null)
        {
            mPostEditText.setText(currentPost.getTulisan() );
        }
    }

    Post currentPost;
    EditText mPostEditText;
    private void savePost()
    {
        if(currentPost != null) {
            String url = AppHelper.domainURL + "/AndroidConnect/PutPost.php";
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            boolean cancel = false;
            int postid = currentPost.getPostid();
            String tulisan = mPostEditText.getText().toString();
            if (TextUtils.isEmpty(tulisan)) {
                mPostEditText.requestFocus();
                mPostEditText.setError(getString(R.string.error_field_required));
                cancel = true;
            }

            if (!cancel) {
                parameter.add(new BasicNameValuePair(Post.TAG_POSTID, String.valueOf(postid)));
                parameter.add(new BasicNameValuePair(Post.TAG_TULISAN, tulisan));
                UpdateDataTask task = new UpdateDataTask(url, parameter, EditPostActivity.this, new closeClass());
                task.execute();
            }
        }
    }

    class closeClass implements IRefreshMethod
    {

        @Override
        public void refresh() {
            finish();
        }
    }
    private void cancel()
    {
        finish();
    }

}
