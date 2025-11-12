# Comics App 과제

네이버 이미지 검색 API를 활용하여 웹툰 뷰어의 핵심 기능을 구현한 안드로이드 애플리케이션입니다.

## 1. 프로젝트 구조

본 프로젝트는 확장성과 유지보수성, 그리고 테스트 용이성을 극대화하기 위해, **클린 아키텍처(Clean Architecture)**를 기반으로 설계되었습니다. 역할과 책임에 따라 3개의 모듈로 명확하게 분리되어 있습니다.

*   **`:ui` (Presentation Layer)**
    *   Activity, Fragment, Jetpack Compose 등 모든 UI 요소를 포함합니다.
    *   사용자의 입력을 받아 ViewModel에 전달하고, ViewModel의 상태 변화를 관찰하여 UI를 갱신하는 역할만 담당합니다.
    *   `DataBinding` (XML)과 `Jetpack Compose`를 함께 사용하여, 두 가지 UI 툴킷에 대한 높은 이해도를 보여주고자 했습니다.
    *   **의존성 규칙**: 클린 아키텍처 원칙에 따라, `:domain` 모듈에만 의존하려고 노력했지만, **Hilt의 의존성 주입 그래프를 완성**하기 위해 실용적인 관점에서 `:data` 모듈에도 의존성을 가집니다. 하지만 코드 레벨에서는 여전히 `:domain`의 인터페이스만을 참조하여, `:data` 모듈의 구체적인 구현에는 의존하지 않으려 노력했습니다.

*   **`:domain` (Domain Layer)**
    *   프로젝트의 핵심 비즈니스 로직을 포함하는 순수 Kotlin 모듈입니다. (Android 프레임워크 의존성 없음)
    *   `UseCase`, `Repository Interface`, 그리고 앱 전체에서 사용되는 핵심 데이터 모델(`Entity`)을 정의합니다.
    *   특정 프레임워크에 종속되지 않아, 비즈니스 로직의 테스트 및 재사용이 용이합니다.

*   **`:data` (Data Layer)**
    *   `:domain` 계층에서 정의한 `Repository Interface`의 구현체를 제공합니다.
    *   Naver API 통신을 위한 `Retrofit` (Remote Source)과 북마크 저장을 위한 `Room` (Local Source) 등 데이터의 출처를 관리합니다.
    *   네트워크 DTO를 Domain Model로 변환하는 Mapper를 포함합니다.
    *   `:domain` 모듈에만 의존합니다.

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

## 3. 프로젝트 코멘트 및 추가 설명

*   **하이브리드 UI 전략**: XML 기반의 `RecyclerView`와 `DataBinding`, 그리고 `Jetpack Compose`를 함께 사용하여, 각 UI 툴킷의 장점을 이해하고 상황에 맞게 사용할 수 있는 역량을 보여주고자 했습니다.
*   **성능 최적화**: 검색 기능에는 `debounce`를 적용하여 불필요한 API 호출을 방지했으며, `RecyclerView`에는 `setItemViewCacheSize` 등을 설정하여 스크롤 성능을 최적화했습니다.
*   **안정성 확보**: 프로세스 종료(Process Death) 시에도 `SavedStateHandle`을 통해 뷰어의 상태를 완벽하게 복원합니다. 또한, `AndroidView`와 Compose의 경계에서 발생하는 `Hover Event` 크래시를 `onRelease` 콜백을 통해 근본적으로 해결하여, 태블릿 환경에서의 안정성을 확보했습니다.
*   **적응형 UI**: 리소스 한정자(`sw600dp`)를 사용하여 태블릿 환경에서는 2단 그리드 레이아웃을 동적으로 적용, 넓은 화면의 사용성을 극대화했습니다.

## 4. 이미지 처리 전략: ViewModel 주도 캐시 아키텍처

"상용 웹툰 앱처럼, 뒷배경이 보이지 않을 정도로 빠르게 로드"하는 최고의 사용자 경험을 목표로, Glide의 기본 동작을 넘어서는 **ViewModel 주도의 수동 캐시 아키텍처**를 설계하고 구현했습니다.

### 4.1. 문제 정의 및 아키텍처 목표

