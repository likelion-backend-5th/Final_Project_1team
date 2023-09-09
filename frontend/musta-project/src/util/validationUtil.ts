/**
 * 주어진 value 값이 truthy 한 값이 경우 true 리턴
 * 아닐 경우 false 리턴
 * @param value
 * string
 */
export function textValidation(value: string) {
  return !!value;
}

/**
 * 주어진 value 값이 0보다 큰 숫자 일 경우 true 리턴
 * 아닐 경우 false 리턴
 * @param value
 * number
 */
export function priceValidation(value: number) {
  if (!value) {
    return false;
  }

  if (value < 0) {
    return false;
  }

  if (value > 9999999999) {
    return false;
  }

  return true;
}
