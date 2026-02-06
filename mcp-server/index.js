const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const port = 5000;

app.use(cors());
app.use(bodyParser.json());

// Tools implementation
const tools = {
    getRandomNumber: {
        description: "Returns a random number between min and max",
        inputSchema: {
            type: "object",
            properties: {
                min: { type: "integer" },
                max: { type: "integer" }
            },
            required: ["min", "max"]
        },
        execute: (args) => {
            const min = args.min || 0;
            const max = args.max || 100;
            return {
                content: [
                    {
                        type: "text",
                        text: String(Math.floor(Math.random() * (max - min + 1)) + min)
                    }
                ]
            };
        }
    },
    echo: {
        description: "Echoes back the input",
        inputSchema: {
            type: "object",
            properties: {
                message: { type: "string" }
            },
            required: ["message"]
        },
        execute: (args) => {
            return {
                content: [
                    {
                        type: "text",
                        text: `Echo: ${args.message}`
                    }
                ]
            };
        }
    }
};

app.post('/mcp', (req, res) => {
    const { jsonrpc, method, params, id } = req.body;

    console.log(`Received request: ${JSON.stringify(req.body)}`);

    if (jsonrpc !== '2.0') {
        return res.status(400).json({ jsonrpc: "2.0", error: { code: -32600, message: "Invalid Request" }, id });
    }

    let result;
    try {
        switch (method) {
            case 'initialize':
                result = {
                    protocolVersion: "2024-11-05",
                    capabilities: { tools: { listChanged: true } },
                    serverInfo: { name: "mcp-server-js", version: "1.0.0" }
                };
                break;
            case 'tools/list':
                result = {
                    tools: Object.keys(tools).map(key => ({
                        name: key,
                        description: tools[key].description,
                        inputSchema: tools[key].inputSchema
                    }))
                };
                break;
            case 'tools/call':
                const tool = tools[params.name];
                if (!tool) {
                    return res.status(404).json({ jsonrpc: "2.0", error: { code: -32601, message: "Method not found" }, id });
                }
                result = tool.execute(params.arguments);
                break;
            default:
                if (method.startsWith('notifications/')) {
                    return res.status(200).end();
                }
                return res.status(404).json({ jsonrpc: "2.0", error: { code: -32601, message: "Method not found" }, id });
        }
        res.json({ jsonrpc: "2.0", result, id });
    } catch (error) {
        console.error("Error processing request:", error);
        res.status(500).json({ jsonrpc: "2.0", error: { code: -32000, message: error.message }, id });
    }
});

app.listen(port, () => {
    console.log(`MCP Server listening on port ${port}`);
});
