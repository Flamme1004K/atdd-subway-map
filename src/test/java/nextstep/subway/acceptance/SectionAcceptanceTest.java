package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.common.AcceptanceTest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineAcceptanceTest.노선을_만들다;
import static nextstep.subway.acceptance.LineAcceptanceTest.노선을_조회한다;
import static nextstep.subway.acceptance.StationAcceptanceTest.역을_만들다;
import static nextstep.subway.acceptance.common.RestAssuredTemplate.deleteRequestWithParameter;
import static nextstep.subway.acceptance.common.RestAssuredTemplate.postRequestWithParameterAndRequestBody;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long _8호선;
    private Long 모란역;
    private Long 암사역;

    @BeforeEach
    void init() {
        모란역 = 역을_만들다("모란역").as(StationResponse.class).getId();
        암사역 = 역을_만들다("암사역").as(StationResponse.class).getId();
        _8호선 = 노선을_만들다("8호선", "bg-pink-500", 모란역, 암사역, 17L).as(LineResponse.class).getId();
    }

    /**
     * Given 지하철역 구간을 생성하고
     * When 지하철역 노선 조회 시
     * Then 추가 된 구간을 조회할 수 있다.
     */
    @Test
    void 지하철역_구간을_생성한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 암사역, 한성백제역, 10L);

        // when
        var 조회한_8호선_구간 = 노선을_조회한다(_8호선).as(LineResponse.class).getStationResponses();

        // then
        assertThat(조회한_8호선_구간).containsExactly(
                new StationResponse(1L, "모란역"),
                new StationResponse(2L, "암사역"),
                new StationResponse(3L, "한성백제역")
        );
    }

    /**
     * Given 지하철역 구간을 2개 생성하고
     * When 지하철역 노선 조회 시
     * Then 추가 된 구간을 조회할 수 있다.
     */
    @Test
    void 지하철역_구간을_2개_생성한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 암사역, 한성백제역, 10L);

        var 문정역 = 역을_만들다("문정역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 한성백제역, 문정역, 10L);

        // when
        var 조회한_8호선 = 노선을_조회한다(_8호선).as(LineResponse.class).getStationResponses();

        // then
        assertThat(조회한_8호선).containsExactly(
                new StationResponse(1L, "모란역"),
                new StationResponse(2L, "암사역"),
                new StationResponse(3L, "한성백제역"),
                new StationResponse(4L, "문정역")
        );
    }

    /**
     * Given 역을 생성한다.
     * When 생성한 역을 이용하여 구간을 추가한다
     * Then 추가한 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아니면 등록이 실패한다.
     */
    @Test
    void 하행_종점역이_아닌_역에_구간을_추가하면_실패한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        var 문정역 = 역을_만들다("문정역").as(StationResponse.class).getId();

        // when & then
        구간_생성에_실패한다(_8호선, 한성백제역, 문정역);
    }

    /**
     * When 구간을 생성할 때
     * Then 새로운 구간의 하행역이 해당 노선에 등록되어있으면 등록이 실패한다.
     */
    @Test
    void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역이면_실패한다() {
        // when & then
        구간_생성에_실패한다(_8호선, 모란역, 암사역);
    }

    /**
     * Given 지하철역 구간을 생성하고
     * When 지하철역 구간을 삭제하면
     * Then 지하철 노선 조회 시 삭제 된 구간이 존재하지 않는다.
     */
    @Test
    void 지하철역_구간을_삭제한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 암사역, 한성백제역, 10L);

        // when
        구간을_삭제한다(_8호선, 한성백제역);

        // then
        var 조회한_8호선 = 노선을_조회한다(_8호선).as(LineResponse.class).getStationResponses();
        assertThat(조회한_8호선).doesNotContain(
                new StationResponse(3L, "한성백제역")
        );
    }

    /**
     * When 상행 종점역과 하행 좀정역에 대한 구간을 삭제하면
     * Then 상행 종점역과 하행 종점역만 있는 경우이기 때문에 삭제가 실패된다.
     */
    @Test
    void 상행_종점역과_하행_종점역만_있는_경우_에_지하철_구간을_삭제하면_실패한다() {
        // when & then
        지하철_구간_삭제에_실패한다(_8호선, 암사역);
    }

    /**
     * Given 지하철 구간을 추가하고
     * When & Then 지하철 중간 구간을 삭제 시 실패한다
     */
    @Test
    void 중간_지점_구간을_삭제하면_실패한다() {
        // given
        var 한성백제역 = 역을_만들다("한성백제역").as(StationResponse.class).getId();
        구간을_만들다(_8호선, 암사역, 한성백제역, 10L);

        // when & then
        지하철_구간_삭제에_실패한다(_8호선, 암사역);
    }

    private ExtractableResponse<Response> 구간을_만들다(Long id, Long upStationId, Long downStationId, Long distance) {
        var sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        var response = postRequestWithParameterAndRequestBody("/lines/{id}/sections", id, sectionRequest);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response;
    }

    private void 구간_생성에_실패한다(Long lineId, Long upStationId, Long downStationId) {
        var sectionRequest = new SectionRequest(upStationId, downStationId, 10L);
        var response = postRequestWithParameterAndRequestBody("/lines/{id}/sections", lineId, sectionRequest);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 구간을_삭제한다(Long lineId, Long stationId) {
        var response = deleteRequestWithParameter("/lines/{id}/sections?stationId=" + stationId, lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        return response;
    }

    private void 지하철_구간_삭제에_실패한다(Long lineId, Long stationId) {
        var response = deleteRequestWithParameter("/lines/{id}/sections?stationId=" + lineId, stationId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
