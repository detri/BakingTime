package com.detri.bakingtime.network;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class InitialRequestIdlingResource {
    private static final String RESOURCE_NAME = "Initial Request";
    private static CountingIdlingResource mCountingIdlingResource = new CountingIdlingResource(RESOURCE_NAME);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
