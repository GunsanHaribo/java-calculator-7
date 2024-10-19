# java-calculator-precourse

# java-calculator-precourse

## 🚀 기능 요구 사항

입력한 문자열에서 숫자를 추출하여 더하는 계산기를 구현한다.

- 쉼표(,) 또는 콜론(:)을 구분자로 가지는 문자열을 전달하는 경우 구분자를 기준으로 분리한 각 숫자의 합을 반환한다.

- 앞의 기본 구분자(쉼표, 콜론) 외에 커스텀 구분자를 지정할 수 있다.
  커스텀 구분자는 **문자열 앞부분**의 "//"와 "\n" 사이에 위치하는 문자를 커스텀 구분자로 사용한다.
  예를 들어 "//;\n1;2;3"과 같이 값을 입력할 경우 커스텀 구분자는 세미콜론(;)이며, 결과 값은 6이 반환되어야 한다.

- 사용자가 잘못된 값을 입력할 경우 IllegalArgumentException을 발생시킨 후 애플리케이션은 종료되어야 한다.

### 구현 제한사항

- 입력은 구분자와 양수로 구성된 문자열이다.
- 출력 형식
   ~~~
   결과 : 6
   ~~~

### 실행 결과 예시

| Before input     | Input      | Result |
|------------------|------------|--------|
| 덧셈할 문자열을 입력해 주세요 | ""         | 결과 : 0 |
|                  | 1,2        | 결과 : 3 |
|                  | 1,2,3      | 결과 : 6 |
|                  | 1,2:3      | 결과 : 6 |
|                  | //;\n1;2;3 | 결과 : 6 |

## ✅ 문제풀이 기본 요구사항 정리

### 1. 빈 입력처리

| 기능     | 세부사항      | 입력예시 | 출력 | 완료여부 |
|--------|-----------|------|----|------|
| 빈 입력처리 | 빈문자열 : "" | "/n" | 0  | ✅    |

### 2. 구분자가 없을 경우

| 기능         | 세부사항          | 입력예시             | 출력                       | 완료여부 |
|------------|---------------|------------------|--------------------------|------|
| 구분자가 없는 경우 | 숫자나 문자만 있는 경우 | asdfgdsf, 123241 | IllegalArgumentException | ✅    |

### 3. 기본 구분자 처리(쉼표와 세미콜론)

| 기능            | 세부사항                   | 입력예시                                                    | 출력                        | 완료여부 |
|---------------|------------------------|---------------------------------------------------------|---------------------------|-----|
|               | 구분자만 있는 경우             | ;;;;; 또는 ;;;,,, 또는 ,,,,                                 | IllegalArgumentException  | ✅   |
| 정상적인 덧셈       | 기본 구분자의 정상적인 덧셈        | 1,2,3 또는 1;2;3 또는 1;2,3                                 | 6                         | ✅   |
|               | 음수가 포함된 경우             | -1,2,3                                                  | IllegalArgumentException  | ✅   |
|               | 0이 포함된 경우              | 0,2;3                                                   | IllegalArgumentException  |  ✅   |
|               | 형식은 맞췄지만 중간에 공백이 있는 경우 | 1;2 ;3 ;4;5                                             | 15                        |✅ |
|               | 구분자가 아닌 문자를 포함할 경우     | :,kk333333                                              | IllegalArgumentException  |  ✅   |
| 자료형 범위 초과할 경우 | 양수 하나가 int의 범위를 넘는 경우  | 2_147_483_648                                           | 2_147_483_648             |  ✅   |
|               | 양수 하나가 long의 범위를 넘는 경우 | 9_223_372_036_854_775_808                               | 9_223_372_036_854_775_808 |  ✅   |
|               | 연산 결과가 int의 범위를 넘는 경우  | 2_147_483_647;1 2_147_483_647,1                         | 2_147_483_648             |  ✅   |
|               | 연산 결과가 long의 범위를 넘는 경우 | 9_223_372_036_854_775_807;1 9_223_372_036_854_775_807,1 | 9_223_372_036_854_775_808 |  ✅   |

### 4. 커스텀 구분자 처리(//구분자\n)

| 기능            | 세부사항                     | 입력예시                                                         | 출력                        | 완료여부                |
|---------------|--------------------------|--------------------------------------------------------------|---------------------------|---------------------|
|               | 커스텀 구분자만 있는 경우           | //??\n                                                       | IllegalArgumentException  |         ✅           |
|               | 커스텀 구분자가 2개 이상 있는 경우     | //??\n//!!\n33                                               | IllegalArgumentException  |        ✅            |
|               | 커스텀 구분자 앞에 문자나 숫자가 있는경우  | ~~~//??\n33                                             | IllegalArgumentException  |           ✅           |
|               | 커스텀 구분자가 숫자일 경우          | //3\n//4\n//5\n11394                                         | IllegalArgumentException  |        ✅             |
| 정상적인 덧셈       | 커스텀 구분자가 있는 정상적인 덧셈      | //?\n12?345                                                  | 357                       |       ✅               |
|               | 커스텀 구분자와 숫자 하나있을떄        | //?\n33                                                      | 33                        |  ✅ |
|               | 구분자가 아닌 문자를 포함할 경우       | //?\n33kk3333                                                | IllegalArgumentException  |      ✅               |
|               | 형식은 맞췄지만 중간에 공백이 있는 경우   | //?\n1?2 ?3 ?4?5                                             | 15                        |      ✅               |
| 자료형 범위 초과할 경우 | 양수 하나가 int의 범위를 넘는 경우    | //?\n2_147_483_648                                           | 2_147_483_648             |        ✅             |
|               | 양수 하나가 long의 범위를 넘는 경우   | //?\n9_223_372_036_854_775_808                               | 9_223_372_036_854_775_808 |                     |
|               | 연산 결과가 int의 범위를 넘는 경우    | //?\n2_147_483_647?1 2_147_483_647,1                         | 2_147_483_648             |                     |
|               | 연산 결과가 long의 범위를 넘는 경우   | //?\n 9_223_372_036_854_775_807?1 9_223_372_036_854_775_807,1 | 9_223_372_036_854_775_808 |                     |