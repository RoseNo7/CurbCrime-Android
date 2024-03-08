package com.roseno.curbcrime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roseno.curbcrime.R;
import com.roseno.curbcrime.manager.AlarmManager;
import com.roseno.curbcrime.manager.SharedPreferenceManager;
import com.roseno.curbcrime.model.AlarmSound;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private final String TAG = "AlarmAdapter";

    private final List<AlarmSound> alarmSounds;
    private int selectedSound;

    public AlarmAdapter(List<AlarmSound> alarmSounds, int selectedSound) {
        this.alarmSounds = alarmSounds;
        this.selectedSound = selectedSound;
    }

    @NonNull
    @Override
    public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_alarm_sound_data, parent, false);

        return new AlarmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
        String name = alarmSounds.get(position).getName();

        holder.mNameTextView.setText(name);
        holder.mRadioButton.setChecked(position == selectedSound);
    }

    @Override
    public int getItemCount() {
        return alarmSounds.size();
    }


    public class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RadioButton mRadioButton;
        TextView mNameTextView;

        public AlarmHolder(@NonNull View itemView) {
            super(itemView);

            mRadioButton = itemView.findViewById(R.id.radioButton_item_alarm_sound_data);
            mNameTextView = itemView.findViewById(R.id.textView_item_alarm_sound_data);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final Context context = v.getContext();
            final int position = getAdapterPosition();

            selectedSound = position;

            String resName = alarmSounds.get(position).getUri().getLastPathSegment();
            int resId = context.getResources().getIdentifier(resName, "raw", context.getPackageName());

            // 설정 저장
            SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(context);
            sharedPreferenceManager.setInt(AlarmManager.PREFERENCE_KEY_SELECTED_SOUND, resId);

            AlarmManager.start(context);

            notifyDataSetChanged();
        }
    }
}
