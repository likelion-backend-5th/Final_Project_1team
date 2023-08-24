import React from 'react';

type ReportFormProps = {
  content: string;
  setContent: (value: string) => void;
  handleSubmit: () => void | Promise<void>;
};

const ReportForm: React.FC<ReportFormProps> = ({
  content,
  setContent,
  handleSubmit,
}) => {
  return (
    <div>
      <textarea
        placeholder="신고 내용을 입력하세요"
        value={content}
        onChange={(e) => setContent(e.target.value)}
        style={{ width: '100%' }}
      />
      <button onClick={handleSubmit}>신고하기</button>
    </div>
  );
};

export default ReportForm;
