# Utilmore Java

이 프로젝트는 Teamo2 Java 기반 프로젝트에서 공통으로 사용되는 유틸리티 클래스들을 제공하는 라이브러리입니다.

## 개발 환경
- Java 17
- Gradle 8.x

## 주요 기능

### TimeUtil
- 날짜/시간 변환 및 포맷팅
- 나이 계산
- 요일 기반 날짜 추출
- 날짜 범위 확인
- UTC/KST 시간대 지원

## 라이브러리 사용 방법

### 1. 프로젝트 설정

build.gradle 파일에 다음 내용을 추가합니다: (* 버전 확인)

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/teamo2dev/utilmore-java")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation 'kr.teamo2:utilmore:{version}'
}
```
### 2. 환경변수 설정

#### - Github actions를 통한 CI/CD 시

- 프로젝트의 CI/CD workflow yml에서 다음 부분을 수정합니다.
  ```yaml
  run:
    docker build (-기타 옵션들...) \
    --build-arg GITHUB_USERNAME=${{ secrets.UTILMORE_PACKAGE_USERNAME }} \
    --build-arg GITHUB_TOKEN=${{ secrets.UTILMORE_PACKAGE }}
    ```
- Dockerfile에 다음 항목을 추가합니다.
  ```dockerfile
  ARG GITHUB_USERNAME
  ARG GITHUB_TOKEN

  ENV GITHUB_USERNAME=${GITHUB_USERNAME}
  ENV GITHUB_TOKEN=${GITHUB_TOKEN}
  ```
#### - 로컬 환경에서 구동 시
- 환경변수를 설정합니다.
  ```properties
  GITHUB_USERNAME=YOUR_GITHUB_USERNAME
  GITHUB_TOKEN=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
  ```
  
- 또는 `~/.gradle/gradle.properties` 파일에 GitHub 인증 정보를 추가합니다:
  ```properties
  gpr.user=YOUR_GITHUB_USERNAME
  gpr.key=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
  ```
  
## 패키지 수정 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/teamo2dev/utilmore-java.git
```

### 2. 버전 수정
- build.gradle 파일에서 다음 부분을 수정합니다.

```gradle
publishing {
    // repository
    publications {
        gpr(MavenPublication) {
            groupId = 'kr.teamo2'
            artifactId = 'utilmore'
            version = '1.0.5' // 다음 버전
        
            from(components.java)
        }
    }
}
```
### 3. 패키지 배포

- Prod 브랜치로 푸시 혹은 PR을 생성하여 merge될 때 자동으로 배포됩니다.
- 수동으로 배포하려면 다음 명령어를 실행합니다.

```bash
./gradlew publish
```

