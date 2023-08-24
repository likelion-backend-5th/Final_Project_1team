export function statusToKorean(status: string): string {
    switch (status) {
        case "PENDING":
            return "대기중";
        case "IN_REVIEW":
            return "검토중";
        case "RESOLVED":
            return "해결됨";
        case "DISMISSED":
            return "거절됨";
        default:
            return status;
    }
}