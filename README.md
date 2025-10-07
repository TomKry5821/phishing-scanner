# phishing-scanner

Repozytorium zawierające kod źródłowy projektu skanera SMS-ów phishingowych.

## Opis działania

**phishing-scanner** to serwis weryfikujący przychodzące wiadomości SMS pod kątem phishingu.  
Skaner weryfikuje przychodzące wiadomości i w zależności od wyniku je odrzuca lub przekazuje dalej.

Skaner rozróżnia trzy typy SMS-ów:

1. **Włączający weryfikację** — SMS o treści `START` wysyłany na numer zarządzający usługą; włącza skanowanie dla
   numeru.
2. **Wyłączający weryfikację** — SMS o treści `STOP`; wyłącza skanowanie dla numeru.
3. **Zwykła wiadomość** — dowolna treść, która jest analizowana pod kątem phishingu.

Pierwsze dwa typy służą do zarządzania listą numerów, dla których usługa jest aktywna.  
Wiadomości wysyłane na numery bez aktywnej weryfikacji są przekazywane dalej bez analizy.  
Jeżeli zwykły SMS zostanie uznany za phishing — trafia na kolejkę wiadomości phishingowych; w przeciwnym razie — na
kolejkę wiadomości bezpiecznych.

---

## Przyjęta architektura

Skaner działa jako serwis serwerowy i łączy się z:

- **Systemem kolejkowym** (RabbitMQ) — odbiera przychodzące SMS-y i przekazuje je na odpowiednie kolejki zgodnie z
  wynikiem weryfikacji.
- **Źródłem danych** (H2 / SQL) — przechowuje numery z włączoną weryfikacją.
- **Usługą weryfikującą phishing** opisaną w rozdziale **Integracje**.

Główne założenia: asynchroniczne przetwarzanie, skalowalność i łatwe wpięcie jako filtr w ramach większego systemu.  
Dla uproszczenia demonstracji pominięto komponenty takie jak monitoring czy zewnętrzna baza danych.

---

## Wykorzystane technologie

- Java + Spring Boot
- Spring Cloud Stream (abstrakcja nad systemem kolejkowym)
- RabbitMQ (system kolejkowy)
- H2 (lokalna baza danych; aplikacja konfigurowalna do użycia innego SQL)
- Testy: Groovy + Spock
- Budowanie: Gradle
- Konteneryzacja: Docker, Docker Compose

---

## Integracje

W ramach zadania zaimplementowana została integracja z serwisem weryfikującym, czy przesłany URI jest phishingiem.
Komunikacja z serwisem odbywa się po protokole HTTP. Do integracji z systemem potrzebny token autoryzacyjny, który jest
podawany z poziomu konfiguracji skanera.

---

## Uruchamianie i konfiguracja

Wymagania:

1. Docker
2. Docker Compose
3. (opcjonalnie) Gradle, jeśli chcesz zbudować artefakt lokalnie

Wykonaj polecenie:

```shell
  docker compose -f docker/docker-compose.yaml up -d
```

Uwaga - Obraz uruchamia się poprawnie na środowiskach z systemem operacyjnym linux. W razie potrzeby uruchomienia na
innym systemie konieczne będzie zastosowanie opcji **platform linux/amd64** dla kontenera scanner w docker-compose.

Poniżej przedstawione są zmienne konfiguracyjne skanera. W razie potrzeby należy uzupełnić zmienną pożądaną wartościa i
umieścić w pliku scanner/.env a następnie wywołać powyższą komendę uruchamiającą środowisko.

