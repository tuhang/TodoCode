package org.todocode.ai._01_prompts;

import java.util.Map;

/**
 * [TodoCode] 提示词工程模式
 *
 * <h3>背景:</h3>
 * 好的提示词对 LLM 的有效性至关重要。
 * 用清晰的指令和示例构建你的提示词。
 *
 * <h3>易错点:</h3>
 * 模糊的提示词会导致不一致的输出。
 * 始终为结构化输出提供格式示例。
 *
 * <h3>核心理解:</h3>
 * 关键提示词工程技术:
 * - 少样本学习: 提供示例
 * - 思维链: 要求"逐步思考"
 * - 角色扮演: "你是...方面的专家"
 */
public class PromptTemplates {

    /**
     * 建立上下文和规则的系统提示词。
     */
    public static String systemPrompt(String role, String constraints) {
        return """
                You are %s.
                
                ## Rules:
                %s
                
                ## Response Format:
                - Be concise and direct
                - Use bullet points for lists
                - Provide code examples when relevant
                """.formatted(role, constraints);
    }

    /**
     * 用于一致输出的少样本提示词模板。
     */
    public static String fewShotPrompt(String task, Map<String, String> examples) {
        StringBuilder sb = new StringBuilder();
        sb.append("Task: ").append(task).append("\n\n");
        sb.append("Examples:\n");

        for (Map.Entry<String, String> example : examples.entrySet()) {
            sb.append("Input: ").append(example.getKey()).append("\n");
            sb.append("Output: ").append(example.getValue()).append("\n\n");
        }

        sb.append("Now process the following:\n");
        return sb.toString();
    }

    /**
     * 用于复杂推理的思维链提示词。
     */
    public static String chainOfThoughtPrompt(String problem) {
        return """
                Problem: %s
                
                Let's solve this step by step:
                1. First, identify the key components
                2. Then, analyze each component
                3. Finally, synthesize a solution
                
                Think through each step carefully before providing the final answer.
                """.formatted(problem);
    }

    /**
     * 带 JSON 格式的结构化输出提示词。
     */
    public static String structuredOutputPrompt(String request, String jsonSchema) {
        return """
                %s
                
                Respond ONLY with valid JSON matching this schema:
                ```json
                %s
                ```
                
                Do not include any text outside the JSON object.
                """.formatted(request, jsonSchema);
    }
}
