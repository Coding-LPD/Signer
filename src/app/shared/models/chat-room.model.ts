export class ChatRoom {

  constructor(
    public _id?: string,
    public courseId?: string,
    public studentIds?: string[],
    public name?: string,
    public avatar?: string
  ) {}

}