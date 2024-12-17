# Utilmore Java

이 프로젝트는 Teamo2 Java 기반 프로젝트에서 공통으로 사용되는 유틸리티 클래스들을 제공하는 라이브러리입니다.

## 버전
- 1.0.5

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

build.gradle 파일에 다음 내용을 추가합니다:

(repositories에서 mavenCentral()을 사용하고 있는 경우, 해당 부분을 제거)

(최신 버전 확인)

```gradle
repositories {
    maven {
        url "https://repository.carmore.kr/repository/maven-public/"
    }
}

dependencies {
    implementation 'kr.teamo2:utilmore:{version}'
}
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

