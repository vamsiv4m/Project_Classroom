package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyCalender extends DialogFragment {
    Calendar calendar=Calendar.getInstance();

    // Create an interface for onclick of calender
    public interface OnCalenderOkClickListener{
        void onClick(int year,int month,int day);
    }

    public OnCalenderOkClickListener onCalenderOkClickListener;
    public void setOnCalenderOkClickListener(OnCalenderOkClickListener onCalenderOkClickListener){
        this.onCalenderOkClickListener = onCalenderOkClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),((view, year, month, dayOfMonth) -> {
            onCalenderOkClickListener.onClick(year,month,dayOfMonth);
        }),calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setDate(int year,int month,int day){
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);
    }
    public String getDate(){
        return DateFormat.format("dd-MM-yyyy",calendar).toString();
    }
    public String getMonth(){
        return DateFormat.format("MM-yyy",calendar).toString();
    }
    public String getYear(){
        return DateFormat.format("yyyy",calendar).toString();
    }
    public int getLastDate(){
        return calendar.getActualMaximum(Calendar.DATE);
    }
    public long getTimeinMilli(){
        return calendar.getTimeInMillis();
    }
}
