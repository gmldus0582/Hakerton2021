package com.kftc.openbankingsample2.biz.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.kftc.openbankingsample2.R;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthConst;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthHomeFragment;
import com.kftc.openbankingsample2.biz.center_auth.api.user_me.CenterAuthAPIUserMeRequestFragment;
import com.kftc.openbankingsample2.biz.center_auth.api.user_me.CenterAuthAPIUserMeResultFragment;
import com.kftc.openbankingsample2.biz.center_auth.auth.CenterAuthFragment;
import com.kftc.openbankingsample2.biz.center_auth.http.CenterAuthApiRetrofitAdapter;
import com.kftc.openbankingsample2.biz.self_auth.SelfAuthHomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;
import com.kftc.openbankingsample2.BuildConfig;
import com.kftc.openbankingsample2.common.Scope;
import com.kftc.openbankingsample2.common.application.AppData;
import com.kftc.openbankingsample2.common.data.AccessToken;
import com.kftc.openbankingsample2.common.data.ApiCallUserMeResponse;
import com.kftc.openbankingsample2.common.util.Utils;

/**
 * 센터인증과 자체인증을 선택하는 앱의 메인화면
 */
public class HomeFragment extends AbstractCenterAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;
    private View view2;
    private View view3;

    // data
    private Bundle args;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        args = getArguments();
        if (args == null) args = new Bundle();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        return view;

    }
//    public View onCreateView2(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        initView();
//        return view2;
//
//    }

    private void initView() {
        // 로그인
        // access_token : 가장 최근 토큰으로 기본 설정
        view2 = getLayoutInflater().inflate(R.layout.fragment_center_auth_api_account_balance,null,false);
        EditText etToken = view.findViewById(R.id.etToken);
        etToken.setText(AppData.getCenterAuthAccessToken(Scope.INQUIRY));

        // access_token : 기존 토큰에서 선택
        view2.findViewById(R.id.btnSelectToken).setOnClickListener(v -> showTokenDialog(etToken, Scope.INQUIRY));

        // 은행거래고유번호(20자리)
        EditText etBankTranId = view2.findViewById(R.id.etBankTranId);
        setRandomBankTranId(etBankTranId);
        view2.findViewById(R.id.btnGenBankTranId).setOnClickListener(v -> setRandomBankTranId(etBankTranId));

        // 핀테크이용번호 : 최근 계좌로 기본 설정
        EditText etFintechUseNum = view2.findViewById(R.id.etFintechUseNum);
        etFintechUseNum.setText(Utils.getSavedValue(CenterAuthConst.CENTER_AUTH_FINTECH_USE_NUM));

        // 핀테크이용번호 : 기존 계좌에서 선택
        View.OnClickListener onClickListener = v -> showAccountDialog(etFintechUseNum);
        view2.findViewById(R.id.btnSelectFintechUseNum).setOnClickListener(onClickListener);

        // 거래일시
        EditText etTranDtime = view2.findViewById(R.id.etTranDtime);
        etTranDtime.setText(new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(new Date()));

        //----------계좌선택---------------------------------------------------------------
        view3 = getLayoutInflater().inflate(R.layout.fragment_center_auth_api_user_me_request,null,false);
        etToken.setText(AppData.getCenterAuthAccessToken(Scope.LOGIN));

        // user_seq_no : 가장 최근 사용자 일련번호로 기본 설정
        EditText etUserSeqNo = view3.findViewById(R.id.etUserSeqNo);
        etUserSeqNo.setText(Utils.getSavedValue(CenterAuthConst.CENTER_AUTH_USER_SEQ_NO));

        // access_token : 기존 토큰에서 선택
        view3.findViewById(R.id.btnSelectToken).setOnClickListener(v -> showTokenDialog(etToken, etUserSeqNo, Scope.LOGIN));

        //계좌등록
        String strToken = etToken.getText().toString();

        if(strToken.length()==1){

            view.findViewById(R.id.btnAuthToken).setOnClickListener(v -> startFragment(CenterAuthFragment.class, args, R.string.fragment_id_center_auth));
            view.findViewById(R.id.btnInqrUserInfoPage).setOnClickListener(v -> showAlert("error","본인인증이 필요합니다."));

        }
        else{

            view.findViewById(R.id.btnAuthToken).setOnClickListener(v->{
                startFragment(CenterAuthHomeFragment.class, args, R.string.fragment_id_center);
                setRandomBankTranId(etBankTranId);
                String accessToken = etToken.getText().toString().trim();
                Utils.saveData(CenterAuthConst.CENTER_AUTH_ACCESS_TOKEN, accessToken);
                String fintechUseNum = etFintechUseNum.getText().toString();
                Utils.saveData(CenterAuthConst.CENTER_AUTH_FINTECH_USE_NUM, fintechUseNum);

                // 요청전문
                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("bank_tran_id", etBankTranId.getText().toString());
                paramMap.put("fintech_use_num", fintechUseNum);
                paramMap.put("tran_dtime", etTranDtime.getText().toString());

                showProgress();
                CenterAuthApiRetrofitAdapter.getInstance()
                        .accountBalanceFinNum("Bearer " + accessToken, paramMap)
                        .enqueue(super.handleResponse("balance_amt", "잔액"));

            });

            view.findViewById(R.id.btnInqrUserInfoPage).setOnClickListener(v -> {
                String accessToken = etToken.getText().toString().trim();
                Utils.saveData(CenterAuthConst.CENTER_AUTH_ACCESS_TOKEN, accessToken);
                String userSeqNo = etUserSeqNo.getText().toString().trim();
                Utils.saveData(CenterAuthConst.CENTER_AUTH_USER_SEQ_NO, userSeqNo);

                HashMap<String, String> paramMap = new HashMap<>();
                paramMap.put("user_seq_no", userSeqNo);

                showProgress();
                CenterAuthApiRetrofitAdapter.getInstance()
                        .userMe("Bearer " + accessToken, paramMap)
                        .enqueue(super.handleResponse("res_cnt", "등록계좌수", responseJson -> {

                                    // 성공하면 결과화면으로 이동
                                    ApiCallUserMeResponse result = new Gson().fromJson(responseJson, ApiCallUserMeResponse.class);
                                    args.putParcelable("result", result);
                                    goNext();
                                })
                        );
            });


        }

    }

    void goNext() {
        startFragment(CenterAuthAPIUserMeResultFragment.class, args, R.string.fragment_id_api_call_userme);
    }

    @Override
    public void onResume() {
        super.onResume();
        showSetting(false);
    }
}