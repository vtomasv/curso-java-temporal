package com.sigeo.clase03;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class CsvParserTest {

    @Test
    void shouldParseValidLinesAndReportErrors() {
        CsvParser parser = new CsvParser();
        List<String> lines = List.of(
            "id1,req1,PENDING",
            "id2,req2", // Invalido: faltan campos
            "id3,req3,APPROVED"
        );

        CsvParser.ParseResult result = parser.parse(lines);

        assertThat(result.validLines()).containsExactly("id1,req1,PENDING", "id3,req3,APPROVED");
        assertThat(result.errors()).hasSize(1);
        
        CsvParser.ParseError error = result.errors().get(0);
        assertThat(error.lineNumber()).isEqualTo(2);
        assertThat(error.field()).isNotNull();
        assertThat(error.cause()).isNotNull();
    }
}
