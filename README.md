#  Todo List - SSY

##  설치 환경

- **JDK**: 11 이상
- **Android Studio**: Meerkat 이상 (권장: 최신 Stable 버전)
- **Gradle**: 8.11.1 (프로젝트 gradle-wrapper.properties에 명시된 버전 사용)
- **Android SDK**: minSdk 26 / targetSdk 35

##  프로젝트 구조

###  **데이터 관리 (Data Layer)**
- **Room Database**: 로컬 데이터 영속성 보장
- **Repository Pattern**: 데이터 소스 추상화
- **DAO**: 데이터베이스 접근 인터페이스

###  **보안 관련 컴포넌트**
- **CryptoManager**: Android Keystore를 활용한 AES 암호화/복호화 처리

###  **비즈니스 로직 (Domain Layer)**
- **UseCase**: 각 기능별 비즈니스 로직 캡슐화
- **Domain Model**: 핵심 데이터 모델

###  **사용자 인터페이스 (Presentation Layer)**
- **Jetpack Compose**: 선언적 UI 구성
- **Navigation**: 화면 간 이동 관리
- **Common Components**: 재사용 가능한 UI 컴포넌트

### **Architecture**
- **MVI Pattern**: 상태 관리 및 사용자 인터랙션 처리

