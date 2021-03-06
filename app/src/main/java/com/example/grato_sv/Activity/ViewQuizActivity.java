package com.example.grato_sv.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.grato_sv.Adapter.QuizItemAdapter;
import com.example.grato_sv.Adapter.ShowListQuizAdapter;
import com.example.grato_sv.Adapter.ViewQuizItemAdapter;
import com.example.grato_sv.MainActivity;
import com.example.grato_sv.Model.Answer;
import com.example.grato_sv.Model.ListQuiz;
import com.example.grato_sv.Model.LoginResponse;
import com.example.grato_sv.Model.QuestionAndAnswer;
import com.example.grato_sv.Model.ShowQuestionAndAnswer;
import com.example.grato_sv.R;
import com.example.grato_sv.SessionManagement;
import com.example.grato_sv.ViewModel.GratoViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ViewQuizActivity extends AppCompatActivity implements ViewQuizItemAdapter.ViewQuizItemListener {
    public static int no_question = 0;
    RecyclerView rvList;
    Toolbar toolbar;
    LoginResponse loginResponseSession;
    TextView question_id;
    TextView question_content;
    GratoViewModel mGratoViewModel;
    String subjectName,subjectID,classId;
    public static String quiz_name = "";
    ViewQuizItemAdapter.ViewQuizItemListener viewQuizItemListener = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz);
        SessionManagement sessionManagement = SessionManagement.getInstance(this);
        String loginResponseJson = sessionManagement.getSession();
        Gson gson = new Gson();
        loginResponseSession = gson.fromJson(loginResponseJson, LoginResponse.class);
        subjectName = com.example.grato_sv.MainActivity.getSubjectName();
        subjectID = com.example.grato_sv.MainActivity.getSubjectID();
//        ActionBar toolbar = this.getSupportActionBar();
//        toolbar.setTitle(subjectName);
        Intent intent = getIntent();
        quiz_name = intent.getStringExtra("quiz_name");
        classId = MainActivity.getClassID();
        addControls();
        toolbar.setTitle(subjectName);
        getData();
//        addEvents();
    }
    //    private void addEvents() {
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                i = i -1;
//                finish();
//            }
//        });
//
//    }
    private void addControls() {
        rvList = findViewById(R.id.recycle_4);
        toolbar = findViewById(R.id.dolistquiztoolbar);
        question_id = findViewById(R.id.numquestion);
        question_content = findViewById(R.id.show_question_content);

    }
    public void getData(){
        no_question = no_question+1;
        mGratoViewModel = new ViewModelProvider(this).get(GratoViewModel.class);
        mGratoViewModel.getResponseShowQuestionAndAnswer().observe(this, new Observer<List<ShowQuestionAndAnswer>>() {
            @Override
            public void onChanged(List<ShowQuestionAndAnswer> listAnswer) {
                //Log.d("BBB", listQuizs.toString());
                question_id.setText("Question "+listAnswer.get(no_question-1).getQuestion_id().toString()+ ":");
                question_content.setText("Question :"+ listAnswer.get(no_question-1).getQuestion_content());
                ViewQuizItemAdapter quizAdapter = new ViewQuizItemAdapter((ArrayList<ShowQuestionAndAnswer>) listAnswer);
                quizAdapter.setmViewQuizItemListener(viewQuizItemListener);
                rvList.setHasFixedSize(true);
                rvList.setAdapter(quizAdapter);
            }
        });

        mGratoViewModel.fetchShowQuestionAndAnswer(
                loginResponseSession.getToken(),
                subjectID,
                202,
                classId,
                quiz_name,
                no_question
        );
    }

    @Override
    public void clickEnd() {
        finish();
    }
}