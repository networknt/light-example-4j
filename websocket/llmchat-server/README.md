# LLM Chat Server Example

This project demonstrates a WebSocket-based LLM Chat Server using `light-genai-4j` and `light-websocket-4j`. It connects to a local Ollama instance to provide chat capabilities.

## Prerequisites

- Java 21+ installed.
- Maven installed.
- [Ollama](https://ollama.com/) installed and running locally.
- The `qwen3:14b` model pulled in Ollama (or configure a different model in `src/main/resources/config/ollama.yml`).

```bash
ollama serve
ollama pull qwen3:14b
```

## Configuration

The server configuration is located in `src/main/resources/config/`.

- **`service.yml`**: Configures the `GenAiClient` implementation (uses `OllamaClient`).
- **`websocket-handler.yml`**: Maps the `/chat` path to the `GenAiWebSocketHandler`.
- **`ollama.yml`**: Configures the Ollama connection URL and model.
  ```yaml
  ollamaUrl: http://localhost:11434
  model: qwen3:14b
  ```

## Build

To build the project, run the following command from the project root:

```bash
mvn clean install
```

## Run

To run the server:

```bash
java -jar target/llmchat-server.jar
```

The server will start on port `8080` (HTTP) and `8443` (HTTPS) by default (check `server.yml`).

## Usage

1.  Open your browser and navigate to `http://localhost:8080`.
2.  You will see a simple Chat UI.
3.  Enter a **User ID** (or leave as default) and **Model** (optional override).
4.  Click **Connect**.
5.  Type a message and click **Send**.

The server receives the message via WebSocket, sends it to the configured LLM (Ollama), and streams the response back to the client.

## Architecture

- **`GenAiWebSocketHandler`**: Handles WebSocket connections and orchestrates the chat flow.
- **`ChatSessionRepository`**: Manages chat sessions (in-memory default).
- **`ChatHistoryRepository`**: Stores chat history for context (in-memory default).
- **`GenAiClient`**: Abstraction for interacting with GenAI providers.
- **`OllamaClient`**: Implementation of `GenAiClient` for Ollama.
