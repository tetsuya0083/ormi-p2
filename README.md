# ❄️ 냉장고를 부탁해 (Ormi P2) README 📖

---

## 🚀 1. 프로젝트 개요

"냉장고를 부탁해"는 Spring Boot, Thymeleaf, MySQL을 기반으로 한 **요리 레시피 & 커뮤니티** 웹 애플리케이션입니다.

* 🍳 레시피를 공유하고
* 💬 서로 소통하며
* ⭐️ 마음에 드는 글을 북마크 할 수 있어요!

---

## 📝 2. 요구사항 명세

1. **🔐 사용자 인증 & 프로필**

   * 스프링 시큐리티를 이용한 로그인/로그아웃
   * 회원가입: 닉네임, 이메일, 비밀번호, 전화번호, 프로필 이미지 등록
   * 프로필 페이지: 본인 정보 및 활동 통계(작성글 수, 댓글 수, 포인트) 제공

2. **📝 게시글 관리**

   * 카테고리: `RECOMMENDATION`, `SHARE`, `REQUEST`, `CHAT`, `TIPS`, `QA`
   * 게시글 작성·수정: 제목, 내용, 카테고리, 난이도(Share 전용), 이미지 업로드, 태그 입력
   * 최신글 및 인기글 목록 제공
   * 카드형 UI와 상세 조회 기능

3. **🔍 검색 & 필터링**

   * 제목·키워드·태그 검색 기능
   * 카테고리 드롭다운 필터
   * 서버-사이드 필터링 (JPA 쿼리 활용)

4. **🔖 북마크 기능**

   * 카드별 북마크 토글 (비동기 처리)

     * `POST /api/bookmarks/{postId}`: 북마크 추가
     * `DELETE /api/bookmarks/{postId}`: 북마크 해제
   * 북마크 전용 페이지(`/bookmarks`)

     * 기본 3개씩 표시 (페이지네이션 지원)
     * 페이지당 보기 옵션: 3, 10, 20, 50, 100
     * Prev · 페이지 번호 · Next 네비게이션

5. **🎨 UI/UX**

   * 반응형 카드 그리드 (CSS Grid)
   * 프로필 드롭다운 메뉴
   * 이미지 업로드 & 미리보기 기능
   * Tagify 및 Summernote 에디터 통합

---

## 🛠️ 3. 기술 스택

* **Backend**: Spring Boot, Spring Data JPA, Spring Security
* **Frontend**: Thymeleaf, HTML/CSS, JavaScript (ES6)
* **Database**: MySQL (AWS RDS)
* **Build**: Maven
* **Deploy**: AWS EC2

---

## 📁 4. 디렉토리 구조

```
src/
├── main/
│   ├── java/com/estsoft/ormi_p2/
│   │   ├── controller/       # 웹 & REST 컨트롤러
│   │   ├── domain/           # JPA 엔티티 & ENUM
│   │   ├── repository/       # Spring Data JPA 리포지토리
│   │   ├── service/          # 서비스 인터페이스 & 구현
│   │   └── OrmiP2Application.java
│   ├── resources/
│   │   ├── templates/        # Thymeleaf 뷰(.html)
│   │   ├── static/css/       # CSS 파일
│   │   ├── static/js/        # JavaScript 파일
│   │   └── application.properties
└── test/                     # 단위 테스트 코드
```
