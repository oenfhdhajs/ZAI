import MessageResponse from './SuccessResponse';

export default interface ErrorResponse extends MessageResponse {
  stack?: string;
}