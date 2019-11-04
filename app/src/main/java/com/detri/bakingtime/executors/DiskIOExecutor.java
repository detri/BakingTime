package com.detri.bakingtime.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOExecutor {
    private Executor diskIoExecutor;
    private static DiskIOExecutor instance;

    private DiskIOExecutor() {
        diskIoExecutor = Executors.newSingleThreadExecutor();
    }

    public static DiskIOExecutor getInstance() {
        if (instance == null) {
            instance = new DiskIOExecutor();
        }
        return instance;
    }

    public void execute(Runnable runnable) {
        diskIoExecutor.execute(runnable);
    }
}
