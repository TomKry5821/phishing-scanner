# phishing-scanner

Repozytorium zawierające kod źródłowy projektu skanera SMS-ów phishingowych.

## Opis działania

phishing-scanner to serwis odpowiadający ze weryfikację przychodzących SMS-ów pod kątem phishingu. Skaner weryfikuje
przychodzące wiadomości oraz w zależności od tego, czy wiadomość jest phishingiem odrzuca ją lub przekazuje dalej.
Skaner przyjmuje SMS-y z podziałem na 3 rodzaje:

1. Włączający weryfikację dla numeru wysyłającego - SMS ten powinien zostać wysłany na odpowiedni numer oraz posiadać
   treść "START".
2. Wyłączający weryfikację dla numeru wysyłającego - SMS ten powinien zostać wysłany na odpowiedni numer oraz posiadać
   treść "STOP".
3. Zwykły tj. zawierający ogólną treść, którą należy zweryfikować.

SMS-y na numery, które nie posiadają włączonej usługi będą przekazywane dalej bez weryfikacji.

## Przyjęta architektura

Skaner jest serwisem działającym w postaci serwera. Posiada połączenia do:

1. Systemu kolejkowego, z którego odbiera przychodzące SMS-y oraz przekazuje je dalej na odpowiednie kolejki w
   zależności od wyniku weryfikacji.
2. Źródła danych, w którym utrwala dane dotyczące numerów telefonów z włączoną lub wyłączoną weryfikacją.

Przyjęta architektura pozwala na utworzenie prostego, wydajnego i skalowalnego rozwiązania. Głównymi zaletami
architektury jest możliwość asynchronicznej obsługi przechodzących przez sieć wiadomości i zastosowanie skanera jako
filtra w infrastrukturze większego systemu. Na potrzeby realizacji zadania, w ramach infrastruktury pominięte zostaną
elementy monitoringu, oraz pośrednika z uwagi na to, że nie są one konieczne do prezentacji działania skanera.

## Wykorzystane technologie

Serwis skanera jest zaimplementowany w języku java z użyciem frameworka spring-boot. Systemem kolejkowym użytym jako
element infrastruktury będzie RabbitMQ lecz samo rozwiązanie zostanie zaimplementowane przy użyciu Spring Cloud Stream,
które pozwala na przepinanie pomiędzy różne systemy kolejkowe bez zmian w kodzie. Źródłem danych użytym jako element
infrastruktury będzie wewnętrzna baza H2 z uwagi na to, że jest ona całkowicie wystarczalna do prezentacji działania
skanera natomiast rozwiązanie będzie dostosowane do konfigurowalnego źródła danych opartego o SQL. Testy jednostkowe
oraz integracyjne są napisane w języku Groovy z wyykorzystaniem Frameworka Spock.

Rozwiązanie jest skonteneryzowane tj. skaner będzie możliwy do pobrania w postaci obrazu docker a następnie uruchomiony
jako kontener.

## Integracje

W ramach zadania zaimplementowana została integracja z serwisem weryfikującym, czy przesłany URI jest phishingiem.
Komunikacja z serwisem odbywa się po protokole HTTP. Do integracji z systemem potrzebny token autoryzacyjny, który jest
podawany z poziomu konfiguracji skanera.

## Uruchamianie