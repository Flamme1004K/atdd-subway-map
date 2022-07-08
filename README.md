# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 🚀 2단계 - 지하철 노선 기능
### 기능 요구사항
- [ ] 요구사항 설명에서 제공되는 인수 조건을 기반으로 지하철 노선 관리 기능을 구현한다.
  - [x] 지하철 노선 등록 API 구현한다.
  - [ ] 지하철 노선 목록 API 구현한다.
  - [x] 지하철 노선 조회 API 구현한다.
  - [ ] 지하철 노선 수정 API 구현한다.
- [ ] 지하철 노선을 구현한다.
  - [x] 지하철 노선은 상행종점역과 하행종점역이 있다.
  - [x] 지하철 노선은 거리를 가지고 있다.
  - [x] 지하철 노선을 저장하기 위해서는 역의 정보가 저장되어 있어야한다.
  - [x] 상행종점역과_하행종점역은 다르다.
- [x] 인수 테스트를 격리한다.
- [x] 인수 테스트 메서드 시그니처를 만든다.
- [ ] 인수 조건을 검증하는 인수 테스트를 작성한다.
- [ ] 지하철 노선 생성 인수 테스트를 작성한다.
```text
When 지하철 노선을 생성하면
Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다 
```
- [ ] 지하철 노선 목록 조회 인수 테스트를 작성한다.
```text
Given 2개의 지하철 노선을 생성하고
When 지하철 노선 목록을 조회하면
Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
```

- [x] 지하철 노선 조회 인수 테스트를 작성한다.
```text
Given 지하철 노선을 생성하고
When 생성한 지하철 노선을 조회하면
Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
```
- [ ] 지하철 노선 수정 인수 테스트를 작성한다.
```text
Given 지하철 노선을 생성하고
When 생성한 지하철 노선을 수정하면
Then 해당 지하철 노선 정보는 수정된다
```
- [ ] 지하철 노선 삭제 인수 테스트를 작성한다.
```text
Given 지하철 노선을 생성하고
When 생성한 지하철 노선을 삭제하면
Then 해당 지하철 노선 정보는 삭제된다
```

### 프로그래밍 요구사항
- 아래의 순서로 기능을 구현하세요.
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리 시키세요.
- 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.

## 🚀 1단계 - 지하철역 인수 테스트 작성
### 기능 요구사항
- 지하철역 인수 테스트를 완성하세요.
  - [x] 지하철역 목록 조회 인수 테스트 작성하기
  - [x] 지하철역 삭제 인수 테스트 작성하기
  - [x] RestAssuredTemplate 만들어보기