# MCP-DateTime: Installation Guide for LLMs

This server provides a rich and precise time/date/timezone utility toolset for MCP-compatible environments.  
It includes instant time queries, date math, formatting, parsing, comparison, and localization support.

---

## ☕ Java Requirements

MCP-DateTime requires **Java 21 or higher** to run properly.

---

## 🔹 For Claude Desktop Users

1. Open your `claude_desktop_config.json` file and add the following configuration:

```json
{
  "mcpServers": {
    "mcp-datetime": {
      "command": "java",
      "args": [
        "-jar",
        "https://github.com/key824999/MCP-DateTime/releases/download/v0.1.0/mcp-datetime.jar"
      ]
    }
  }
}
```

2. Restart Claude Desktop or refresh your MCP server list.
3. You can now use date/time functions like `nowUtcIso`, `addDays`, `isLeapYear`, `convertZone`, and many more.

> ⚠️ **Note for multiple MCP servers**
>
> If you're adding more than one MCP server in your `claude_desktop_config.json`,  
> make sure each server block is separated by a comma `,`.

---

## 🔹 For MCP CLI Users

If you have [MCP CLI](https://www.npmjs.com/package/mcp) installed, you can register the tool using:

```bash
npx mcp add https://raw.githubusercontent.com/key824999/MCP-DateTime/refs/heads/master/manifest.json
```

---

## 🔹 Running the Server Locally

To run this server on your own machine:

```bash
git clone https://github.com/key824999/MCP-DateTime.git
cd MCP-DateTime
./gradlew bootRun
```

---

## 📌 Notes

- All tools are exposed via `@Tool` annotations
- The manifest is auto-generated from classpath scanning
- Unit test coverage, timezone validation, and locale handling are built-in