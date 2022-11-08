package edu.hm.hafner.analysis.parser;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.analysis.AbstractParserTest;
import edu.hm.hafner.analysis.IssueParser;
import edu.hm.hafner.analysis.ParsingException;
import edu.hm.hafner.analysis.Report;
import edu.hm.hafner.analysis.Severity;
import edu.hm.hafner.analysis.assertions.SoftAssertions;

import static edu.hm.hafner.analysis.assertions.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests the class {@link PnpmAuditParser}.
 *
 * @author Fabian Kaupp - kauppfbi@gmail.com
 */
public class PnpmAuditParserTest extends AbstractParserTest {

    /**
     * Creates a new instance of {@link AbstractParserTest}
     */
    protected PnpmAuditParserTest() {
        super("pnpm-audit.json");
    }

    @Override
    protected void assertThatIssuesArePresent(final Report report, final SoftAssertions softly) {
        softly.assertThat(report).hasSize(14);

        softly.assertThat(report.get(0))
                .hasModuleName("serialize-javascript")
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("CVE-2020-7660")
                .hasCategory("Insecure serialization leading to RCE");
    }

    @Test
    void shouldHandleEmptyResultsJenkins67296() {
        Report report = parse("issue67296.json");

        assertThat(report).isEmpty();
    }

    @Test
    void shouldMapCorrectly() {
        Report report = parse("pnpm-audit.json");

        assertThat(report).hasSize(14);

        assertThat(report.get(0))
                .hasSeverity(Severity.WARNING_HIGH)
                .hasType("CVE-2020-7660")
                .hasModuleName("serialize-javascript");
        assertThat(report.get(1))
                .hasSeverity(Severity.WARNING_LOW)
                .hasType("CVE-2013-7370")
                .hasModuleName("connect");
        assertThat(report.get(2))
                .hasSeverity(Severity.WARNING_NORMAL)
                .hasType("CVE-2016-1000236")
                .hasModuleName("cookie-signature");
    }

    @Test
    void brokenInput() {
        assertThatThrownBy(() -> parse("eclipse.txt")).isInstanceOf(ParsingException.class);
    }

    @Override
    protected IssueParser createParser() {
        return new PnpmAuditParser();
    }
}
