package com.example.mounter.directions;

import androidx.test.core.app.ApplicationProvider;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


// mocking Realm using Realm guidelines
// https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleActivityTest.java
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@SuppressStaticInitializationFor("io.realm.internal.Util")
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class, RealmResults.class, RealmCore.class, RealmLog.class})
class RideDetailsActivityTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private Realm mockRealm;


    // setup mostly taken from Realm guidelines
    // https://github.com/realm/realm-java/blob/master/examples/unitTestExample/src/test/java/io/realm/examples/unittesting/ExampleActivityTest.java
    @Before
    public void setup() throws Exception {
        // Setup Realm to be mocked. The order of these matters
        mockStatic(RealmCore.class);
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmConfiguration.class);
        Realm.init(ApplicationProvider.getApplicationContext());

        final Realm mockRealm = mock(Realm.class);
        final RealmConfiguration mockRealmConfig = mock(RealmConfiguration.class);


        doNothing().when(RealmCore.class);
        RealmCore.loadLibrary(any(Context.class));

        whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);

        // Anytime getInstance is called with any configuration, then return the mockRealm
        when(Realm.getDefaultInstance()).thenReturn(mockRealm);
    }

    @Test
    void onCreate() {
    }

    @Test
    void onDestroy() {
    }

    @Test
    void onMapReady() {
    }

    @Test
    void buildDirectionsUrl() {

    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return mock(RealmResults.class);
    }
}