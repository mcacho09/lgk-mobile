package app.logistikappbeta.com.logistikapp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.LinkedList;

import app.logistikappbeta.com.logistikapp.Fragments.ReporteHistoricoTabs.RHDosFragment;
import app.logistikappbeta.com.logistikapp.Fragments.ReporteHistoricoTabs.RHTresFragment;
import app.logistikappbeta.com.logistikapp.Fragments.ReporteHistoricoTabs.RHUnoFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReporteHistoricoActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.vPager) ViewPager vPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_historico);
        ButterKnife.bind(this);
        setTitle("Reporte histórico");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        String travel = getIntent().getStringExtra("travel");
        String stores = getIntent().getStringExtra("stores");

        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        RHUnoFragment f1 = new RHUnoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("travel", travel);
        f1.setArguments(bundle);
        adapter.addFragment(f1, "UNO");
        RHDosFragment f2 = new RHDosFragment();
        bundle = new Bundle();
        bundle.putString("stores", stores);
        f2.setArguments(bundle);
        adapter.addFragment(f2, "DOS");
        RHTresFragment f3 = new RHTresFragment();
        f3.setArguments(bundle);
        adapter.addFragment(f3, "TRES");
        vPager.setAdapter(adapter);
        vPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(vPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_local_shipping);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_place_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_public);

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);

        toolbar.setNavigationOnClickListener(this);

        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(getResources().getColor(R.color.colorBlue), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        tab.getIcon().setColorFilter(getResources().getColor(R.color.colorGray), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    protected class VPAdapter extends FragmentPagerAdapter{

        private LinkedList<Fragment> listFragment = new LinkedList<>();
        private LinkedList<String> listTitles = new LinkedList<>();

        public VPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        public void addFragment(Fragment fragment, String title){
            listFragment.add(fragment);
            listTitles.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return null;
        }

    }
}
