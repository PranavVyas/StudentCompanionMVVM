package com.vyas.pranav.studentcompanion.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.evrencoskun.tableview.TableView;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.adapters.TimetableTableAdapter;
import com.vyas.pranav.studentcompanion.data.timetabledatabase.TimetableEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.viewmodels.TimetableViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.vyas.pranav.studentcompanion.utils.Constants.EXTRA_TIMETABLE_DAY;
import static com.vyas.pranav.studentcompanion.utils.Constants.EXTRA_TIMETABLE_DAY_KEY;

public class TimetableFragment extends Fragment {
    @BindView(R.id.table_timetable_fragment_main)
    TableView tableTimetable;
    @BindView(R.id.switch_fragment_timetable_productive_view)
    LabeledSwitch switchProductiveView;
    @BindView(R.id.viewpager_fragment_timetable_day_switcher)
    ViewPager viewPagerDaySwitcher;

    private TimetableTableAdapter mAdapter;
    private TimetableViewModel timetableViewModel;
    private int lecturesPerDay;
    private int currentPage = 0;
    private boolean isProductiveViewOn = false;

    public TimetableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new TimetableTableAdapter(getContext());
        timetableViewModel = ViewModelProviders.of(getActivity()).get(TimetableViewModel.class);
        lecturesPerDay = timetableViewModel.getLecturesPerDay();
        currentPage = timetableViewModel.getCurrentPage();
        isProductiveViewOn = timetableViewModel.isProductiveViewOn();

