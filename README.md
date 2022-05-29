# pseudo.lotto
가상 온라인 로또 복권 판매, 추첨 서비스

## 서비스 접속하기 -> [http://13.209.40.0:8080/](http://13.209.40.0:8080/)

## 사용 기술
- Java
- Spring Boot
- Thymeleaf
- JPA
- MySQL
- AWS EC2

## 주요 기능
- 회원 가입/로그인
![회원가입](https://user-images.githubusercontent.com/96247255/170872647-a7189fc0-b758-44da-946c-b71fde3d8843.PNG)
![로그인](https://user-images.githubusercontent.com/96247255/170872649-e94dd765-972f-42f5-9de1-bdfbd0a0ac4f.PNG)

- 포인트 충전/출금
![포인트충전](https://user-images.githubusercontent.com/96247255/170872654-ca42a1e8-1ecf-4ba8-88ca-3e5e88a48e66.PNG)
![포인트출금](https://user-images.githubusercontent.com/96247255/170872662-7ad1d8c4-91d7-463b-b13d-1f448dc801cc.PNG)

- 복권 구매
![복권 구매](https://user-images.githubusercontent.com/96247255/170872521-59108876-4f0b-4aba-8058-438787216b8a.PNG)
![복권 구매2](https://user-images.githubusercontent.com/96247255/170872523-cabbfd1e-6390-4c94-aa6b-ee8e0e39ddcc.PNG)

- 지난 회차 정보 조회
![회차별 당첨번호](https://user-images.githubusercontent.com/96247255/170872546-c49c1ac4-17a8-451a-bcd8-affb87d615a1.PNG)

- 복권 구매 내역 조회
![복권 구매내역](https://user-images.githubusercontent.com/96247255/170872562-1a033b44-20d0-4e08-be9c-05b0a0272a9f.PNG)

- 포인트 변동 내역 확인
![포인트 변동 내역 조회](https://user-images.githubusercontent.com/96247255/170872573-58f92276-850f-4478-b2f5-b3cc7e75dd53.PNG)
![포인트 변동 내역 조회2](https://user-images.githubusercontent.com/96247255/170872577-13933940-d4b6-4ab3-bd67-d15f40f4ea35.PNG)

- 포인트 이익이 많은 사람들조회
![lucky](https://user-images.githubusercontent.com/96247255/170872602-f95b9ee5-ba31-42a7-846b-e571154560b6.PNG)

- 포인트 손해가 많은 사람들 조회
![unlucky](https://user-images.githubusercontent.com/96247255/170872605-2ab950e4-e0a6-468e-b73d-587a54c046bd.PNG)

## 자세히
- Spring Boot, JPA 기술 학습을 목적으로 제작한 토이프로젝트입니다.
- 조회 기능을 제외한 나머지 기능들은 로그인이 필요합니다.
- 복권 판매는 매시간 0분~55분사이에 이루어지며 스프링의 스케줄링 기능을 이용해서 매시간 55분이되면 당첨번호 추첨 및 정산합니다.
- 아직 미숙한 부분이 많으며 향후 보완할 계획입니다.

## ERD
![erd](https://user-images.githubusercontent.com/96247255/170872702-41ba2ba2-9b31-4339-9c1f-ff9156ceef42.PNG)

## TODO LIST
- [ ] PRG(Post Redirect Get) 적용
- [ ] 코드 정리
- [ ] 단위 테스트 작성
- [ ] 안정적인 서비스 운영을 위한 로깅 추가
- [ ] 자바스크립트 로직 추가하여 백엔드 로직의 복잡도 최소화
- [ ] 웹 이외의 애플리케이션에서도 이용이 가능하도록 REST API 기반 구조로 전환
- [ ] NginX + Redis 를 이용해서 stateless 서버 구조로 변경
- [ ] 처리율 제한 알고리즘 적용해서 구매 갯수 제한 기능 추가
- [ ] OAuth 적용
- [ ] 디자인
