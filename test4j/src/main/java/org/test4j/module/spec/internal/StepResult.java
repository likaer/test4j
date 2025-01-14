package org.test4j.module.spec.internal;

import lombok.Getter;
import org.test4j.json.JSON;
import org.test4j.tools.commons.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StepResult {
    private final ScenarioResult storyResult;
    private final String type;
    private String description;
    private String message;
    private boolean success = true;
    private Throwable exception;

    public StepResult(ScenarioResult scenarioResult, StepType type, String description) {
        this.storyResult = scenarioResult;
        this.type = type == null ? StepType.Given.name() : type.name();
        this.description = description;
        this.message = "ok";
    }

    public void setError(Throwable e) {
        this.success = false;
        this.message = e.getMessage() == null ? e.getClass().getName() : e.getMessage();
        this.exception = e;
        this.storyResult.setException(e);
    }

    public void skip(String name) {
        this.message = "skip method:" + name;
        this.success = false;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        buff.append(type).append(": ").append(description).append("\n");
        if (success) {
            buff.append("\tsuccess: ").append(message);
        } else {
            buff.append("\tfailure: ")
                    .append(exception == null ? "error message" : exception.getClass().getName())
                    .append(this.message.startsWith("\n") ? "" : "\n")
                    .append(this.message);
        }
        return buff.toString();
    }

    public void setDescription(String method, String description, Object[] args, Object result) {
        StringBuilder buff = new StringBuilder();
        if (!StringHelper.isBlankOrNull(this.description)) {
            buff.append(this.description).append("\n\t");
        }
        if (StringHelper.isBlankOrNull(description)) {
            buff.append(method);
        } else {
            buff.append(buildMethodDesc(method, description, args, result));
        }
        this.description = buff.toString();
    }

    private String buildMethodDesc(String method, String description, Object[] args, Object result) {
        List<String> tokens = this.findTokenList(description);
        Map<Integer, String> paras = new HashMap<>();
        this.addPara(paras, 0, result);
        int index = 1;
        for (Object arg : args) {
            this.addPara(paras, index, arg);
            index++;
        }
        StringBuilder buff = new StringBuilder();
        tokens.forEach(token -> buff.append(this.doToken(token, paras)));
        return buff.toString();
    }

    private String doToken(String token, Map<Integer, String> paras) {
        if (token.startsWith("{") && token.endsWith("}")) {
            Integer index = this.findIndex(token);
            return index == null ? token : "【" + paras.get(index) + "】";
        } else {
            return token;
        }
    }

    private Integer findIndex(String token) {
        String num = token.substring(1, token.length() - 1);
        if (StringHelper.isBlank(num)) {
            return 1;
        }
        try {
            return Integer.parseInt(num.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    private void addPara(Map<Integer, String> paras, int index, Object arg) {
        try {
            paras.put(index, JSON.toJSON(arg, true));
        } catch (Throwable e) {
            // do nothing
        }
    }

    private List<String> findTokenList(String description) {
        List<String> list = new ArrayList<>();

        String temp = description;
        while (true) {
            int start = temp.indexOf("{");
            int end = temp.indexOf("}");
            if (start > -1 && end > -1 && start < end) {
                list.add(temp.substring(0, start));
                list.add(temp.substring(start, end + 1));
                temp = temp.substring(end + 1);
            } else {
                break;
            }
        }
        return list;
    }
}
