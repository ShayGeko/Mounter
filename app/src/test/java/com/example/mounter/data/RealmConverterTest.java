package com.example.mounter.data;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.powermock.modules.junit4.rule.PowerMockRule;
import io.realm.RealmList;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;


class RealmConverterTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    void toRealmList() {
        LatLng coords = mock(LatLng.class);
        RealmList<Double> expected = new RealmList<>(coords.latitude, coords.longitude);

        RealmList<Double> actual = RealmConverter.toRealmList(coords);

        assertThat(actual, equalTo(expected));
    }

    @Test
    void toLatLngSuccess() {

        LatLng expected = mock(LatLng.class);
        RealmList<Double> list = new RealmList<>(expected.latitude, expected.longitude);

        LatLng actual = RealmConverter.toLatLng(list);

        assertThat(actual, equalTo(expected));
    }
    @Test
    void toLatLngShouldReturnNullOnInvalidInput() {

        RealmList<Double> list = new RealmList<Double>(2.0);

        LatLng actual = RealmConverter.toLatLng(list);

        assertNull(actual);
    }
}