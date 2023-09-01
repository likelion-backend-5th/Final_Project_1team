import { S3Client } from '@aws-sdk/client-s3';
// Set the AWS Region.
const REGION = 'ap-northeast-2'; //e.g. "ap-northeast-2"
// Create an Amazon S3 service client object.
const s3Client = new S3Client({ region: REGION });
export { s3Client };