        switchProductiveView.setOn(isProductiveViewOn);
        final LiveData<List<TimetableEntry>> timetableEntriesLiveData = timetableViewModel.getTimetableEntries();
        timetableEntriesLiveData.observe(this, new Observer<List<TimetableEntry>>() {
            @Override
            public void onChanged(List<TimetableEntry> timetableEntries) {
                timetableEntriesLiveData.removeObserver(this);
                List<String> Monday = new ArrayList<>();
                List<String> Tuesday = new ArrayList<>();
                List<String> Wednesday = new ArrayList<>();
                List<String> Thursday = new ArrayList<>();
                List<String> Friday = new ArrayList<>();
                List<String> weekDays = new ArrayList<>(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

                for (int i = 0; i < timetableEntries.size(); i++) {
                    int day = i / lecturesPerDay;
                    int lecture = i % lecturesPerDay;

                    switch (day) {
                        case 0:
                            //Monday
                            Monday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 1:
                            //Tuesday
                            Tuesday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 2:
                            //Wednesday
                            Wednesday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 3:
                            //Thursday
                            Thursday.add(lecture, timetableEntries.get(i).getSubName());
                            break;

                        case 4:
                            //Friday
                            Friday.add(lecture, timetableEntries.get(i).getSubName());
                            break;
                    }

                }
                List<List<String>> daysLectures = new ArrayList<>(Arrays.asList(
                        Monday, Tuesday, Wednesday, Thursday, Friday
                ));
                List<String> lectureNo = getColumnHeaders(lecturesPerDay);
                tableTimetable.setAdapter(mAdapter);
                mAdapter.setAllItems(lectureNo, weekDays, daysLectures);
                loadDataInViewPager(daysLectures, lectureNo);
                refreshView();
            }
        });

        switchProductiveView.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                isProductiveViewOn = isOn;
                refreshView();
                timetableViewModel.setProductiveViewOn(isOn);
            }
        });
    }

    private void loadDataInViewPager(List<List<String>> daysLectures, List<String> lectureNo) {
        viewPagerDaySwitcher.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                timetableViewModel.setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerDaySwitcher.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                TimetableDayFragment dayFrag = new TimetableDayFragment();
                switch (position) {
                    case 0:
                        Bundle mondayBundle = new Bundle();
                        mondayBundle.putStringArrayList(EXTRA_TIMETABLE_DAY_KEY, (ArrayList<String>) daysLectures.get(0));
                        mondayBundle.putString(EXTRA_TIMETABLE_DAY, "Monday");
                        dayFrag.setArguments(mondayBundle);
                        return dayFrag;

                    case 1:
                        Bundle tuesdayBundle = new Bundle();
                        tuesdayBundle.putStringArrayList(EXTRA_TIMETABLE_DAY_KEY, (ArrayList<String>) daysLectures.get(1));
                        tuesdayBundle.putString(EXTRA_TIMETABLE_DAY, "Tuesday");
                        dayFrag.setArguments(tuesdayBundle);
                        return dayFrag;

                    case 2:
                        Bundle wednesdayBundle = new Bundle();
                        wednesdayBundle.putStringArrayList(EXTRA_TIMETABLE_DAY_KEY, (ArrayList<String>) daysLectures.get(2));
                        wednesdayBundle.putString(EXTRA_TIMETABLE_DAY, "Wednesday");
                        dayFrag.setArguments(wednesdayBundle);
                        return dayFrag;

                    case 3:
                        Bundle thursdayBundle = new Bundle();
                        thursdayBundle.putStringArrayList(EXTRA_TIMETABLE_DAY_KEY, (ArrayList<String>) daysLectures.get(3));
                        thursdayBundle.putString(EXTRA_TIMETABLE_DAY, "Thursday");
                        dayFrag.setArguments(thursdayBundle);
                        return dayFrag;

                    case 4:
                        Bundle fridayBundle = new Bundle();
                        fridayBundle.putStringArrayList(EXTRA_TIMETABLE_DAY_KEY, (ArrayList<String>) daysLectures.get(4));
                        fridayBundle.putString(EXTRA_TIMETABLE_DAY, "Friday");
                        dayFrag.setArguments(fridayBundle);
                        return dayFrag;
                }
                return null;

            }

            @Override
            public int getCount() {
                return 5;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Monday";

                    case 1:
                        return "Tuesday";

                    case 2:
                        return "Wednesday";

                    case 3:
                        return "Thursday";

                    case 4:
                        return "Friday";

                    default:
                        return "Default Day";
                }
            }
        });
    }

    private List<String> getColumnHeaders(int lecturesPerDay) {
        List<String> columnHeader = new ArrayList<>();
        for (int i = 0; i < lecturesPerDay; i++) {
            String header = "Lecture" + (i + 1);
            int startingTime = timetableViewModel.getStartingTimeOfLecture(i);
            int endingTime = timetableViewModel.getEndingTimeOfLecture(i);
            String start = ConverterUtils.convertTimeIntInString(startingTime);
            String end = ConverterUtils.convertTimeIntInString(endingTime);
            header = header + "\n" + start + "\nTo\n" + end;
            columnHeader.add(header);
        }
        return columnHeader;
    }

    private void refreshView() {
        if (isProductiveViewOn) {
            viewPagerDaySwitcher.setVisibility(View.GONE);
            tableTimetable.setVisibility(View.VISIBLE);
        } else {
            viewPagerDaySwitcher.setVisibility(View.VISIBLE);
            tableTimetable.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_timetable_frag_sem_info)
    void semInfoClicked() {
        BottomSheetDialog mDialog = new BottomSheetDialog(getContext());
        mDialog.setContentView(R.layout.item_holder_alert_dialog_sem_info);
        mDialog.show();
        TextView tvSemNo = mDialog.findViewById(R.id.tv_holder_dialog_sem_info_sem);
        TextView tvStartDate = mDialog.findViewById(R.id.tv_holder_dialog_sem_info_start_date);
        TextView tvEndDate = mDialog.findViewById(R.id.tv_holder_dialog_sem_info_end_date);
        tvSemNo.setText("" + timetableViewModel.getSemInfo());
        tvStartDate.setText("" + timetableViewModel.getStartDate());
        tvEndDate.setText("" + timetableViewModel.getEndDate());
        Button btnOk = mDialog.findViewById(R.id.btn_holder_dialog_sem_info_ok);
        btnOk.setOnClickListener(view -> mDialog.dismiss());
    }

}
