# 팀 구성원, 개인 별 역할
- 👑 황민우: 팀장, CTO
- 김지영: 기획
- 심현보: 위클리 작성
- 우주완: 리드미 작성
- 이연재: 인프라
- 장서현: PM


# 팀 내부 회의 진행 회차 및 일자
### 1회차 (2023.01.16)  
- 기능 정의서 작성 (기능 목록 작성, DB 설계, Endpoint 설정)
- slack 가입
- 깃허브 협업 연습(branch, forks, pull request)

### 2회차 (2023.01.17)
- 체크 리스트 & 기능 명세서 작성
- ERD 테이블
- CI 구축 (crontab X)

### 3회차(2023.01.18)
- WBS 작성
- README 작성
- 기능 구현
    - 황민우 - 도서 검색: api(카카오)연동
    - 김지영 - UI, 챌린지 기능 CRUD
    - 심현보 - 팔로잉의 리뷰 등록 알림
    - 우주완 - 회원 가입
    - 이연재 - 팔로잉, 언팔로잉
    - 장서현 - 도서 리뷰 등록

### 4회차(2023.01.19)
- 목요일까지 개발한 것 develop branch에 merge 후 postman과 DB를 사용해 테스트 후 수정 & 배포

### 5회차(2023.01.20)
- 최종 위클리 점검


# 현재까지 개발 과정 요약 (최소 500자 이상)
- 황민우: 
  - 현재 WebFlux를 사용하여 카카오 도서 검색 api요청 연동을 해두었습니다.
  - 카카오 api에서 간혹 중복되는 검색결과가 나왔고 공식 질의응답 페이지에서 확인 결과 중복될 수 있다는 오피셜 답변을 확인하였습니다.
  - 이 부분을 어떻게 처리하면 좋을 지 몰라, 따로 처리하지 않고 반환하게 해두었습니다.
  - api 의존도 문제 해결 방안에 대해 고민 중입니다.
  - api 응답이 지연될 시 우리 DB에서 반환하도록 해야할지
  - 그럼 그 DB에 데이터 저장 시점은 언제가 좋을지 고민 중이고
  - 크롤링이나 스크립트를 짜서 기본 데이터 구축이 필요해보입니다.

- 김지영: 
  - 챌린지 CRUD 구현(Entity, Repository, DTO, Service,,,)
  - ControllerTest, ServiceTest
  - home.html 메인화면 로고, 사이드바, 서치창

- 심현보: 
  - 내 리뷰에 좋아요를 누르면 알림 생성
  - 알람 조회 controller 테스트 코드 작성
  - ApplicationEventPublisher를 이용한 Event 처리
  - 팔로잉의 리뷰 등록 알림 , 나를 팔로잉하면 알림 구현 진행중

- 우주완: 
	- 로그인, 회원 가입을 위해 jwtfilter, jwtprovider, config(password) 생성
	- 회원가입, 로그인 기능 구현(엔티티, 디티오, 리포, 서비스, 컨트롤러 구현)
  - controller 회원가입 로그인 테스트 코드 작성
  - controller 회원가입 로그인 서비스 코드 미작성
  - 회원 수정 기능 구현 중 

- 이연재: 
  - 깃 액션을 이용해 CI 파이프라인을 구축하고 AWS EC2에 크론탭을 사용해 변경사항을 매분마다 확인해 배포되도록 구현했습니다.
  - 팔로우/언팔로우 기능 구현(Controller, Entity, Dto, Service, Repository)
  - 팔로우/언팔로우 테스트 코드 작성(Controller, Service)
  - 팔로잉 조회/팔로우 조회 기능 구현

- 장서현: 
	- PM으로서 각 멤버에게 역할 할당 및 개발할 기능을 부여했고, 팀 노션을 관리했습니다.
	- 기능 개발: 도서 리뷰 등록 (리뷰 Entity, DTO, Repository, Service, Controller 구현)
						 도서 리뷰 좋아요 & 취소 (좋아요 Entity, Repository 구현)
	- 개발한 각 기능에 대한 테스트 코드 작성(Controller, Se~rvice)


# 개발 과정에서 나왔던 질문 (최소 200자 이상)
### 질문1 (2023.01.16)
- 리뷰 항목(레이더 차트) 컬럼들을 다른 테이블로 분리 여부
- KDC(카테고리 분류) 관리 테이블 생성
- ~알림: 좋아요, 새로운 리뷰는 리뷰 id가 필요하고 팔로잉은 리뷰 id 필요~ → **해결**: source컬럼으로 좋아요 id, 팔로워 id, 리뷰 id를 받기

### 질문2 (2023.01.17)
- 개인 랭킹
  - 백준의 랭킹 같은 기능을 어떻게 구현하면 좋을지.
  - 탑10 ~ 탑100은 Spring Data JPA 메소드로 구현, 개인 랭킹을 조회하기 위해서 어떻게 하면 좋을지?
  - 따로 랭킹 테이블을 작성하고 일정시간이 되면 업데이트해주는 방식으로 가야하는지.
- 알림 삭제 기능 (확인하면 사라지는 기능: confirm) -> 고도화(스프링 스케줄러 -> batch[keep])
  - 읽음 처리
  - 일정 시간이 지난 알림은 삭제 (스케줄러)
- 분업시 기능별로 맡으면 연관관계 어떻게 해결?

### 질문3 (2023.01.19)
- 디렉터리 관리
  - 기존 레이어드 별로 관리하는 방법과 현재 적용 중인 도메인 별로 관리하는 방법 중 협업에 적합한 방법이 무엇일까요? 


# 개발 결과물 공유
- Github Repository URL: [book-more.git](https://github.com/likelion-backend-2nd-project-team11/book-more)
- Team Notion URL: [중요한 건 꺾이지 않는 민우팀 노션](https://menu-hwang.notion.site/20389849506f486f8e59acd448848e32)
- 배포 주소: [www.bookmore.site:8080](http://ec2-43-201-77-193.ap-northeast-2.compute.amazonaws.com:8080)
