# 회계 거래 자동분류 시스템

은행 거래 내역 CSV 파일을 업로드하여 미리 정의된 분류 규칙에 따라 자동으로 회계 카테고리를 분류하는 시스템입니다.





<img width="1084" height="1203" alt="image" src="https://github.com/user-attachments/assets/21f5dc6c-4cf9-490a-b242-d6218b106eb2" />

## 실행 방법

### 사전 요구사항
- Docker
- Docker Compose

### 1. 저장소 클론
```bash
git clone https://github.com/your-username/accounting-classification-system.git
cd accounting-classification-system
```

### 2. Docker Compose로 실행
```bash
# Docker 이미지 빌드 및 컨테이너 실행
docker-compose up --build

# 백그라운드 실행 (선택사항)
docker-compose up --build -d
```

### 3. 접속 확인
- **웹 애플리케이션**: http://localhost:8080
- **데이터베이스**: localhost:5432 (postgres/postgres)

### 4. 애플리케이션 사용
1. 웹 브라우저에서 http://localhost:8080 접속
2. 파일 업로드 페이지에서 CSV와 JSON 파일 선택
3. "처리 시작" 버튼 클릭
4. 결과 확인

### 5. 종료
```bash
# 컨테이너 종료
docker-compose down

# 데이터까지 삭제 (선택사항)
docker-compose down -v
```

---

## 🛠 로컬 개발 환경 실행 (Docker 없이)

### 사전 요구사항
- Java 21 이상
- PostgreSQL 16 이상

### 1. 데이터베이스 설정
```sql
-- PostgreSQL에서 데이터베이스 생성
CREATE DATABASE oncomm;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE oncomm TO postgres;
```

### 2. 애플리케이션 실행
```bash
# 개발 모드로 실행
./gradlew bootRun

# 또는 JAR 빌드 후 실행
./gradlew bootJar
java -jar build/libs/*.jar
```

---

## 📖 사용법

### 🌐 웹 브라우저를 통한 사용

#### 1. 분류 규칙 파일 준비 (rules.json)
```json
{
  "companies": [
    {
      "company_id": "com_1",
      "company_name": "A 커머스",
      "categories": [
        {
          "category_id": "cat_101",
          "category_name": "매출",
          "keywords": ["네이버페이", "쿠팡"]
        },
        {
          "category_id": "cat_102",
          "category_name": "식비",
          "keywords": ["배달의민족", "김밥천국"]
        }
      ]
    }
  ]
}
```

#### 2. 거래 내역 CSV 파일 준비
```csv
거래일시,적요,입금액,출금액,거래후잔액,거래점
2025-07-20 13:45:11,스타벅스 강남2호점,0,5500,994500,강남지점
2025-07-20 15:12:30,(주)배달의민족,0,25000,969500,강남지점
2025-07-21 09:30:00,네이버페이(주),150000,0,1107000,온라인
```

#### 3. 웹 인터페이스 사용
1. **파일 업로드**: http://localhost:8080 접속 후 CSV, JSON 파일 선택
2. **처리 대기**: "처리 완료" 메시지 확인
3. **결과 확인**: 대시보드에서 분류 결과 및 통계 확인

### 🔧 API를 통한 사용

#### 파일 업로드 및 처리
```bash
# CSV/JSON 파일 업로드
curl -X POST http://localhost:8080/api/v1/accounting/process \
  -F "transactions=@transactions.csv" \
  -F "rules=@rules.json"

# 응답: "처리 완료"
```

#### 분류 결과 조회
```bash
# 회사별 분류된 거래 조회
curl "http://localhost:8080/api/v1/accounting/records?companyId=com_1" | jq

# 응답 예시
[
  {
    "occurredAt": "2025-07-20T15:12:30",
    "description": "(주)배달의민족",
    "deposit": 0,
    "withdraw": 25000,
    "category": {
      "id": "cat_102",
      "name": "식비"
    }
  }
]
```

#### 미분류 거래 조회
```bash
# 미분류 거래 조회
curl "http://localhost:8080/api/v1/accounting/unclassified?companyId=com_1" | jq

# 응답 예시
[
  {
    "occurredAt": "2025-07-21T21:00:15",
    "description": "개인용도 이체",
    "deposit": 0,
    "withdraw": 100000
  }
]
```

#### 요약 통계 조회
```bash
# 전체 수입/지출 요약
curl "http://localhost:8080/api/v1/accounting/summary/total/com_1" | jq

# 응답 예시
{
  "categoryId": "TOTAL",
  "categoryName": "전체",
  "totalIncome": 400000,
  "totalExpenditure": 111000
}

# 카테고리별 수입/지출 요약
curl "http://localhost:8080/api/v1/accounting/summary/categories/com_1" | jq

# 응답 예시
[
  {
    "categoryId": "cat_101",
    "categoryName": "매출",
    "totalIncome": 400000,
    "totalExpenditure": 0
  },
  {
    "categoryId": "cat_102",
    "categoryName": "식비",
    "totalIncome": 0,
    "totalExpenditure": 33000
  }
]
```

