package io.github.atkawa7.httpsnippet.generators.clojure;

import com.smartbear.har.model.*;
import io.github.atkawa7.httpsnippet.builder.CodeBuilder;
import io.github.atkawa7.httpsnippet.generators.CodeGenerator;
import io.github.atkawa7.httpsnippet.http.HttpHeaders;
import io.github.atkawa7.httpsnippet.http.MediaType;
import io.github.atkawa7.httpsnippet.models.Client;
import io.github.atkawa7.httpsnippet.models.Language;
import io.github.atkawa7.httpsnippet.models.internal.CodeRequest;
import io.github.atkawa7.httpsnippet.utils.ObjectUtils;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class CljHttp extends CodeGenerator {
private static final List<String> SUPPORTED_METHODS =
	Arrays.asList("get", "post", "put", "delete", "patch", "head", "options");

public CljHttp() {
	super(Client.CJ_HTTP, Language.CLOJURE);
}

private boolean isNotSupported(final String method) {
	return SUPPORTED_METHODS.indexOf(method.toLowerCase()) == -1;
}

private String padBlock(final int max, String input) {
	int len = max;
	StringBuilder padding = new StringBuilder();
	while (len > 0) {
	padding.append(" ");
	len--;
	}
	return input.replace("\n", "\n" + padding);
}

private <T> String literalRepresentation(T value) {
	if (ObjectUtils.isNull(value)) {
	return "nil";
	} else if (value instanceof String) {
	return "\"" + ((String) value).replace("\"", "\\\"") + "\"";
	} else if (value instanceof CljFile || value instanceof CljKeyword) {
	return value.toString();
	} else if (value instanceof List) {
	List list = (List) value;
	List<String> listBuilder = new ArrayList<>();
	for (Object obj : list) {
		listBuilder.add(this.literalRepresentation(obj));
	}
	return "[" + padBlock(1, String.join(" ", listBuilder)) + " ]";
	} else if (value instanceof Map) {
	Map<Object, Object> map = (Map) value;
	List<String> listBuilder = new ArrayList<>();

	for (Map.Entry<Object, Object> entry : map.entrySet()) {
		String val = padBlock(listBuilder.size() + 2, literalRepresentation(entry.getValue()));
		String format = String.format("%s %s \n", entry.getKey(), val);
		listBuilder.add(format);
	}
	return "{" + padBlock(1, String.join(":", listBuilder)) + "}";
	} else {
	return ObjectUtils.defaultIfNull(value, "");
	}
}

@Override
protected String generateCode(CodeRequest codeRequest) throws Exception {
	if (isNotSupported(codeRequest.getMethod())) {
	throw new RuntimeException(String.format("Request method %s", codeRequest.getMethod()));
	}

	CodeBuilder code = new CodeBuilder();

	Map<String, Object> body = new HashMap<>();

	if (codeRequest.hasHeadersAndCookies()) {
	body.put("headers", codeRequest.allHeadersAsMap());
	}

	if (codeRequest.hasQueryStrings()) {
	body.put("query-params", codeRequest.queryStringsAsMap());
	}

	if (codeRequest.hasBody()) {
	switch (codeRequest.getMimeType()) {
		case MediaType.APPLICATION_JSON:
		if (codeRequest.hasText()) {

			body.put("content-type", new CljKeyword("json"));
			body.put("form-params", codeRequest.getText());
		}
		break;
		case MediaType.APPLICATION_FORM_URLENCODED:
		if (codeRequest.hasParams()) {
			body.put("form-params", codeRequest.paramsAsMap());
		}
		break;

		case MediaType.MULTIPART_FORM_DATA:
		if (codeRequest.hasParams()) {
			List<Object> multipart = new ArrayList<>();
			for (HarParam param : codeRequest.getParams()) {
				Map<String, Object> content = new HashMap<>();
				Object value =
					(StringUtils.isNotBlank(param.getFileName())
							&& StringUtils.isBlank(param.getValue()))
						? new CljFile(param.getFileName())
						: param.getValue();
				content.put("name", param.getName());
				content.put("content", value);
				multipart.add(content);
			}
			body.put("multipart", multipart);

		}
		break;
		default:
		{
			body.put("body", codeRequest.getText());
		}
	}
	}

	Optional<HarHeader> optionalHarHeader = codeRequest.find(HttpHeaders.ACCEPT);
	optionalHarHeader.ifPresent(harHeader -> body.put("accept", new CljKeyword("json")));

	code.push("(require '[clj-http.client :as client])\n");

	if (ObjectUtils.isEmpty(body)) {
	code.push("(client/%s \"%s\")", codeRequest.getMethod().toLowerCase(), codeRequest.getUrl());
	} else {
	code.push(
		"(client/%s \"%s\" %s)",
		codeRequest.getMethod().toLowerCase(),
		codeRequest.getUrl(),
		padBlock(
			11 + codeRequest.getMethod().length() + codeRequest.getUrl().length(),
			literalRepresentation(body)));
	}
	return code.join();
}

@AllArgsConstructor
@Getter
class CljFile {
	private String path;

	@Override
	public String toString() {
	return "(clojure.java.io/file \"" + this.path + "\")";
	}
}

@AllArgsConstructor
@Getter
class CljKeyword {
	private String name;

	public String toString() {
	return ':' + this.name;
	}
}
}