```dotenv
PHISHING_SCANNER_PHONE_NUMBER=#Numer telefonu w formacie 48123123123, na który przychodzą wiadomość włączające lub wyłączające usługę skanowania.
URI_VERIFIER_BASE_URL=#Bazowy URL usługi weryfikacji phishingu. Przykład: https://api.example.com
URI_VERIFIER_EVALUATE_URI=#Ścieżka/URI endpointu weryfikacji (może być względna względem BASE_URL lub pełny URL). Przykład: /v1/evaluate
URI_VERIFIER_ACCESS_TOKEN=#Token autoryzacyjny do usługi weryfikacji phishingu (sekret)
URI_VERIFIER_RETRIES_NUMBER=#Ilość prób ponownych zapytań w razie błędów. Domyślnie: 3
URI_VERIFIER_DURATION_BETWEEN_RETRIES_SECONDS=#Czas (w sekundach) pomiędzy próbami. Domyślnie: 2
URI_VERIFIER_PASS_UNVERIFIED_MESSAGES=#Czy przepuszczać wiadomości niezweryfikowane (true/false). Domyślnie: false
```

## Testy

W ramach implementacji serwisu zostały utworzone testy jednostkowe sprawdzające logikę oraz integracyjne sprawdzające
przepływ wiadomości pomiedzy kolejką i skanerem.
Po uruchomieniu można manualnie przetestować działanie skanera poprzez ręczne przesłanie wiadomości na odpowiednią
kolejkę. Aby to zrobić należy wykonać następujące kroki:

1. Wejście na GUI RabbitMQ pod adresem http://localhost:15672/#/exchanges/%2F/new-message (do zalogowania się należy
   użyć danych dostępowych podanych w pliku docker/.env).
2. Opublikowanie wiadomości poprzez umieszczenie odpowiedniej treści json w zakładce publish message.

Po opublikowaniu wiadomości w logach kontenera widoczne będą operacje na podanej wiadomości.
Przykładowe treści SMSów (Można opublikować w poniższej kolejności aby uruchomić skaner dla numer, przetworzyć wiadomość
a następnie wyłączyć skaner dla numeru):

```json
{
  "sender": "48123456789",
  "receiver": "48123123123",
  "content": "START"
}
```

 ```json
{
  "sender": "48765432123",
  "receiver": "48123456789",
  "content": "Check this page please https://scam.com"
}
```

```json
{
  "sender": "48123456789",
  "receiver": "48123123123",
  "content": "STOP"
}
```

---

## Dalszy rozwój

1. Dodanie sprawdzania zdrowia kontenera.
2. Dopracowanie logów (przekazywanie do pliku, mechanizm rolowania i składowania logów).
3. Obsługa DLQ (Dead letter queue) dla błędnych wiadomości z kolejki aby nie traciły się i leciały gdzieś dalej np. do
   zapisu i weryfikacji.

---

## Dodatkowe uwagi

Zostało przyjęte założenie, że w razie odpowiedzi niebędącej sukcesem z serwisu weryfikacji URL pod kątem phishingu
wiadomość będzie przekazywana na kolejkę wiaodmości bezpiecznych.

Zostało przyjęte założenie, że jeśli użytkownik nie korzysta ze skanera, to jego wiadomości domyślnie nie będą
weryfikowane (serwis jest płatny więc byłyby to dodatkowe koszty) i od razu przekazywane dalej.

Obsługa produkcyjnego źródła danych jest możliwa lecz konieczne byłoby pobranie odpowiedniego sterownika bazy danych
(resztę konfiguracji można umieścić w pliku scanner/.env)

Zostało przyjęte założenie, że dla ułatwienia review, pliki compose oraz plik .env będą w repozytorium (normalnie pliki
.env by nie były ze względów bezpieczeństwa a compose byłby w oddzielny repozytorium).

Wymagania zadania:

1. Obsługiwanie wszystkich SMSów i odrzucanie phishingu - Skaner obsługuje wszystkie wiadomości i odrzuca lub przekazuje
   dalej wiadomości z tym, że odrzucenie jest przekazaniem wiadomości na inną kolejkę aby jej nie stracić.
2. Obsługa dodawania i usuwania numerów obsługiwanych przez skaner dzięki konfiguracji specjalnego numeru telefonu
   włączającego i wyłączającego usługę i specjalnej treści wiadomości.
3. Wszystkie wymagania techniczne spełnione (groovy to też środowisko JVM). Tworzony jest obraz docker, który jest
   automatycznie wypychany na dockerhub.