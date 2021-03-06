package com.lashgo.mobile.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.lashgo.mobile.LashgoConstants;
import com.lashgo.model.dto.CheckDto;

import java.util.Calendar;

/**
 * Created by Eugene on 19.08.2014.
 */
public final class LashGoUtils {

    private LashGoUtils() {

    }

    public static LashgoConstants.CheckState getCheckState(CheckDto checkDto) {
        if (checkDto != null) {
            Calendar checkActiveCalendar = Calendar.getInstance();
            Calendar checkVoteCalendar = Calendar.getInstance();
            checkActiveCalendar.setTime(checkDto.getStartDate());
            checkVoteCalendar.setTime(checkDto.getStartDate());
            checkActiveCalendar.add(Calendar.HOUR_OF_DAY, checkDto.getDuration());
            checkVoteCalendar.add(Calendar.HOUR_OF_DAY, checkDto.getDuration() + checkDto.getVoteDuration());
            if ((checkActiveCalendar.getTimeInMillis() > System.currentTimeMillis())) {
                return LashgoConstants.CheckState.ACTIVE;
            } else if (checkVoteCalendar.getTimeInMillis() > System.currentTimeMillis()) {
                return LashgoConstants.CheckState.VOTE;
            } else {
                return LashgoConstants.CheckState.FINISHED;
            }
        }
        return null;
    }

    public static String getUserAvatarUrl(String avatar) {
        if (!TextUtils.isEmpty(avatar)) {
            if (avatar.contains("http://") || avatar.contains("https://")) {
                return avatar;
            }
            return PhotoUtils.getFullPhotoUrl(avatar);
        }
        return avatar;
    }

    public static Fragment findFragmentByPosition(FragmentManager fragmentManager, ViewPager viewPager, FragmentPagerAdapter pagerAdapter, int position) {
        if (viewPager == null) {
            throw new IllegalArgumentException("ViewPager can't be null");
        }
        if (pagerAdapter == null) {
            throw new IllegalArgumentException("PagerAdapter can't be null");
        }
        if (fragmentManager == null) {
            return null;
        }
        return fragmentManager.findFragmentByTag(
                "android:switcher:" + viewPager.getId() + ":"
                        + pagerAdapter.getItemId(position));
    }
}
