package com.kftc.openbankingsample2.biz.center_auth.api.manage_pws;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kftc.openbankingsample2.R;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.api.donate_activity.donate_activity;

public class managePws extends AbstractCenterAuthMainFragment {
    private Context context;

    // view
    private View view;

    // data
    private Bundle args;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.manageCnt =0;
        context = getContext();
        args = getArguments();
        if (args == null) args = new Bundle();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manage_pws, container, false);
        initView();
        return view;
    }

    private void initView(){
        TextView dial = view.findViewById(R.id.inputMoney);
        view.findViewById(R.id.one).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");

            }
            dial.append("1");
        });
        view.findViewById(R.id.two).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }
            dial.append("2");
        });
        view.findViewById(R.id.three).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }
            dial.append("3");
        });
        view.findViewById(R.id.four).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("4");
        });
        view.findViewById(R.id.five).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("5");
        });
        view.findViewById(R.id.six).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("6");
        });
        view.findViewById(R.id.seven).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("7");
        });
        view.findViewById(R.id.eight).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("8");
        });
        view.findViewById(R.id.nine).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("9");
        });
        view.findViewById(R.id.zero).setOnClickListener(v ->{
            if(dial.getText().equals("비밀번호 입력")){
                dial.setText("");
            }

            dial.append("0");
        });
        view.findViewById(R.id.reset).setOnClickListener(v ->{
            dial.setText(" ");
        });
        view.findViewById(R.id.del).setOnClickListener(v ->{
            String dialDel = dial.getText().toString();
            dial.setText(dialDel.substring(0,dialDel.length()-1));
        });
        view.findViewById(R.id.btnFin).setOnClickListener(v ->{
            String dialFin = dial.getText().toString();
            if(dialFin.equals("1234")){
                startFragment(donate_activity.class, args, R.string.fragment_donate_activity);
                super.manageCnt =1;
            }
        });
    }

}
