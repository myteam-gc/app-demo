package com.hnhy.ui.dialogchooseinfo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnhy.ui.R;

import java.util.ArrayList;
import java.util.List;

public class DialogChooseInfoMultiple extends DialogFragment {
    public static final String TAG = "DialogChooseInfo";
    static final String DATA = "data";
    public OnChooseInfoClickListener mOnChooseInfoClickListener;
    private TextView mTvTile, mTvSure;
    private LinearLayout mLlSearch;
    private EditText mEtSearch;
    private TextView mTvSearch;
    private RecyclerView mRlvChooseInfo;
    private List<BeanChooseOption> mBeanChooseOptionList;
    private BeanChooseInfo mLocalData;
    private String mHttpUrl;
    private String mTitle;
    private LinearLayout mLlContainer;
    private AdapterChooseInfo mAdapterChooseInfo;
    private List<BeanChooseOption> mSelectDatas;//选中的数据


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable());
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_AppCompat_Dialog_Alert);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_info, container, false);
        init(view);
        return view;
    }

    public void setSelectValue(List<BeanChooseOption> selectValues) {
        this.mSelectDatas = selectValues;
    }


    private void init(View view) {
        mBeanChooseOptionList = new ArrayList<>();
        mTvTile = view.findViewById(R.id.tv_title);
        mTvSure = view.findViewById(R.id.tv_sure);
        mLlSearch = view.findViewById(R.id.ll_search);
        mEtSearch = view.findViewById(R.id.et_search);
        mTvSearch = view.findViewById(R.id.tv_search);
        mRlvChooseInfo = view.findViewById(R.id.rlv_choose_info);
        mTvSearch = view.findViewById(R.id.tv_search);
        mTvSure.setVisibility(View.VISIBLE);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData();
            }
        });
        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChooseInfoClickListener != null) {
                    mOnChooseInfoClickListener.onChoose(mSelectDatas != null && mSelectDatas.size() > 0, mSelectDatas);
                    dismiss();
                }
            }
        });
        getBundleInfo();
        initView();
    }

    public void setOnChooseInfoClickListener(OnChooseInfoClickListener listener) {
        mOnChooseInfoClickListener = listener;
    }

    private void getBundleInfo() {
        Bundle bundle = getArguments();
        mLocalData = bundle.getParcelable(DATA);
        mTitle = mLocalData.title;
    }

    private void initView() {
        setTitle();
        initRecyclerView();
        setContent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTile.setText(mTitle);
        }
    }

    private void setContent() {
        if (mLocalData.beanChooseOptionList != null) {
            if (mLocalData.beanChooseOptionList.size() > 10) {
                mLlSearch.setVisibility(View.VISIBLE);
            }
            getDataFromLocal();
        }
    }

    public void initRecyclerView() {
        mAdapterChooseInfo = new AdapterChooseInfo(getActivity(), mBeanChooseOptionList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRlvChooseInfo.setLayoutManager(manager);
        mRlvChooseInfo.setAdapter(mAdapterChooseInfo);
        MultiItemDivider itemDivider = new MultiItemDivider(getActivity(), MultiItemDivider.VERTICAL_LIST, R.drawable.shape_divider);
        itemDivider.setDividerMode(MultiItemDivider.INSIDE);
        mRlvChooseInfo.addItemDecoration(itemDivider);
        mAdapterChooseInfo.setOnChooseInfoClickListener(new AdapterChooseInfo.OnChooseInfoClickListener() {
            @Override
            public void click(int position, BeanChooseOption beanChooseInfo) {//处理选择监听
                if (beanChooseInfo.selected) {
                    beanChooseInfo.selected = false;
                    mSelectDatas.remove(beanChooseInfo);
                } else {
                    beanChooseInfo.selected = true;
                    mSelectDatas.add(beanChooseInfo);
                }
                mAdapterChooseInfo.notifyDataSetChanged();
            }
        });
    }

    private void getDataFromLocal() {
        mBeanChooseOptionList.addAll(mLocalData.beanChooseOptionList);
        for (int i = 0; i < mBeanChooseOptionList.size(); i++) {
            if (mSelectDatas != null && mSelectDatas.size() > 0) {
                if (hasContain(mBeanChooseOptionList.get(i))) {
                    mBeanChooseOptionList.get(i).selected = true;
                }
            }
        }
        mAdapterChooseInfo.notifyDataSetChanged();
    }

    private boolean hasContain(BeanChooseOption bean) {
        for (BeanChooseOption beanChooseOption : mSelectDatas) {
            if (bean.equals(beanChooseOption)) {
                return true;
            }
        }
        return false;
    }

    private void filterData() {
        mBeanChooseOptionList.clear();
        mBeanChooseOptionList.addAll(mLocalData.beanChooseOptionList);
        String searchStr = mEtSearch.getText().toString().trim();
        for (int i = 0; i < mLocalData.beanChooseOptionList.size(); i++) {
            if (!mLocalData.beanChooseOptionList.get(i).key.contains(searchStr)) {
                mBeanChooseOptionList.remove(mLocalData.beanChooseOptionList.get(i));
            }
        }
        mAdapterChooseInfo.notifyDataSetChanged();
    }

    public interface OnChooseInfoClickListener {
        void onChoose(boolean hasSelected, List<BeanChooseOption> beanChooseInfos);
    }
}
