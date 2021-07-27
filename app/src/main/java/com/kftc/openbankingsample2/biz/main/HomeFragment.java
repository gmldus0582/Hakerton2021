package com.kftc.openbankingsample2.biz.main;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kftc.openbankingsample2.R;
import com.kftc.openbankingsample2.biz.center_auth.AbstractCenterAuthMainFragment;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthConst;
import com.kftc.openbankingsample2.biz.center_auth.CenterAuthHomeFragment;
import com.kftc.openbankingsample2.biz.center_auth.auth.CenterAuthFragment;
import com.kftc.openbankingsample2.biz.self_auth.SelfAuthHomeFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;
import com.kftc.openbankingsample2.BuildConfig;
import com.kftc.openbankingsample2.common.Scope;
import com.kftc.openbankingsample2.common.application.AppData;
import com.kftc.openbankingsample2.common.data.AccessToken;
import com.kftc.openbankingsample2.common.util.Utils;

/**
 * 센터인증과 자체인증을 선택하는 앱의 메인화면
 */
public class HomeFragment extends AbstractCenterAuthMainFragment {

    // context
    private Context context;

    // view
    private View view;

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

    private void initView() {
        // 로그인
        // access_token : 가장 최근 토큰으로 기본 설정
        EditText etToken = view.findViewById(R.id.etToken);
        etToken.setText(AppData.getCenterAuthAccessToken(Scope.INQUIRY));

        // access_token : 기존 토큰에서 선택
        view.findViewById(R.id.btnSelectToken).setOnClickListener(v -> showTokenDialog(etToken, Scope.INQUIRY));


        //계좌등록
        view.findViewById(R.id.btnAuthToken).setOnClickListener(v -> startFragment(CenterAuthFragment.class, args, R.string.fragment_id_center_auth));

        // 자체인증
//        view.findViewById(R.id.btnSelfAuth).setOnClickListener(v -> startFragment(SelfAuthHomeFragment.class, args, R.string.fragment_id_self));

        // 하단 버전표시
        try {
            Date date = new Date(BuildConfig.TIMESTAMP);
            String dateStr = new SimpleDateFormat("yyyyMMdd", Locale.KOREA).format(date);

            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            ((TextView) view.findViewById(R.id.tvVersion)).setText(String.format("Ver: %s_%s", String.valueOf(pi.versionName), dateStr));
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        showSetting(false);
    }
}
