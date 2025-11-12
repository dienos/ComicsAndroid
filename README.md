# Comics App 과제

네이버 이미지 검색 API를 활용하여 웹툰 뷰어의 핵심 기능을 구현한 안드로이드 애플리케이션입니다.

## 1. 프로젝트 구조

본 프로젝트는 **클린 아키텍처(Clean Architecture)**를 기반으로 설계되었으며, 아래와 같이 역할과 책임에 따라 계층화된 패키지 구조를 가집니다. 의존성은 항상 외부 계층(`ui`, `data`)에서 내부 계층(`domain`)을 향합니다.

```
KSTDAndroidJTH (Root)
│
├── 📁 ui (Presentation Layer)
│   ├── base/              # BaseActivity, BaseFragment 등 공통 기반 클래스
│   ├── di/                # Hilt 의존성 주입 관련 모듈
│   ├── feature/           # 화면 단위(Activity/Fragment/Compose) 및 ViewModel
│   │   ├── splash/
│   │   ├── home/
│   │   ├── search/
│   │   ├── bookmark/
│   │   ├── viewer/
│   │   └── dialog/
│   ├── glide/             # Glide 관련 확장 모듈
│   ├── composable/        # 재사용 가능한 Composable 함수
│   ├── extension/         # View, Context 등 다양한 확장 함수
│   ├── theme/             # 앱의 테마, 색상, 타이포그래피 정의
│   └── util/              # 상수(Constants) 및 기타 유틸리티 클래스
│
├── 📁 domain (Domain Layer) - 순수 Kotlin 모듈
│   ├── model/             # 앱 전역에서 사용되는 핵심 데이터 모델 (Entity)
│   ├── usecase/           # 개별 비즈니스 로직을 캡슐화한 UseCase
│   ├── repository/        # 데이터 계층의 접근 방법을 정의한 인터페이스
│   └── exception/         # 사용자 정의 Exception
│
└── 📁 data (Data Layer)
    ├── datasource/        # Remote(Retrofit) / Local(Room) 데이터 소스
    ├── di/                # Hilt 의존성 주입 관련 모듈
    ├── repository/        # Domain 계층의 Repository 인터페이스 구현체
    └── mapper/            # DTO(Data Transfer Object)를 Domain Model로 변환하는 매퍼

```

## 2. 사용 라이브러리 및 용도

| 라이브러리 | 사용 용도 |
| :--- | :--- |
| **Kotlin & Coroutines** | 현대적이고 간결한 언어 사용, 비동기 처리를 통한 UI 반응성 확보 |
| **Jetpack Compose** | Splash, Webtoon Viewer 등 일부 화면을 선언형 UI로 작성 |
| **Hilt** | 프로젝트 전반의 의존성 주입을 통해 결합도를 낮추고 테스트 용이성 확보 |
| **ViewModel** | UI 상태를 저장하고 생명주기를 관리, `SavedStateHandle`로 프로세스 종료 대응 |
| **Room** | 북마크 데이터를 로컬에 안정적으로 저장하기 위한 ORM |
| **Paging 3** | 대용량의 네트워크 데이터를 효율적으로 로드하고 표시 (무한 스크롤) |
| **Navigation Component** | Single Activity 구조에서 Fragment 간의 화면 전환 관리 |
| **Retrofit & OkHttp** | Naver API와의 Type-safe한 네트워크 통신, `Interceptor`를 통한 인증 헤더 주입 |
| **Glide** | `AppGlideModule`을 통한 캐시 튜닝, 리스트 뷰의 이미지 로딩 및 캐싱 |
| **Lottie** | Splash 화면에서 고품질 벡터 애니메이션을 부드럽게 재생 |

## 3. 주요 기능 및 특징

