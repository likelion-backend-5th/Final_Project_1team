import { styled } from '@mui/material';
import { Paper } from '@mui/material';
import ReviewItem from '../../components/review/ReviewItem';
import { useParams } from 'react-router-dom';

const StyledPaper = styled(Paper)`
  margin-top: 20px;
  padding: 20px;
`;

const ReviewPage = () => {
  const params: any = useParams();
  let reviewApiId: any = params.reviewApiId;

  return (
    <StyledPaper>
      <ReviewItem reviewApiId={reviewApiId} />
    </StyledPaper>
  );
};

export default ReviewPage;
