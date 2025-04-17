package mcp.datetime

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.classgraph.ClassGraph
import org.springframework.ai.tool.annotation.Tool
import java.io.File
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

object ManifestGenerator {
    private const val TOOL_NAME = "MCP-DateTime"
    private const val VERSION = "0.0.1"

    @JvmStatic
    fun main(args: Array<String>) {
        val mapper = ObjectMapper()
        val manifest: ObjectNode = mapper.createObjectNode()

        manifest.put("tool_name", TOOL_NAME)
        manifest.put("version", VERSION)
        manifest.put(
            "description",
            "A Kotlin-based MCP-compatible tool that provides time utilities such as formatting, parsing, date calculation, and timezone conversion."
        )

        val entrypoint = manifest.putObject("entrypoint")
        entrypoint.put("command", "java")

        val argsArray: ArrayNode = entrypoint.putArray("args")
        argsArray.add("-jar")
        argsArray.add("./libs/$TOOL_NAME-$VERSION-SNAPSHOT.jar")

        val actions: ArrayNode = manifest.putArray("actions")
        val actionNames = mutableSetOf<String>()

        val scanResult = ClassGraph()
            .acceptPackages("mcp.datetime.tool")
            .enableClassInfo()
            .enableAnnotationInfo()
            .scan()

        for (classInfo in scanResult.allClasses) {
            val clazz = classInfo.loadClass().kotlin

            for (function in clazz.declaredFunctions) {
                val toolAnnotation = function.findAnnotation<Tool>() ?: continue
                val toolName = function.name

                if (!actionNames.add(toolName)) {
                    error("Duplicate @Tool name detected: '$toolName' in class ${clazz.qualifiedName}")
                }

                val action = mapper.createObjectNode()
                action.put("name", toolName)
                action.put("description", toolAnnotation.description)

                val parametersNode = mapper.createObjectNode()

                function.parameters.filter { it.name != null && it.kind.name == "VALUE" }.forEach { param ->
                    val name = param.name!!
                    val type = param.type.jvmErasure.java
                    parametersNode.put(name, mapType(type))
                }

                action.set<ObjectNode>("parameters", parametersNode)
                actions.add(action)
            }
        }

        scanResult.close()

        mapper.writerWithDefaultPrettyPrinter()
            .writeValue(File("manifest.json"), manifest)

        println("-> manifest.json generated successfully!")
    }

    private fun mapType(type: Class<*>): String = when {
        Number::class.java.isAssignableFrom(type) || type.isPrimitive
        -> "number"
        type == String::class.java
        -> "string"
        type == Boolean::class.java || type == java.lang.Boolean.TYPE
        -> "boolean"
        else
        -> "any"
    }
}

