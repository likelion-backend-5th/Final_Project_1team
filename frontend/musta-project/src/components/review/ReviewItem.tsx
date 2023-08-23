import { Button, Rating, Typography, styled } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ContentsDiv = styled('div')`
  display: flex;
`;

const RatingDiv = styled('div')`
  max-width: 400px;
  text-align: center;
`;

const ReviewItem = ({ reviewApiId }: any) => {
  // 현재 로그인 되어있는 계정의 username 더미
  const [username, setUsername] = useState('김감자');

  const navigate = useNavigate();

  // 더미 데이터
  const [review, setReview] = useState([
    {
      apiId: 'review1',
      content:
        '미안하다 이거 보여주려고 어그로끌었다.. 나루토 사스케 싸움수준 ㄹㅇ실화냐? 진짜 세계관최강자들의 싸움이다.. 그찐따같던 나루토가 맞나? 진짜 나루토는 전설이다..',
      point: 4,
      username: '김감자',
      reviewStatus: 'UPLOAD',
    },
  ]);

  const userHandler = () => {
    if (review[0].username == username) return true;
    return false;
  };

  const editHandler = ({ review }: any) => {
    navigate(`/review/edit/${reviewApiId}`, {
      state: {
        apiId: `${review[0].apiId}`,
        content: `${review[0].content}`,
        point: `${review[0].point}`,
        username: `${review[0].username}`,
        reviewStatus: `${review[0].reviewStatus}`,
      },
    });
  };

  // const deleteHandler = () => {};

  return (
    <div>
      {review ? (
        <>
          <Typography align="left" variant="h5" gutterBottom>
            <h3 style={{ margin: 'auto' }}>{review[0].username}님의 리뷰</h3>
            <br></br>
            <ContentsDiv>
              <div style={{ marginRight: '2%' }}>
                <img
                  style={{ maxWidth: '400px' }}
                  src="/img/favicon.png"
                  title="예시 이미지"
                  alt="no Image"></img>
              </div>
              <div>
                <h5>{review[0].content}</h5>
              </div>
            </ContentsDiv>
            <RatingDiv>
              <Rating name="read-only" value={review[0].point} readOnly />
            </RatingDiv>
            <div style={{ textAlign: 'right' }}>
              {userHandler() && (
                <>
                  <Button
                    style={{ margin: '0.5rem' }}
                    variant="outlined"
                    color="warning"
                    startIcon={<EditIcon />}
                    onClick={() => editHandler({ review })}>
                    수정
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    startIcon={<DeleteIcon />}>
                    삭제
                  </Button>
                </>
              )}
            </div>
          </Typography>
        </>
      ) : (
        <p>존재하지 않는 리뷰</p>
      )}
    </div>
  );
};

export default ReviewItem;
