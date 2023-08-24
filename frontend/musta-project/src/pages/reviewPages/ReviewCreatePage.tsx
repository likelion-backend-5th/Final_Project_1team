import { styled } from '@mui/material';
import { Paper } from '@mui/material';
import ReviewCreateItem from '../../components/review/ReviewCreateItem';

const StyledPaper = styled(Paper)`
  margin-top: 20px;
  padding: 20px;
`;

const ReviewCreatePage = () => {
  return (
    <StyledPaper>
      <ReviewCreateItem />
    </StyledPaper>
  );
};

export default ReviewCreatePage;