*   **클린 아키텍처 활용**: `ui`, `domain`, `data` 3계층으로 모듈을 분리하여, 각 계층의 역할을 명확히 하고 테스트 용이성과 유지보수성을 극대화했습니다.
*   **태블릿/폴더블 레이아웃 대응**: 리소스 한정자(`sw600dp`)를 사용하여 태블릿과 같은 넓은 화면에서는 2단 그리드 레이아웃을 동적으로 적용, 화면 사용성을 높였습니다.
*   **다국어 처리 (Localization)**: 문자열 리소스를 `values`와 `values-ko`로 분리하여 관리함으로써, 향후 다양한 언어 추가에 용이한 구조를 갖추었습니다.
*   **기기 회전, 테마 변경 대응**: `ViewModel`과 `SavedStateHandle`을 통해 화면 회전이나 프로세스 종료(Process Death) 시에도 UI 상태를 안정적으로 보존하고 복원합니다.
*   **다크 모드 대응**: `values-night` 리소스 폴더를 통해 다크 모드 전용 색상과 테마를 제공하여, 사용자 설정에 맞는 일관된 시각적 경험을 제공합니다.
*   **하이브리드 UI 전략**: XML 기반의 `RecyclerView`와 `Jetpack Compose`를 함께 사용하여, 각 UI 툴킷의 장점을 이해하고 상황에 맞게 사용할 수 있는 역량을 보여주고자 했습니다.

## 4. 이미지 처리 전략: ViewModel 주도 캐시 아키텍처

"상용 웹툰 앱처럼, 뒷배경이 보이지 않을 정도로 빠르게 로드"하는 최고의 사용자 경험을 목표로, Glide의 기본 동작을 넘어서는 **ViewModel 주도의 수동 캐시 아키텍처**를 설계하고 구현했습니다.

### 4.1. 문제 정의 및 아키텍처 목표

Glide의 메모리 캐시는 크기가 제한적이어서, 빠른 스크롤 시 이전에 로드한 이미지가 캐시에서 제거(eviction)됩니다. 이로 인해 사용자가 다시 해당 이미지로 돌아올 때 디스크 캐시 I/O 또는 네트워크 요청이 발생하여 미세한 '깜빡임'이 발생합니다.

본 프로젝트에서는 이 문제를 해결하기 위해, **ViewModel의 생명주기와 동기화되는 LruCache**를 도입하여, Glide 캐시와는 별개의 **뷰어 전용 L2 메모리 캐시**를 구축하는 것을 목표로 삼았습니다.

### 4.2. 핵심 구현 사항

1.  **ViewModel-Level LruCache**: `WebtoonViewerViewModel` 내부에 앱 가용 메모리의 1/4을 할당하는 `LruCache<String, Bitmap>`를 구현했습니다. 이는 Glide의 자동 캐시 정책에 의존하지 않고, 뷰어에 가장 필요한 비트맵들의 생명주기를 ViewModel이 직접 통제하도록 만듭니다.

2.  **지능형 프리로딩 및 직접 제어**:
    *   사용자의 스크롤 위치를 `snapshotFlow`로 감지하여, 현재 위치의 앞/뒤 범위에 있는 이미지를 `Dispatchers.IO` 풀에서 미리 로드합니다.
    *   `Glide.asBitmap().submit().get()`으로 **비트맵을 직접 생성**하여 ViewModel의 `LruCache`에 저장합니다. `.get()`은 블로킹 호출이므로, 반드시 백그라운드 스레드에서 수행하여 UI 랙을 방지합니다.
    *   UI(Compose `AndroidView`)는 `ViewModel`의 캐시에서 비트맵을 직접 가져와 `setImageBitmap`으로 즉시 설정합니다. 캐시 히트(Cache Hit) 시 렌더링 지연 시간은 **0ms**에 수렴하여 완벽하게 부드러운 사용자 경험을 제공합니다.

### 4.3. 메모리 관리 및 안정성 (Trade-offs & Defensive Design)
*   **엄격한 캐시 크기 관리**: `LruCache`의 최대 크기는 `Runtime.getRuntime().maxMemory() / 1024`를 기준으로 설정하여, 기기의 메모리 사양에 따라 동적으로 조절됩니다. 무조건적인 고정 사이즈 할당으로 인한 저사양 기기의 OOM 가능성을 최소화했습니다.
