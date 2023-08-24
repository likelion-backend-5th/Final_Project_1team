import { styled } from '@mui/material';
import { Paper } from '@mui/material';
import ReviewListForm from '../../components/review/ReviewListForm';

const StyledPaper = styled(Paper)`
  margin-top: 20px;
  padding: 20px;
`;

const ReviewListPage = () => {
  return (
    <StyledPaper>
      <ReviewListForm />
    </StyledPaper>
  );
};

export default ReviewListPage;
