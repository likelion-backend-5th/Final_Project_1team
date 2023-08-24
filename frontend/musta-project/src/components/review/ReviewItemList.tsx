import {
  ListItem,
  ListItemText,
  Avatar,
  Typography,
  Rating,
  Box,
  Grid,
} from '@mui/material';
import { styled } from '@mui/system';
import { useNavigate } from 'react-router-dom';

const StyledListItem = styled(ListItem)`
  display: flex;
  align-items: center;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  transition: background-color 0.3s;
  cursor: pointer;
  &:hover {
    background-color: rgba(0, 0, 0, 0.05);
  }
`;

const ReviewInfoWrapper = styled(Grid)`
  display: flex;
  flex-direction: column;
  align-items: flex-end;
`;

const StyledBox = styled(Box)`
  display: flex;
  margin-bottom: 2%;
`;

const UserAvatar = styled(Avatar)`
  width: 48px;
  height: 48px;
  margin-right: 12px;
`;

const ReviewItemText = styled(ListItemText)`
  flex: 1;
`;

const ReviewItemList = ({ review }: any) => {
  const navigate = useNavigate();

  const handleItemClick = () => {
    navigate(`/review/${review.apiId}`);
  };

  return (
    <StyledListItem onClick={handleItemClick}>
      <div>
        <StyledBox>
          <UserAvatar alt={review.username} src="/path/to/user-image.jpg" />
          <ReviewItemText primary={review.username} />
          <ReviewInfoWrapper>
            <Rating
              name="read-only"
              value={review.point}
              readOnly
              style={{ marginBottom: '2%' }}
            />
            <Typography variant="caption" color="textSecondary">
              {review.createdAt}
            </Typography>
          </ReviewInfoWrapper>
        </StyledBox>
        <Typography variant="body1" color="textSecondary">
          {review.content}
        </Typography>
      </div>
    </StyledListItem>
  );
};

export default ReviewItemList;