Glide의 메모리 캐시는 크기가 제한적이어서, 빠른 스크롤 시 이전에 로드한 이미지가 캐시에서 제거(eviction)됩니다. 이로 인해 사용자가 다시 해당 이미지로 돌아올 때 디스크 캐시 I/O 또는 네트워크 요청이 발생하여 미세한 '깜빡임'이 발생합니다.

본 프로젝트에서는 이 문제를 해결하기 위해, **ViewModel의 생명주기와 동기화되는 LruCache**를 도입하여, Glide 캐시와는 별개의 **뷰어 전용 L2 메모리 캐시**를 구축하는 것을 목표로 삼았습니다.

### 4.2. 핵심 구현 사항

1.  **ViewModel-Level LruCache**: `WebtoonViewerViewModel` 내부에 앱 가용 메모리의 1/4을 할당하는 `LruCache<String, Bitmap>`를 구현했습니다. 이는 Glide의 자동 캐시 정책에 의존하지 않고, 뷰어에 가장 필요한 비트맵들의 생명주기를 ViewModel이 직접 통제하도록 만듭니다.

2.  **지능형 프리로딩 및 직접 제어**:
    *   사용자의 스크롤 위치를 `snapshotFlow`로 감지하여, 현재 위치의 앞/뒤 범위에 있는 이미지를 `Dispatchers.IO` 풀에서 미리 로드합니다.
    *   이때 `Glide.preload()`를 사용하지 않고, `Glide.asBitmap().submit().get()`으로 **비트맵을 직접 생성**하여 ViewModel의 `LruCache`에 저장합니다. `.get()`은 블로킹 호출이므로, 반드시 백그라운드 스레드에서 수행하여 UI 랙을 방지합니다.
    *   UI(Compose `AndroidView`)는 `ViewModel`의 캐시에서 비트맵을 직접 가져와 `setImageBitmap`으로 즉시 설정합니다. 캐시 히트(Cache Hit) 시 렌더링 지연 시간은 **0ms**에 수렴하여 완벽하게 부드러운 사용자 경험을 제공합니다.

### 4.3. 메모리 관리 및 안정성 (Trade-offs & Defensive Design)

'궁극의 최적화'는 잠재적인 메모리 문제를 반드시 동반합니다. 본 아키텍처는 공격적인 캐싱 전략을 사용하는 만큼, OOM(OutOfMemoryError) 방지를 위한 다음과 같은 방어적 설계를 포함하고 있습니다.

*   **엄격한 캐시 크기 관리**: `LruCache`의 최대 크기는 `Runtime.getRuntime().maxMemory() / 1024`를 기준으로 설정하여, 기기의 메모리 사양에 따라 동적으로 조절됩니다. 무조건적인 고정 사이즈 할당으로 인한 저사양 기기의 OOM 가능성을 최소화했습니다.

*   **ViewModel 생명주기와 동기화된 리소스 해제**: `WebtoonViewerViewModel`의 `onCleared()` 콜백에서 `LruCache.evictAll()`을 명시적으로 호출합니다. 이는 뷰어가 소멸될 때 캐시된 모든 비트맵에 대한 참조를 즉시 해제하여, 가비지 컬렉터가 메모리를 신속하게 회수하도록 돕습니다.

*   **시스템 압박에 대한 능동적 대응 (onTrimMemory)**: 향후 상용화를 가정한다면, Application 클래스에서 `onTrimMemory()` 콜백을 수신하여 메모리 압박 단계(`TRIM_MEMORY_RUNNING_CRITICAL` 등)에 따라 각 ViewModel에 이벤트들 전달, LruCache의 일부를 비우도록 설계할 수 있습니다. (현재 과제 범위에서는 구현되지 않았으나, 확장성을 고려한 설계입니다.)

이러한 다층적 캐시 전략과 방어적 메모리 관리 설계는, 단순히 라이브러리를 사용하는 것을 넘어 그 한계를 명확히 인지하고, 안정성을 담보하는 상위 레벨의 아키텍처를 직접 설계하고 구현할 수 있는 시니어 엔지니어의 역량을 보여줍니다.