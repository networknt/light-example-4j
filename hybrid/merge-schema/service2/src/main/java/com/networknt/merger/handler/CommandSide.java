
package com.networknt.merger.handler;

import com.networknt.utility.NioUtils;
import com.networknt.rpc.Handler;
import com.networknt.rpc.router.ServiceHandler;
import java.nio.ByteBuffer;

@ServiceHandler(id="lightapi.net/service2/command/0.1.0")
public class CommandSide implements Handler {
    @Override
    public ByteBuffer handle(Object input)  {
        return NioUtils.toByteBuffer("{\"message\":\"Hello World!\"}");
    }
}
