import { useState } from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import ReportForm from './ReportForm.tsx';
import axiosUtils from "../../uitls/axiosUtils.ts";

type ReportParams = {
  resourceType: 'article' | 'review' | 'chat';
  resourceApiId: string;
};

function ReportRegister() {
  const [content, setContent] = useState('');
  const { resourceType, resourceApiId } = useParams<ReportParams>();
  const navigate = useNavigate()

  const handleSubmit = async () => {
    try {
      const response = await axiosUtils.post('/reports', {
        resourceType: resourceType,
        resourceApiId: resourceApiId,
        content: content,
      });
      alert('신고가 등록되었습니다.');

      if (resourceType === 'article') {
        navigate(`/article/detail/${resourceApiId}`);
      } else if (resourceType === 'review') {
        navigate(`/review/${resourceApiId}`);
      }

    } catch (error) {
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
