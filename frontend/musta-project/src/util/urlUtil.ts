export function getUrlSearchParams(url: string) {
  const curUrl = new URL(url);
  const curUrlSearchParams = new URLSearchParams(curUrl.search);

  return curUrlSearchParams;
}

export function setQuery(
  urlSearchParams: URLSearchParams,
  key: string,
  value: string
) {
  urlSearchParams.set(key, value);

  return `${urlSearchParams}`;
}
