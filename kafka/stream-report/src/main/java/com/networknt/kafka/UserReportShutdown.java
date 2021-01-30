package com.networknt.kafka;

import com.networknt.server.ShutdownHookProvider;

public class UserReportShutdown implements ShutdownHookProvider {
    @Override
    public void onShutdown() {
        if(UserReportStartup.streams != null) UserReportStartup.streams.close();
    }
}
