package edu.uiuc.cs427app;

import android.app.Activity;
import android.content.Context;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class ThemeManager {

    private static final String DEFAULT_THEME = "Light Mode";
    private static final DatabaseReference USERS_REF = FirebaseDatabase.getInstance().getReference("users");

    public interface ThemeLoadCallback {
        void onThemeLoaded();
    }

    // Load and apply theme from Firebase with callback
    public static void loadTheme(String username, Activity activity, ThemeLoadCallback listener) {
        USERS_REF.child(username).child("ui").child("themes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                applyTheme(activity, snapshot.getValue(String.class), DEFAULT_THEME);
                listener.onThemeLoaded();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                applyTheme(activity, DEFAULT_THEME, DEFAULT_THEME);
                listener.onThemeLoaded();
            }
        });
    }

    // Save selected theme to Firebase
    public static void saveTheme(String username, String theme) {
        USERS_REF.child(username).child("ui").child("themes").setValue(theme);
    }

    // Apply the theme, with a fallback default
    private static void applyTheme(Activity activity, String theme, String fallbackTheme) {
        switch (theme != null ? theme : fallbackTheme) {
            case "Dark Mode":
                activity.setTheme(R.style.AppTheme_Dark);
                break;
            case "Red Theme":
                activity.setTheme(R.style.AppTheme_Red);
                break;
            case "Blue Theme":
                activity.setTheme(R.style.AppTheme_Blue);
                break;
            case "Light Mode":
            default:
                activity.setTheme(R.style.AppTheme_Light);
        }
    }
}



