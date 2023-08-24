import { useState } from 'react';
import { useParams } from 'react-router-dom';
import ReportForm from '../../components/report/ReportForm';

function ReportRegister() {
  const [content, setContent] = useState('');
  const { reportedApiId } = useParams<{ reportedApiId: string }>();

  const handleSubmit = async () => {
    const response = await fetch(
      `http://localhost:8080/api/reports/${reportedApiId}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content }),
      }
    );

    if (response.ok) {
      alert('신고가 등록되었습니다.');
    } else {
      alert('신고 등록에 실패하였습니다.');
    }
  };

  return (
    <ReportForm
      content={content}
      setContent={setContent}
      handleSubmit={handleSubmit}
    />
  );
}

export default ReportRegister;
