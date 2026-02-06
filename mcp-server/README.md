# JavaScript MCP Server

A simple example of an MCP server implemented in Node.js/Express.
It exposes a JSON-RPC endpoint at `POST /mcp` compatible with the `mcp-router`.

## Tools

- `getRandomNumber`: Returns a random integer.
- `echo`: Echoes back a message.

## Usage

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start server:
   ```bash
   npm start
   ```

3. Configure `mcp-router` to point to `http://localhost:3000/mcp` with `protocol: mcp`.
