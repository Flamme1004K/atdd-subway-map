package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        validationStations(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        Station upStation = findStation(lineRequest.getUpStationId());
        Station downStation = findStation(lineRequest.getDownStationId());

        final Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance(), upStation, downStation);
        final Line newLine = lineRepository.save(line);

        return LineResponse.of(newLine);
    }

    private Station findStation(Long upStationId) {
        return stationRepository.findById(upStationId).orElseThrow(() -> new IllegalArgumentException("역이 없습니다."));
    }


    private void validationStations(Long upStationId, Long downStationId) {
        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException("상행종점역과 하행종점역의 아이디는 같을 수 없습니다.");
        }
    }

}