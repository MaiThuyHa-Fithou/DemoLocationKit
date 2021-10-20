package com.mtha.demolocationkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;


public class MainActivity extends AppCompatActivity {

    //khai bao cac doi tuong dieu khien
    TextView tvResult;
    Button btnCheckLocation;
    LocationRequest mLocationRequest;
    SettingsClient settingsClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
        btnCheckLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                mLocationRequest = new LocationRequest();
                builder.addLocationRequest(mLocationRequest);
                LocationSettingsRequest locationSettingsRequest = builder.build();
                settingsClient = LocationServices.getSettingsClient(MainActivity.this);
                settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Toast.makeText(MainActivity.this,"Location setting is successful",Toast.LENGTH_LONG).show();
                        tvResult.setText("Location setting is successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        int statusCode = ((ApiException)e).getStatusCode();
                        switch (statusCode){
                            case LocationSettingsStatusCodes
                                    .RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this,0);
                                }catch (IntentSender.SendIntentException sie){
                                    Log.w("Setting Location", sie.getMessage());
                                }
                                break;
                        }
                    }
                });
            }
        });
    }

    private void getViews(){
        tvResult = findViewById(R.id.tv_result);
        btnCheckLocation = findViewById(R.id.btnCheckLocation);
    }

}