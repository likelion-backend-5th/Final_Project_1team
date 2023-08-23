export function toDate(str: string): Date {
  const m: string[] = str.split(/\D/);
  return new Date(+m[0], +m[1] - 1, +m[2], +m[3], +m[4], +m[5]);
}

export function isToday(str: string) {
  const givenDate: Date = toDate(str);
  const curDate: Date = new Date(Date.now());

  if (givenDate.getFullYear() !== curDate.getFullYear()) {
    return false;
  }

  if (givenDate.getMonth() !== curDate.getMonth()) {
    return false;
  }

  if (givenDate.getDate() !== curDate.getDate()) {
    return false;
  }

  return true;
}
