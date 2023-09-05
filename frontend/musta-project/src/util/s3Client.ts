import { PutObjectCommand, S3Client, S3ClientConfig } from '@aws-sdk/client-s3';

// Set the AWS Region.
const REGION = 'ap-northeast-2'; //e.g. "ap-northeast-2"
const CREDENTIAL = {
  AWS_ACCESS_KEY_ID: import.meta.env.VITE_S3_ACCESS_KEY,
  AWS_SECRET_ACCESS_KEY: import.meta.env.VITE_S3_SECRET_ACCESS_KEY,
};
type S3ImageType = 'article' | 'review';

// Create an Amazon S3 service client object.
const s3Client = new S3Client({
  region: REGION,
  credentials: {
    accessKeyId: CREDENTIAL.AWS_ACCESS_KEY_ID,
    secretAccessKey: CREDENTIAL.AWS_SECRET_ACCESS_KEY,
  },
});

const uploadImagesToS3 = async (imageFiles: any, imageType: S3ImageType) => {
  const now = new Date(Date.now());
  const keyList: string[] = [];
  const timeFormatOption = {
    hour12: false,
    timeZone: 'Asia/Seoul',
  };
  try {
    // 각 이미지 파일을 S3에 업로드
    for (let i = 0; i < imageFiles.length; i++) {
      const holderName = imageFiles[0].name.split('.')[0];
      //  article/2023/09/05/10:20:00_name_uuid.png
      const objectKey = `${imageType}/${now.getFullYear()}/${
        now.getMonth() + 1
      }/${now.getDate()}/${now.toLocaleTimeString(
        'en-us',
        timeFormatOption
      )}_${holderName.replaceAll('_', '-')}_${self.crypto.randomUUID()}.${
        imageFiles[i].name.split('.')[1]
      }`;
      keyList.push(import.meta.env.VITE_S3 + `/${objectKey}`);
      const params = {
        Bucket: bucketName,
        Key: objectKey,
        Body: imageFiles[i],
        ContentType: imageFiles[i].type,
      };

      // 이미지를 S3에 업로드
      const result = await s3Client.send(new PutObjectCommand(params));

      console.log(`Uploaded image ${objectKey} to S3`);
    }

    // 모든 이미지가 업로드되었을 때 반환 또는 다른 처리를 수행
    return keyList;
  } catch (error) {
    console.error('Error uploading images to S3:', error);
    return null;
  }
};

const bucketName = '1team-s3';
export { s3Client, bucketName, uploadImagesToS3 };
