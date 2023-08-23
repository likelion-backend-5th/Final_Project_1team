import { styled } from '@mui/material';
import { Paper } from '@mui/material';
import { useLocation } from 'react-router-dom';
import ReviewEditItem from '../../components/review/ReviewEditItem';

const StyledPaper = styled(Paper)`
  margin-top: 20px;
  padding: 20px;
`;

const ReviewEditPage = () => {
  const location = useLocation();
  const review = { ...location.state };

  return (
    <StyledPaper>
      <ReviewEditItem review={review} />
    </StyledPaper>
  );
};

export default ReviewEditPage;
