package com.hnhy.ui.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnChangeLisener;
import com.codbking.widget.OnSureLisener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UiUtils {

    public static void showDateSelectDialog(final Context mContext, final TextView tvDate, final String pattern) {
        final SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        Date curDate;
        if (TextUtils.isEmpty(tvDate.getText().toString().trim())) {
            curDate = new Date();
        } else {
            curDate = str2Date(tvDate.getText().toString().trim(), pattern);
        }
        DatePickDialog slDialog = new DatePickDialog(mContext);
        slDialog.setYearLimt(5);

        slDialog.setMessageFormat(pattern);
        switch (pattern) {
            case "yyyy-MM-dd":
                slDialog.setType(com.codbking.widget.bean.DateType.TYPE_YMD);
                slDialog.setTitle("选择日期");
                break;
            case "yyyy-MM-dd HH:mm":
                slDialog.setType(com.codbking.widget.bean.DateType.TYPE_YMDHM);
                slDialog.setTitle("选择时间");
                break;
        }
        slDialog.setStartDate(curDate);
        slDialog.setOnChangeLisener(new OnChangeLisener() {
            @Override
            public void onChanged(Date date) {

            }
        });
        slDialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                switch (pattern) {
                    case "yyyy-MM-dd":
                        if (isBeforeDate(date)) {
                            tvDate.setText(format.format(date));
                        } else {
                            Toast.makeText(mContext, "不能选择当前时间之后的时间", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "yyyy-MM-dd HH:mm":
                        if (isBeforeTime(date)) {
                            tvDate.setText(format.format(date));
                        } else {
                            Toast.makeText(mContext, "不能选择当前时间之后的时间", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        });
        slDialog.show();
    }

    /**
     * 选择日期  格式为：yyyy-MM-dd
     *
     * @param mContext
     * @param tvDate
     */
    public static void showDateSelectDialog(Context mContext, final TextView tvDate) {
        showDateSelectDialog(mContext, tvDate, "yyyy-MM-dd");
    }

    private static boolean isBeforeDate(Date date) {
        Date curDate = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return isBeforeTime(date);
    }

    /**
     * 日期是否在当前之前
     *
     * @param date
     * @return
     */
    private static boolean isBeforeTime(Date date) {
        Date curDate = new Date();
        return date.getTime() <= curDate.getTime();
    }

    /**
     * 时间字符串转为Date
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    private static Date str2Date(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            date = new Date();
        }
        return date;
    }
}
