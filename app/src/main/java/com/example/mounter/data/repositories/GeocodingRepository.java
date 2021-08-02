package com.example.mounter.data.repositories;

import android.location.Address;
import android.location.Geocoder;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.example.mounter.Mounter;
import com.example.mounter.data.model.Response;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.Observable;

import io.reactivex.Single;

public class GeocodingRepository extends MounterBaseRepository{
    private static final String TAG = "DirectionsRepository";
    private final MutableLiveData<Response<LatLng>> latLngResponse;

    private final LatLng BCSouthEast = new LatLng(48.30, -139.06);
    private final LatLng BCNorthWest = new LatLng(60.00, -114.03);

    public GeocodingRepository(){
        latLngResponse = new MutableLiveData<>(Response.Pending());
    }
    public GeocodingRepository(Mounter app){
        super(app);
        latLngResponse = new MutableLiveData<>(Response.Pending());
    }
    public Single<LatLng> getLatLng(String address){

        return Single.create(emitter -> {
            try {
                Geocoder geocoder = new Geocoder(app.getApplicationContext(), new Locale("en_CA"));
                // would throw an exception if geocoding api is down or
                // if the returned list is empty
                LatLng latLng = getLatLng(geocoder, address);
                emitter.onSuccess(latLng);
            } catch (Exception e) {
                // nothing was found or api is down
                emitter.onError(e);
            }
        });
    }


    public Single<Pair<LatLng, LatLng>> getLatLngPair(String address1, String address2){

        return Single.create(emitter -> {
            try {
                // I've spent so much time trying to find this Locale.CANADA
                // and for some reason it still doesn't give preference to canadian addresses
                // so, for example, it would still think that by Surrey the user means a town in UK
                Geocoder geocoder = new Geocoder(app.getApplicationContext(), Locale.CANADA);
                // a workaround that i've found is to enforce the region by latlng bounds
                // would throw an exception if geocoding api is down or
                // if the returned list is empty
                Address addr1 = geocoder.getFromLocationName(
                        address1,
                        1,
                        BCSouthEast.latitude,
                        BCSouthEast.longitude,
                        BCNorthWest.latitude,
                        BCNorthWest.longitude).get(0);
                LatLng latLng1 = new LatLng(addr1.getLatitude(), addr1.getLongitude());

                Address addr2 = geocoder.getFromLocationName(
                        address2,
                        1,
                        BCSouthEast.latitude,
                        BCSouthEast.longitude,
                        BCNorthWest.latitude,
                        BCNorthWest.longitude).get(0);
                LatLng latLng2 = new LatLng(addr2.getLatitude(), addr2.getLongitude());


                emitter.onSuccess(new Pair<>(latLng1, latLng2));
            } catch (Exception e) {
                // nothing was found or api is down
                emitter.onError(e);
            }
        });
    }

    @NotNull
    private LatLng getLatLng(Geocoder geocoder, String address) throws IOException {
        Address addr = geocoder.getFromLocationName(
                address,
                1,
                BCSouthEast.latitude,
                BCSouthEast.longitude,
                BCNorthWest.latitude,
                BCNorthWest.longitude).get(0);
        return new LatLng(addr.getLatitude(), addr.getLongitude());
    }
}
