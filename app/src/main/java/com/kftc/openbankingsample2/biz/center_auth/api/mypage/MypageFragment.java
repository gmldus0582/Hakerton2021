package com.kftc.openbankingsample2.biz.center_auth.api.mypage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kftc.openbankingsample2.R;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.api.account_transaction.CenterAuthAPIAccountTransactionRequestFragment;
import com.kftc.openbankingsample2.biz.center_auth.api.donate_activity.donate_activity;

public class MypageFragment extends AbstractCenterAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;
    public static int sum3;


    // data
    private Bundle args;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        args = getArguments();
        if (args == null) args = new Bundle();
        sum3 = super.sum;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mypage, container, false);
        initView();
        return view;
    }

    private void initView(){
        TextView txtSum = view.findViewById(R.id.mySum);
        String str = Integer.toString(sum3);
        txtSum.setText(str);
        TextView txtpad = view.findViewById(R.id.padNum);
        float pNum = sum/280;
        String pstr = String.format("%.1f",pNum);
        txtpad.setText(pstr);
        view.findViewById(R.id.btnInqrTranRecPage).setOnClickListener(v -> startFragment(CenterAuthAPIAccountTransactionRequestFragment.class, args, R.string.fragment_id_api_call_transaction));
    }
}
