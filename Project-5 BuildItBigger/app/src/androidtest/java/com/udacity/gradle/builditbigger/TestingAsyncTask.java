package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;
import org.junit.Test;
import static junit.framework.Assert.assertNotNull;

public class TestingAsyncTask {
    String s;
    EndpointsAsyncTask endpointsAsyncTask;
    @Test
    public void stringTest(){
        endpointsAsyncTask = new EndpointsAsyncTask();
        endpointsAsyncTask.execute(InstrumentationRegistry.getTargetContext());
        try {
            s = endpointsAsyncTask.get();
        } catch (Exception e){
            e.printStackTrace();
        }
        assertNotNull(s);
    }
}
