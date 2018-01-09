package com.shotang.shotang.shotang.groupdeal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.TextView;

import com.shotang.shotang.shotang.R;
import com.shotang.shotang.shotang.RetrofitApiClient;
import com.shotang.shotang.shotang.analytics.EventSender;
import com.shotang.shotang.shotang.catalog.BaseActivity;
import com.shotang.shotang.shotang.groupdeal.model.GroupDealResponse;
import com.shotang.shotang.shotang.groupdeal.network.IGroupDealService;
import com.shotang.shotang.shotang.misc.APIUrl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shotang.shotang.shotang.analytics.EventSender.GROUP_DEAL_LIST;

/**
 * Created by ajay on 4/8/17.
 */

public class GroupDealMainActivity extends BaseActivity {

    private static final String TAG = "GroupDealMainActivity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private GroupDealResponse groupDealResponse;
    private LinearLayout progressbarContainer;

    private int[] tabIcons = {
            R.drawable.favorite,
            R.drawable.add_favorite,
            R.drawable.ic_watchlist_selected
    };


    public static Intent getLaunchIntent(Context context) {
        Intent intent = null;
        String newUri = null;
        try {
            URI oldUri = new URI("https://shotang.com/city_groupdeal/");
           // URI resolved = oldUri.resolve(productId);
            newUri = oldUri.toString();
            intent = new Intent(context, GroupDealMainActivity.class);
            intent.setData(Uri.parse(newUri));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return intent;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdeal_main_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getProductDetails();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventSender.sendGAScreenEvent(GROUP_DEAL_LIST);
        progressbarContainer=(LinearLayout)findViewById(R.id.progressbar_container);
        progressbarContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        adapter = new ViewPagerAdapter(groupDealResponse,getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);

        // Iterate over all tabs and set the custom view
 /*       for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
*/
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(8, 0, 8, 0);
            tab.setPadding(0,0,0,0);

            tab.requestLayout();

            TabLayout.Tab tab1 = tabLayout.getTabAt(i);
            tab1.setCustomView(adapter.getTabView(i));

        }


        // setupTabIcons();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_base_activity, menu);
        MenuItem profile = menu.findItem(R.id.action_person_profile);
        profile.setVisible(false);
        return true;

    }



    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private GroupDealResponse groupDealResponse = null;
        GroupDealMainActivity activity = null;

        public ViewPagerAdapter(GroupDealResponse groupDealResponse,
                FragmentManager manager,GroupDealMainActivity activity) {
            super(manager);
            this.groupDealResponse = groupDealResponse;
            this.activity = activity;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0 :
                {
                    fragment = GroupDealFragment.newIntance(GroupDealResponse.GroupDealStatus.LIVE, groupDealResponse);
                    break;
                }
                case 1:
                {
                    fragment =  GroupDealFragment.newIntance(GroupDealResponse.GroupDealStatus.UPCOMING,groupDealResponse);
                    break;
                }
                case 2:
                {
                    fragment = GroupDealFragment.newIntance(GroupDealResponse.GroupDealStatus.CLOSED,groupDealResponse);
                    break;
                }
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            return GroupDealResponse.GroupDealStatus.values()[position].name();
        }

        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View v = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView2);
            tv.setText(getPageTitle(position));
            ImageView img = (ImageView) v.findViewById(R.id.imageView);

            switch (position){
                case 0 :
                {
                    img.setBackgroundResource(R.drawable.green_dot);
                    break;
                }
                case 1:
                {
                    img.setBackgroundResource(R.drawable.light_blue_dot);
                    break;
                }
                case 2:
                {
                    img.setBackgroundResource(R.drawable.deep_blue_dot);
                    break;
                }
            }
            return v;
        }
    }



    private void getProductDetails() {

        IGroupDealService apiService = RetrofitApiClient.changeApiBaseUrl(APIUrl.baseApi).build().create(IGroupDealService.class);
        Call<GroupDealResponse> callGroupDealResponse=apiService.getDetailsGroupDeal();
        callGroupDealResponse.enqueue(new Callback<GroupDealResponse>() {
            @Override
            public void onResponse(Call<GroupDealResponse> call, Response<GroupDealResponse> responseGroupDeal) {
                if (responseGroupDeal!=null && responseGroupDeal.body()!=null){
                    progressbarContainer.setVisibility(View.GONE);
                    groupDealResponse=responseGroupDeal.body();
                    setupViewPager();

                }
            }

            @Override
            public void onFailure(Call<GroupDealResponse> call, Throwable t) {
                t.getStackTrace();
                Toast.makeText(GroupDealMainActivity.this,"Server Problem",Toast.LENGTH_LONG).show();
                progressbarContainer.setVisibility(View.GONE);

            }
        });


    }
}
