package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private final static int LIMIT_SECTIONS_SIZE = 1;
    private final static int VALIDATION_SECTIONS_SIZE = 0;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Line line, Section section) {
        addSectionValidation(section);
        sections.add(section);
        section.setLine(line);
    }

    private void addSectionValidation(Section section) {
        if (sections.size() > VALIDATION_SECTIONS_SIZE) {
            isContains(section);
            matchDownStation(section);
            matchStation(section);
        }
    }

    private void isContains(Section section) {
        if (sections.contains(section)) {
            throw new IllegalArgumentException("이미 등록 된 구간입니다");
        }
    }

    private void matchDownStation(Section section) {
        boolean isUpStation = sections.stream().anyMatch(currentSection ->
                currentSection.matchDownStation(section.getUpStation())
        );
        if (!isUpStation) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
    }

    private void matchStation(Section section) {
        boolean isDownStation = sections.stream().anyMatch(currentSection ->
                currentSection.matchStation(section.getDownStation())
        );
        if (isDownStation) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
        }
    }

    public void removeSection(Station station) {
        Section lastSection = getLastSection();
        if (!lastSection.matchDownStation(station)) {
            throw new IllegalArgumentException("현재 구간은 마지막 구간이 아닙니다.");
        }

        if (sections.size() == LIMIT_SECTIONS_SIZE) {
            throw new IllegalArgumentException("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없습니다.");
        }
        sections.remove(lastSection);
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Station> getStation() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
