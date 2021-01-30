package com.networknt.kafka;

import com.networknt.server.ShutdownHookProvider;

public class UserQueryShutdownHook implements ShutdownHookProvider {
    @Override
    public void onShutdown() {
        if(UserQueryStartupHook.streams != null) UserQueryStartupHook.streams.close();
    }
}
