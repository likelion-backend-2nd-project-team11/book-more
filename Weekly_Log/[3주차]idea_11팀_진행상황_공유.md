# 팀 구성원, 개인 별 역할
- 👑 황민우: 팀장, CTO
- 김지영: 기획
- 심현보: 위클리 작성
- 우주완: 리드미 작성
- 이연재: 인프라
- 장서현: PM


# 팀 내부 회의 진행 회차 및 일자
### 1회차 (2023.01.30)  
- 중간 보고회
- OAuth2.0 소셜 로그인 적용
- 네이버 api 추가

### 2회차 (2023.01.31)
- 프로젝트 진행 상황 공유
- workflow 작성

### 3회차(2023.02.01)
- 프로젝트 진행 상황 공유
- API 배포서버에서 테스트 → 에러  발생 기록

### 4회차(2023.02.02)
- 프로젝트 진행 상황 공유

### 5회차(2023.02.03)
- S3 파일 업로드


# 현재까지 개발 과정 요약 (최소 500자 이상)
- 황민우
  - 프론트 자바스크립트 작성.
  - 로그인, 회원가입, 리뷰 작성, 조회, 챌린지 조회, 작성, 수정, 삭제 연동.
  - S3 정적 웹 호스팅, ACM SSL 발급, ELB 활용 아키텍처 구성.
- 김지영
	- validation 적용 및 테스트
	- 마이페이지 정보 수정 구현, 마이페이지 프론트 구현
- 심현보
	- 스케줄러를 통한 ranks 테이블 00시 자동 갱신 구현
	- 팔로잉이 리뷰를 등록했을 때의 알림 이벤트 핸들러 로직 변경
	- 랭크 페이지 구현 
- 우주완
	- 회원 검증 로직 추가 (verify)
	- OAuth2 적용
		- 소셜 로그인 Google 적용
- 이연재
  - 프론트 배포 workflow 작성 후 S3로 배포
  - Route53, CloudFront, ELB 활용 아키텍처 구성.
- 장서현
	- 독서 리뷰 등록창, 알림창 디자인
	- 독서 리뷰 등록 Validation 적용: body not null, chart의 5가지 항목 1 ~ 5만 입력 가능
	- Validation 적용 후 exception 형식 수정
	- 프로필 사진 등록 기능 구현(AWS S3 사용)


# 개발 결과물 공유
- Github Repository URL: [book-more.git](https://github.com/likelion-backend-2nd-project-team11/book-more)
- Team Notion URL: [중요한 건 꺾이지 않는 민우](https://menu-hwang.notion.site/20389849506f486f8e59acd448848e32)
- 배포 주소: [www.bookmore.site](http://ec2-43-201-77-193.ap-northeast-2.compute.amazonaws.com:8080)

****필수) 팀원들과 함께 찍은 인증샷(온라인 만남시 스크린 캡쳐)도 함께 업로드 해주세요 🙂

![image](https://user-images.githubusercontent.com/80660768/216482210-282b4e32-857c-4bed-a0b0-0a40d5071a98.png)
