package com.networknt.sse;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HtmlClientHandler implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");

        String html = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>SSE Client</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 40px; }
                    #events { border: 1px solid #ccc; padding: 20px; height: 400px; overflow-y: scroll; }
                    .event { margin: 10px 0; padding: 10px; border-left: 4px solid #007cba; }
                    .timestamp { background-color: #e7f3ff; }
                    .connected { background-color: #e7ffe7; }
                    .close { background-color: #ffe7e7; }
                    .message { background-color: #f0f0f0; }
                    .unknown { background-color: #fff0f0; }
                    .debug { color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <h1>Server-Sent Events Demo</h1>
                <div id="status">Connecting...</div>
                <button onclick="clearEvents()">Clear Events</button>
                <div id="events"></div>
                
                <script>
                    const eventsDiv = document.getElementById('events');
                    const statusDiv = document.getElementById('status');
                    
                    function addEvent(message, type) {
                        const eventDiv = document.createElement('div');
                        eventDiv.className = 'event ' + type;
                        const timestamp = new Date().toLocaleTimeString();
                        eventDiv.innerHTML = 
                            '<span class="debug">[' + timestamp + ']</span> ' +
                            '<strong>' + type.toUpperCase() + ':</strong> ' + message;
                        eventsDiv.appendChild(eventDiv);
                        eventsDiv.scrollTop = eventsDiv.scrollHeight;
                    }
                    
                    function clearEvents() {
                        eventsDiv.innerHTML = '';
                    }
                    
                    // Debug: log all raw events
                    function logRawEvent(event) {
                        console.log('Raw Event:', {
                            type: event.type,
                            data: event.data,
                            lastEventId: event.lastEventId,
                            origin: event.origin
                        });
                    }
                    
                    const eventSource = new EventSource('/sse');
                    
                    eventSource.onopen = function(event) {
                        statusDiv.textContent = 'Connected';
                        statusDiv.style.color = 'green';
                        addEvent('SSE connection established', 'connected');
                        logRawEvent(event);
                    };
                    
                    eventSource.onerror = function(event) {
                        statusDiv.textContent = 'Connection error';
                        statusDiv.style.color = 'red';
                        addEvent('Connection error occurred', 'error');
                        logRawEvent(event);
                    };
                    
                    /*
                    eventSource.onmessage = function(event) {
                        // Handle messages without event type (default)
                        addEvent(event.data, 'message');
                        logRawEvent(event);
                    };
                    */
                    
                    // Specific event listeners
                    eventSource.addEventListener('connected', function(event) {
                        addEvent(event.data, 'connected');
                        logRawEvent(event);
                    });
                    
                    eventSource.addEventListener('timestamp', function(event) {
                        addEvent(event.data, 'timestamp');
                        logRawEvent(event);
                    });
                    
                    eventSource.addEventListener('message', function(event) {
                        addEvent(event.data, 'message');
                        logRawEvent(event);
                    });
                    
                    eventSource.addEventListener('close', function(event) {
                        addEvent(event.data, 'close');
                        statusDiv.textContent = 'Connection closed by server';
                        statusDiv.style.color = 'orange';
                        eventSource.close();
                        logRawEvent(event);
                    });
                    
                    // Catch-all for any other event types
                    eventSource.addEventListener('*', function(event) {
                        addEvent('Unknown event type: ' + event.type + ' - ' + event.data, 'unknown');
                        logRawEvent(event);
                    });
                </script>
            </body>
            </html>
            """;

        exchange.getResponseSender().send(html);
    }
}
