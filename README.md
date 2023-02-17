# file-extention-check

- 사이트 : https://port-0-file-extention-check-3a9t2ble8nvxss.sel3.cloudtype.app/

- 동시성 문제 고려
  - ReentrantLock (단일 인스턴스 채택)
    - 테스트 결과 -> lock의 범위가 작고 빠름
    - 서버 확장 시 추가적인 방안 필요
    - 2000개 기준 5sec 882ms ~ 7sec 002ms

  - Pessimistic Lock
    - 테스트 결과 -> record lock의 범위가 너무 넓음
    - 서버 확장 시에도 사용 가능 (db x)
    - 2000개 기준 15sec 992ms ~ 17sec 102ms

- REST API + SSR
- 중복제거
  - DataIntegrityViolationException -> catch -> convert DuplicateFileExtensionExce
