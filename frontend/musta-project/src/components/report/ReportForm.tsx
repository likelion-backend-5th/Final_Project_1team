import React from 'react';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';

type ReportFormProps = {
    content: string;
    setContent: (value: string) => void;
    handleSubmit: () => void | Promise<void>;
};

const ReportForm: React.FC<ReportFormProps> = ({content, setContent, handleSubmit}) => {
    return (
        <Box
            sx={{
                p: 3,
                borderRadius: 1,
                boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)'
            }}
        >
            <Typography variant="h5" mb={2}>
                신고 등록
            </Typography>
            <Typography variant="body1" mb={3}>
                문제가 되는 내용이나 사용자의 행동을 신고해주세요. 우리는 모든 신고를 검토하고 필요한 조치를 취하겠습니다.
            </Typography>
            <TextField
                fullWidth
                variant="outlined"
                multiline
                rows={4}
                placeholder="신고 내용을 입력하세요"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                ></TextField>
            <Box display="flex" justifyContent="flex-end">
                <Button variant="contained" color="primary" onClick={handleSubmit}>
                    신고하기
                </Button>
            </Box>
        </Box>
    );
};

export default ReportForm;
