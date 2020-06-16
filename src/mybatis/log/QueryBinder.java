package mybatis.log;

import com.github.vertical_blank.sqlformatter.SqlFormatter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBinder {
	private String log;

	public QueryBinder(String log) {
		this.log = log;
	}

	public String bind() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(log.getBytes())));

		String queryString = getQueryString(br);
		String parameterString = getParameterString(br);

		List<String> parameters = Arrays.asList(parameterString.split(","))
										.stream()
										.map(s -> s.trim())
										.map(s -> new Parameter(s))
										.map(v -> v.getBindedValue())
										.collect(Collectors.toList());
		String bindedQuery = SqlFormatter.format(queryString, parameters);

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection contents = new StringSelection(bindedQuery);
		clipboard.setContents(contents, null);

		return bindedQuery;
	}

	/*
	2020-06-15 10:56:35 | DEBUG | selectList                         :159  | [main] ==>  Preparing: SELECT A.failure_id ,A.title ,A.description ,A.failure_product_id ,A.failure_creation_type ,A.failure_region_code ,A.failure_start_datetime ,A.failure_end_datetime ,A.failure_status_code ,A.email_request_id_sent_operator ,A.failure_reg_member_uuid ,A.reg_datetime ,A.mod_datetime ,B.cloud_type as failureTargetCloudTypes ,C.member_uuid as failureTargetMemberUuids ,D.org_id as failureTargetOrgIds ,G.product_id as failureTargetProductIds ,H.project_id as failureTargetProjectIds FROM failure A LEFT JOIN failure_target_cloud_type B ON (A.failure_id = B.failure_id) LEFT JOIN failure_target_member C ON (A.failure_id = C.failure_id) LEFT JOIN failure_target_org D ON (A.failure_id = D.failure_id) LEFT JOIN failure_target_product G ON (A.failure_id = G.failure_id) LEFT JOIN failure_target_project H ON (A.failure_id = H.failure_id) JOIN ( SELECT failure_id FROM failure A WHERE A.title LIKE CONCAT('%', ?, '%') AND A.failure_status_code = ? AND A.failure_region_code in ( ? , ? ) AND A.failure_product_id in ( ? , ? ) AND EXISTS (SELECT 1 FROM failure_target_product FTP WHERE FTP.product_id in ( ? , ? ) AND FTP.failure_id = A.failure_id ) order by A.reg_datetime DESC LIMIT ? OFFSET ? ) as FF ON FF.failure_id = A.failure_id ORDER BY A.reg_datetime DESC
	2020-06-15 10:56:35 | DEBUG | selectList                         :159  | [main] ==> Parameters: a3-title(String), STABLE(String), US1(String), KR1(String), KGDeiKUq(String), NuJCU9TW(String), KGDeiKUq(String), NuJCU9TW(String), 10(Integer), 0(Integer)
	 */
	private String getParameterString(BufferedReader br) throws IOException {
		return extractByStartString(br, "Parameters:");
	}

	private String getQueryString(BufferedReader br) throws IOException {
		return extractByStartString(br, "Preparing:");
	}

	private String extractByStartString(BufferedReader br, String startString) throws IOException {
		String queryString = br.readLine();
		int startIndex = queryString.indexOf(startString);
		return queryString.substring(startIndex + startString.length())
						  .trim();

	}

	enum ParameterType {
		LITERAL {
			@Override
			public String bind(String value) {
				return String.format("'%s'", value);
			}

		},
		NUMBER {
			@Override
			public String bind(String value) {
				return value;
			}

		},
		NULL {
			@Override
			public String bind(String value) {
				return "null";
			}
		};

		abstract public String bind(String value);

		public static ParameterType getParameterType(String type) {
			switch (type) {
				case "STRING":
				case "TIMESTAMP":
					return LITERAL;
				default:
					return NUMBER;
			}
		}
	}

	static class Parameter {
		private String value;
		private ParameterType parameterType;

		public Parameter(String s) {
			int typeStartIndex = s.indexOf("(");
			int typeEndIndex = s.indexOf(")");

			if (typeStartIndex < 0) {
				value = "null";
				parameterType = ParameterType.NULL;
			} else {
				value = s.substring(0, typeStartIndex);
				parameterType = ParameterType.getParameterType(s.substring(typeStartIndex + 1, typeEndIndex).toUpperCase());
			}
		}

		public String getBindedValue() {
			return parameterType.bind(value);
		}
	}
}