---

### 수동 API 테스트 시나리오

#### 1. 기본 플로우 테스트
```bash
# Step 1: 파일 업로드 및 처리
curl -X POST http://localhost:8080/api/v1/accounting/process \
  -F "transactions=@sample-transactions.csv" \
  -F "rules=@sample-rules.json"

# Step 2: 처리 결과 확인
curl "http://localhost:8080/api/v1/accounting/records?companyId=com_1"

# Step 3: 미분류 거래 확인
curl "http://localhost:8080/api/v1/accounting/unclassified?companyId=com_1"

# Step 4: 요약 통계 확인
curl "http://localhost:8080/api/v1/accounting/summary/total/com_1"
```

#### 2. 에러 케이스 테스트
```bash
# 잘못된 회사 ID로 조회
curl "http://localhost:8080/api/v1/accounting/records?companyId=invalid"
# 예상 응답: 빈 배열 []

# 필수 파라미터 누락
curl "http://localhost:8080/api/v1/accounting/records"
# 예상 응답: 400 Bad Request

# 존재하지 않는 회사 ID로 요약 조회
curl "http://localhost:8080/api/v1/accounting/summary/total/nonexistent"
# 예상 응답: 기본값 반환
```

---

## 📋 샘플 데이터

### sample-rules.json
```json
{
  "companies": [
    {
      "company_id": "com_1",
      "company_name": "A 커머스",
      "categories": [
        {
          "category_id": "cat_101",
          "category_name": "매출",
          "keywords": ["네이버페이", "쿠팡"]
        },
        {
          "category_id": "cat_102",
          "category_name": "식비",
          "keywords": ["배달의민족", "김밥천국"]
        },
        {
          "category_id": "cat_103",
          "category_name": "사무용품비",
          "keywords": ["오피스디포"]
        }
      ]
    },
    {
      "company_id": "com_2",
      "company_name": "B 커머스",
      "categories": [
        {
          "category_id": "cat_201",
          "category_name": "교통비",
          "keywords": ["카카오 T", "택시"]
        },
        {
          "category_id": "cat_202",
          "category_name": "통신비",
          "keywords": ["KT", "SKT"]
        },
        {
          "category_id": "cat_204",
          "category_name": "복리후생비",
          "keywords": ["스타벅스"]
        }
      ]
    }
  ]
}
```

### sample-transactions.csv
```csv
거래일시,적요,입금액,출금액,거래후잔액,거래점
2025-07-20 13:45:11,스타벅스 강남2호점,0,5500,994500,강남지점
2025-07-20 15:12:30,(주)배달의민족,0,25000,969500,강남지점
2025-07-20 19:03:01,카카오 T,0,12500,957000,강남지점
2025-07-21 09:30:00,네이버페이(주),150000,0,1107000,온라인
2025-07-21 12:10:05,김밥천국 역삼점,0,8000,1099000,역삼지점
2025-07-21 14:20:40,(주)쿠팡,250000,0,1349000,온라인
2025-07-21 21:00:15,개인용도 이체,0,100000,1249000,강남지점
2025-07-22 11:55:10,오피스디포(주),0,78000,1171000,강남지점
```

---

## 📋 주요 API 엔드포인트

| 메서드 | 엔드포인트 | 설명 | 예시 |
|--------|------------|------|------|
| POST | `/api/v1/accounting/process` | CSV/JSON 파일 업로드 및 분류 처리 | `curl -F "transactions=@file.csv" -F "rules=@rules.json" http://localhost:8080/api/v1/accounting/process` |
| GET | `/api/v1/accounting/records?companyId={id}` | 회사별 분류된 거래 조회 | `curl "http://localhost:8080/api/v1/accounting/records?companyId=com_1"` |
| GET | `/api/v1/accounting/unclassified?companyId={id}` | 미분류 거래 조회 | `curl "http://localhost:8080/api/v1/accounting/unclassified?companyId=com_1"` |
| GET | `/api/v1/accounting/summary/total/{companyId}` | 전체 수입/지출 요약 | `curl "http://localhost:8080/api/v1/accounting/summary/total/com_1"` |
| GET | `/api/v1/accounting/summary/categories/{companyId}` | 카테고리별 요약 | `curl "http://localhost:8080/api/v1/accounting/summary/categories/com_1"` |

---

## 🐳 Docker 설정 파일

### Dockerfile
```dockerfile
FROM gradle:8.4-jdk21-alpine AS builder
WORKDIR /app
COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
```yaml
version: "3.8"

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/oncomm
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
    depends_on:
      - db

  db:
    image: postgres:16
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=oncomm
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

---

## 📄 추가 문서

- **API 상세 문서**: `API_Documentation.pdf`
- **ERD 설계서**: `Database_Design.pdf`
- **분류 로직 설계서**: `Classification_Logic.pdf`
- **Postman Collection**: `postman-collection.json`

---



