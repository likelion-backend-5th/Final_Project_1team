import styled from '@emotion/styled';

const StatusBadge = styled.span<{ status: string }>`
  display: inline-block;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: bold;

  ${(props) => {
    switch (props.status) {
      case 'PENDING':
        return 'background-color: #FFD700;'; // 금색
      case 'IN_REVIEW':
        return 'background-color: #1E90FF;'; // 파랑색
      case 'RESOLVED':
        return 'background-color: #32CD32;'; // 녹색
      case 'DISMISSED':
        return 'background-color: #FF4500;'; // 주황색
      default:
        return '';
    }
  }}
`;

export default StatusBadge;
